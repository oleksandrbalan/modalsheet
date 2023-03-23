@file:OptIn(ExperimentalSheetApi::class, ExperimentalMaterialApi::class)

package eu.wewox.modalsheet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import eu.wewox.modalsheet.Example
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall
import kotlinx.coroutines.launch

/**
 * Showcases the most lower-level [ModalSheet] usage with [ModalBottomSheetState].
 */
@Composable
fun SheetStateModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.SheetStateModalSheet.label) }
    ) { padding ->
        val scope = rememberCoroutineScope()
        var pageIndex by remember { mutableStateOf(0) }
        val sheetState = rememberModalBottomSheetState(
            skipHalfExpanded = true,
            initialValue = ModalBottomSheetValue.Hidden,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        pageIndex = 0
                        sheetState.show()
                    }
                }
            ) {
                Text(text = "Show modal sheet")
            }
        }

        PageModalSheet(
            sheetState = sheetState,
            pageIndex = pageIndex,
            onNext = {
                scope.launch {
                    sheetState.hide()
                    if (pageIndex < Pages.lastIndex) {
                        pageIndex += 1
                        sheetState.show()
                    }
                }
            },
            onPrev = {
                scope.launch {
                    sheetState.hide()
                    if (pageIndex > 0) {
                        pageIndex -= 1
                        sheetState.show()
                    }
                }
            },
        )
    }
}

@Composable
private fun PageModalSheet(
    sheetState: ModalBottomSheetState,
    pageIndex: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit,
) {
    ModalSheet(
        sheetState = sheetState,
        onDismiss = if (sheetState.isVisible || sheetState.currentValue != sheetState.targetValue) onPrev else null
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(SpacingMedium)
        ) {
            val page = Pages[pageIndex]
            Text(
                text = page.title,
                style = MaterialTheme.typography.h4
            )
            Icon(
                imageVector = page.icon,
                contentDescription = page.title,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(256.dp)
            )
            Button(
                onClick = onNext
            ) {
                Text(text = if (pageIndex < Pages.lastIndex) "Show next" else "Close")
            }
        }
    }
}

private data class Page(
    val title: String,
    val icon: ImageVector,
)

private val Pages = listOf(
    Page("Left", Icons.Rounded.KeyboardArrowLeft),
    Page("Up", Icons.Rounded.KeyboardArrowUp),
    Page("Right", Icons.Rounded.KeyboardArrowRight),
    Page("Down", Icons.Rounded.KeyboardArrowDown),
)
