package eu.wewox.modalsheet.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeDownChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import eu.wewox.modalsheet.FullScreenPopup
import eu.wewox.modalsheet.ui.components.BottomNavItem
import eu.wewox.modalsheet.ui.components.BottomNavigation
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

@Composable
fun CustomFullScreenPopupScreen() {
    var item by remember { mutableStateOf(BottomNavItem.Home) }
    Scaffold(
        bottomBar = {
            BottomNavigation(
                current = item,
                onClick = { item = it },
            )
        }
    ) { padding ->
        Crossfade(targetState = item) { current ->
            Scaffold(
                topBar = { TopBar(current.title) },
                modifier = Modifier.padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    when (current) {
                        BottomNavItem.Home -> HomeScreen()
                        BottomNavItem.Counter -> Spacer(Modifier)
                        BottomNavItem.Profile -> Spacer(Modifier)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen() {
    val items = remember { mutableStateListOf(*List(100) { it }.toTypedArray()) }
    LazyColumn(
        contentPadding = PaddingValues(SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(SpacingMedium),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it }) {
            Item(
                id = it,
                onRemove = { items.remove(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()
            )
        }
    }
}

@Composable
private fun Item(
    id: Int,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(SpacingSmall)
        ) {
            Text(
                text = "Item #$id",
                modifier = Modifier.weight(1f)
            )
            ConfirmableRemoveButton(
                onRemove = onRemove,
            )
        }
    }
}

@Composable
private fun ConfirmableRemoveButton(
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by rememberSaveable { mutableStateOf(false) }

    var buttonPosition by remember { mutableStateOf(Offset.Zero) }
    var buttonSize by remember { mutableStateOf(IntSize.Zero) }

    IconButton(
        onClick = { visible = true },
        modifier = modifier.onGloballyPositioned {
            buttonPosition = it.positionInWindow()
            buttonSize = it.size
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }

    ConfirmationCirclePopup(
        visible = visible,
        onDismiss = { visible = false },
        focus = buttonPosition + Offset(buttonSize.width / 2f, buttonSize.height / 2f),
    ) {
        var confirmed by remember { mutableStateOf(false) }
        DisposableEffect(Unit) {
            onDispose {
                if (confirmed) {
                    onRemove()
                }
            }
        }
        IconButton(
            onClick = {
                confirmed = true
                visible = false
            },
            modifier = Modifier.offset { IntOffset(buttonPosition.x.toInt(), buttonPosition.y.toInt()) }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colors.error,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ConfirmationCirclePopup(
    visible: Boolean,
    onDismiss: () -> Unit,
    focus: Offset,
    content: @Composable () -> Unit,
) {
    val fraction by animateFloatAsState(
        targetValue = if (visible) 2f else 0f,
        animationSpec = if (visible) tween(600) else tween(400)
    )
    if (!visible && fraction == 0f) {
        return
    }

    val circleFraction = fraction.coerceAtMost(1f)
    val textAlpha = (fraction - 1).coerceAtLeast(0f)

    FullScreenPopup(onDismiss = onDismiss) {
        val holeSize = with(LocalDensity.current) { HoleSize.toPx() }
        val circleSize = with(LocalDensity.current) { CircleSize.toPx() }
        val primary = MaterialTheme.colors.primary
        val secondary = MaterialTheme.colors.secondary

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.99f) // Trick for DstOut to work correctly
                .drawBehind {
                    drawRect(primary.copy(alpha = circleFraction * 0.6f))
                    drawCircle(secondary, circleFraction * circleSize, focus)
                    drawCircle(Color.Black, holeSize, focus, blendMode = BlendMode.DstOut)
                }
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            val down = awaitFirstDown()
                            down.consumeDownChange()

                            val change = waitForUpOrCancellation() ?: return@awaitPointerEventScope

                            if ((change.position - focus).getDistance() >= circleSize) {
                                onDismiss()
                            }
                        }
                    }
                }
        ) {
            ConfirmationText(
                focus = focus,
                circleSize = circleSize,
                holeSize = holeSize,
                alpha = textAlpha
            )

            content()
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.ConfirmationText(
    focus: Offset,
    circleSize: Float,
    holeSize: Float,
    alpha: Float,
) {
    val sizeDp = CircleSize - HoleSize
    val sizePx = circleSize - holeSize
    val offset = when {
        focus.y < constraints.maxHeight / 3 -> Offset(-0.75f, +0.75f)
        focus.y > constraints.maxHeight * 2 / 3 -> Offset(-0.75f, -0.75f)
        else -> Offset(-1f, 0f)
    } * sizePx * 0.65f
    var textSize by remember { mutableStateOf(IntSize.Zero) }
    Text(
        text = "Are you sure?",
        color = MaterialTheme.colors.onSecondary,
        style = MaterialTheme.typography.h3,
        textAlign = TextAlign.Center,
        onTextLayout = { textSize = it.size },
        modifier = Modifier
            .sizeIn(maxWidth = sizeDp)
            .offset {
                IntOffset(
                    focus.x.toInt() + offset.x.toInt() - textSize.width / 2,
                    focus.y.toInt() + offset.y.toInt() - textSize.height / 2
                )
            }
            .alpha(alpha)
    )
}

private val CircleSize = 250.dp
private val HoleSize = 40.dp
