package eu.wewox.modalsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import eu.wewox.modalsheet.ModalSheetDefaults.Scrim
import kotlin.math.roundToInt

/**
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 *
 * @param data The data to show in the content. If null, hides the modal sheet.
 * @param onDismiss Called when user touches the scrim or swipes the sheet away.
 * @param shape Defines the modal's shape
 * @param backgroundColor The background color. Use Color.Transparent to have no color.
 * @param swipeEnabled True if swipe interaction with bottom sheet is enabled.
 * @param scrimClickEnabled True if click on scrim is enabled.
 * @param scrim Optional custom scrim.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun <T> ModalSheet(
    data: T?,
    onDismiss: () -> Unit,
    shape: Shape = MaterialTheme.shapes.large.copy(bottomEnd = CornerSize(0), bottomStart = CornerSize(0)),
    backgroundColor: Color = MaterialTheme.colors.surface,
    swipeEnabled: Boolean = true,
    scrimClickEnabled: Boolean = true,
    scrim: @Composable BoxScope.(Boolean) -> Unit = {
        Scrim(it, onScrimClick = if (scrimClickEnabled) onDismiss else NoOpLambda)
    },
    content: @Composable ModalSheetScope.(T) -> Unit,
) {
    var lastNonNullData by remember { mutableStateOf(data) }
    DisposableEffect(data) {
        if (data != null) lastNonNullData = data
        onDispose {}
    }

    ModalSheet(
        visible = data != null,
        onDismiss = onDismiss,
        shape = shape,
        backgroundColor = backgroundColor,
        swipeEnabled = swipeEnabled,
        scrimClickEnabled = scrimClickEnabled,
        scrim = scrim,
    ) {
        lastNonNullData?.let {
            content(it)
        }
    }
}

/**
 * Static modal sheet that behaves like bottom sheet and draws over system UI.
 * Non-data variant should contain only static [content]. For dynamic content use [ModalSheet].
 *
 * @param visible True if modal should be visible.
 * @param onDismiss Called when user touches the scrim or swipes the sheet away.
 * @param shape Defines the modal's shape
 * @param backgroundColor The background color. Use Color.Transparent to have no color.
 * @param swipeEnabled True if swipe interaction with bottom sheet is enabled.
 * @param scrimClickEnabled True if click on scrim is enabled.
 * @param scrim Optional custom scrim.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun ModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    shape: Shape = MaterialTheme.shapes.large.copy(
        bottomEnd = CornerSize(0),
        bottomStart = CornerSize(0)
    ),
    backgroundColor: Color = MaterialTheme.colors.surface,
    swipeEnabled: Boolean = true,
    scrimClickEnabled: Boolean = true,
    scrim: @Composable BoxScope.(Boolean) -> Unit = {
        Scrim(it, onScrimClick = if (scrimClickEnabled) onDismiss else NoOpLambda)
    },
    content: @Composable ModalSheetScope.() -> Unit,
) {
    // If content animated by AnimatedVisibility in PopupBody is still visible (in composition)
    var contentVisible by remember { mutableStateOf(false) }

    if (visible || contentVisible) {
        FullScreenPopup(
            onDismiss = onDismiss
        ) {
            PopupBody(
                visible = visible,
                scrim = scrim,
                content = {
                    // Set contentVisible true when the composable enters composition and false when it leaves
                    DisposableEffect(Unit) {
                        contentVisible = true
                        onDispose { contentVisible = false }
                    }

                    val scope = remember { ModalSheetScopeImpl() }
                    SwipeableContent(
                        swipeEnabled = swipeEnabled,
                        onSwipedAway = onDismiss,
                        scrollState = scope.scrollState
                    ) {
                        Surface(
                            shape = shape,
                            color = backgroundColor,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            scope.content()
                        }
                    }
                }
            )
        }
    }
}

/**
 * The scope for [ModalSheet] content.
 */
@ExperimentalSheetApi
public interface ModalSheetScope {

    /**
     * The scroll state.
     * Must be passed to [.verticalScroll] modifier to support scrolling inside content.
     */
    public val scrollState: ScrollState
}

/**
 * Contains the default values used by [ModalSheet].
 */
@ExperimentalSheetApi
public object ModalSheetDefaults {

    /**
     * The default scrim component.
     *
     * @param popupVisible True if popup is visible, used to alter scrim color.
     * @param onScrimClick Called when user clicks on the scrim.
     * @param color The default scrim color (without alpha).
     */
    @ExperimentalSheetApi
    @Composable
    public fun BoxScope.Scrim(
        popupVisible: Boolean,
        onScrimClick: () -> Unit,
        color: Color = Color.Black,
    ) {
        val scrimAlpha by animateFloatAsState(
            targetValue = if (popupVisible) ScrimAlpha else 0f,
            animationSpec = tween(AnimationDuration)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(color.copy(alpha = scrimAlpha))
                .clickableWithoutRipple(onScrimClick)
        )
    }
}

@Composable
private fun PopupBody(
    visible: Boolean,
    scrim: @Composable BoxScope.(Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    // If content should be visible - derived from visible parameter
    var popupVisible by remember { mutableStateOf(false) }

    LaunchedEffect(visible) {
        // Set in next composition so AnimatedVisibility is laid out
        popupVisible = visible
    }

    Box(Modifier.fillMaxSize()) {
        scrim(popupVisible)

        AnimatedVisibility(
            visible = popupVisible,
            // Slide animations with initial and end positions in bottom
            enter = slideInVertically(tween(AnimationDuration), initialOffsetY = { it }),
            exit = slideOutVertically(tween(AnimationDuration), targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .consumeClicks()
        ) {
            content()
        }
    }
}

@Composable
private fun SwipeableContent(
    swipeEnabled: Boolean,
    onSwipedAway: () -> Unit,
    scrollState: ScrollState,
    content: @Composable () -> Unit,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val swipeableState = rememberSwipeableState(0)
    val scrollConnection = remember { ModalSheetScrollConnection(swipeEnabled, swipeableState, scrollState) }
    val anchors = remember(size) { mapOf(0f to 0, size.height.toFloat() to size.height) }

    val swipeOffset by swipeableState.offset
    val completed = with(swipeableState) {
        // Offset needs to be positive because the fraction is 1 in resting state
        val movedFromRestingState = offset.value > 1
        // If below bottom threshold
        val progressFinished = progress.fraction > BottomSwipeThreshold

        movedFromRestingState && progressFinished
    }

    LaunchedEffect(completed) {
        if (completed) {
            onSwipedAway()
        }
    }

    // Swipeable container
    Box(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(TopSwipeThreshold) },
                orientation = Orientation.Vertical,
                resistance = SwipeableDefaults.resistanceConfig(anchors.keys, 0f, 0f),
                enabled = swipeEnabled
            )
            .nestedScroll(scrollConnection)
    ) {
        // Content moved by swipe offset
        Box(
            modifier = Modifier
                .onSizeChanged { size = it }
                .offset { IntOffset(0, swipeOffset.roundToInt()) }
        ) {
            content()
        }
    }
}

/**
 * The NestedScrollConnection, which delegates scroll to the [swipeableState] to make bottom sheet swipeable and
 * scrollable.
 *
 * Credits: https://proandroiddev.com/how-to-master-swipeable-and-nestedscroll-modifiers-in-compose-bb0635d6a760
 */
private class ModalSheetScrollConnection(
    private val swipeEnabled: Boolean,
    private val swipeableState: SwipeableState<Int>,
    private val scrollState: ScrollState,
) : NestedScrollConnection {

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        return if (delta < 0) {
            swipeableState.performDrag(delta).toYOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        if (!swipeEnabled && delta > 0) {
            return Offset.Zero
        }
        return swipeableState.performDrag(delta).toYOffset()
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        return if (available.y < 0 && scrollState.value == 0) {
            swipeableState.performFling(available.y)
            available
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
        swipeableState.performFling(velocity = available.y)
        return super.onPostFling(consumed, available)
    }

    private fun Float.toYOffset() = Offset(0f, this)
}

@ExperimentalSheetApi
private class ModalSheetScopeImpl : ModalSheetScope {
    override val scrollState: ScrollState = ScrollState(0)
}

private const val AnimationDuration = 150
private const val ScrimAlpha = 0.6f
private const val TopSwipeThreshold = 0.3f
private const val BottomSwipeThreshold = 0.8f
