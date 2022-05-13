package eu.wewox.modalsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 *
 * @param data The data to show in the content. If null, hides the modal sheet.
 * @param onDismiss Called when user touches the scrim or swipes the sheet away.
 * @param cancelable True if this modal could be closed with swipe gesture, tap on scrim or tap on
 * hardware back button.
 * @param shape The shape of the bottom sheet.
 * @param elevation The elevation of the bottom sheet.
 * @param backgroundColor The background color of the bottom sheet.
 * @param contentColor The preferred content color provided by the bottom sheet to its
 * children. Defaults to the matching content color for [backgroundColor], or if that is not
 * a color from the theme, this will keep the same content color set above the bottom sheet.
 * @param scrimColor The color of the scrim that is applied to the rest of the screen when the
 * bottom sheet is visible. If the color passed is [Color.Unspecified], then a scrim will no
 * longer be applied and the bottom sheet will not block interaction with the rest of the screen
 * when visible.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun <T> ModalSheet(
    data: T?,
    onDismiss: () -> Unit,
    cancelable: Boolean = true,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    var lastNonNullData by remember { mutableStateOf(data) }
    DisposableEffect(data) {
        if (data != null) lastNonNullData = data
        onDispose {}
    }

    ModalSheet(
        visible = data != null,
        onDismiss = onDismiss,
        cancelable = cancelable,
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
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
 * @param cancelable True if this modal could be closed with swipe gesture, tap on scrim or tap on
 * hardware back button.
 * @param shape The shape of the bottom sheet.
 * @param elevation The elevation of the bottom sheet.
 * @param backgroundColor The background color of the bottom sheet.
 * @param contentColor The preferred content color provided by the bottom sheet to its
 * children. Defaults to the matching content color for [backgroundColor], or if that is not
 * a color from the theme, this will keep the same content color set above the bottom sheet.
 * @param scrimColor The color of the scrim that is applied to the rest of the screen when the
 * bottom sheet is visible. If the color passed is [Color.Unspecified], then a scrim will no
 * longer be applied and the bottom sheet will not block interaction with the rest of the screen
 * when visible.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun ModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    cancelable: Boolean = true,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipHalfExpanded = true,
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            // Intercept and disallow hide gesture / action
            if (it == ModalBottomSheetValue.Hidden && cancelable) {
                onDismiss()
            }
            false
        }
    )

    // Remove modal from hierarchy completely when it is not needed
    if (!visible && !sheetState.isAnimationRunning && !sheetState.isVisible) {
        return
    }

    LaunchedEffect(visible) {
        if (visible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalSheet(
        sheetState = sheetState,
        onDismiss = {
            if (cancelable) {
                onDismiss()
            }
        },
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
        content = content,
    )
}

@ExperimentalSheetApi
@Composable
private fun ModalSheet(
    sheetState: ModalBottomSheetState,
    onDismiss: () -> Unit,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    content: @Composable ColumnScope.() -> Unit,
) {
    FullScreenPopup(
        onDismiss = onDismiss,
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetShape = shape,
            sheetElevation = elevation,
            sheetBackgroundColor = backgroundColor,
            sheetContentColor = contentColor,
            scrimColor = scrimColor,
            sheetContent = content,
            content = { /* Empty */ }
        )
    }
}

/**
 * Contains the default values used by [ModalSheet].
 */
@ExperimentalSheetApi
public object ModalSheetDefaults {

    /**
     * Default shape of the bottom sheet.
     */
    public val shape: Shape
        @Composable
        get() = MaterialTheme.shapes.large

    /**
     * Default elevation of the bottom sheet.
     */
    public val elevation: Dp
        @Composable
        get() = ModalBottomSheetDefaults.Elevation

    /**
     * Default background color of the bottom sheet.
     */
    public val backgroundColor: Color
        @Composable
        get() = MaterialTheme.colors.surface

    /**
     * Default color of the scrim that is applied to the rest of the screen when the bottom sheet
     * is visible.
     */
    public val scrimColor: Color
        @Composable
        get() = ModalBottomSheetDefaults.scrimColor
}
