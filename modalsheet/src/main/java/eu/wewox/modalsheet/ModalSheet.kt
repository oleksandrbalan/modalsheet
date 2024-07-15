package eu.wewox.modalsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 *
 * @param data Value that determines the content of the sheet. Sheet is closed (or remains closed) when null is passed.
 * @param onDataChange Called when data changes as a result of the visibility change.
 * @param cancelable When true, this modal sheet can be closed with swipe gesture, tap on scrim or tap on system back
 * button. Note: passing 'false' does not disable the interaction with the sheet. Only the resulting state after the
 * sheet settles.
 * @param onSystemBack The action invoked by pressing the system back button. By default sets the data to 'null' if
 * [cancelable] is set to true. Could be overridden or set to 'null' to react on system back button press.
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
 * @param sheetPadding The padding to apply on the whole modal sheet. Could be used to draw above (on y axis) system UI.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun <T> ModalSheet(
    data: T?,
    onDataChange: (T?) -> Unit,
    cancelable: Boolean = true,
    onSystemBack: (() -> Unit)? = { onDataChange(null) },
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    sheetPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.(T) -> Unit,
) {
    var lastNonNullData by remember { mutableStateOf(data) }
    DisposableEffect(data) {
        if (data != null) lastNonNullData = data
        onDispose {}
    }

    ModalSheet(
        visible = data != null,
        onVisibleChange = { visible ->
            if (visible) {
                onDataChange(lastNonNullData)
            } else {
                onDataChange(null)
            }
        },
        onSystemBack = onSystemBack,
        cancelable = cancelable,
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
        sheetPadding = sheetPadding,
    ) {
        lastNonNullData?.let {
            content(it)
        }
    }
}

/**
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 * Should be used on with the content which is not dependent on the outer data. For dynamic content use [ModalSheet]
 * overload with a 'data' parameter.
 *
 * @param visible True if modal should be visible.
 * @param onVisibleChange Called when visibility changes.
 * @param cancelable When true, this modal sheet can be closed with swipe gesture, tap on scrim or tap on system back
 * button. Note: passing 'false' does not disable the interaction with the sheet. Only the resulting state after the
 * sheet settles.
 * @param onSystemBack The action invoked by pressing the system back button. By default sets the visibility to false
 * if [cancelable] is set to true. Could be overridden or set to 'null' to react on system back button press.
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
 * @param sheetPadding The padding to apply on the whole modal sheet. Could be used to draw above (on y axis) system UI.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun ModalSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    cancelable: Boolean = true,
    onSystemBack: (() -> Unit)? = { onVisibleChange(false) },
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    sheetPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    // Hold cancelable flag internally and set to true when modal sheet is dismissed via "visible" property in
    // non-cancellable modal sheet. This ensures that "confirmValueChange" will return true when sheet is set to hidden
    // state.
    val internalCancelable = remember { mutableStateOf(cancelable) }
    val sheetState = rememberModalBottomSheetState(
        skipHalfExpanded = true,
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            // Intercept and disallow hide gesture / action
            if (it == ModalBottomSheetValue.Hidden && !internalCancelable.value) {
                return@rememberModalBottomSheetState false
            }
            true
        },
    )

    LaunchedEffect(visible, cancelable) {
        if (visible) {
            internalCancelable.value = cancelable
            sheetState.show()
        } else {
            internalCancelable.value = true
            sheetState.hide()
        }
    }

    LaunchedEffect(sheetState.currentValue, sheetState.targetValue, sheetState.progress) {
        if (sheetState.progress == 1f && sheetState.currentValue == sheetState.targetValue) {
            val newVisible = sheetState.isVisible
            if (newVisible != visible) {
                onVisibleChange(newVisible)
            }
        }
    }

    if (!visible && sheetState.currentValue == sheetState.targetValue && !sheetState.isVisible) {
        return
    }

    ModalSheet(
        sheetState = sheetState,
        onSystemBack = if (onSystemBack != null) { { if (cancelable) onSystemBack() } } else { null },
        shape = shape,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        scrimColor = scrimColor,
        sheetPadding = sheetPadding,
        content = content,
    )
}

/**
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 * Takes [ModalBottomSheetState] as parameter to fine-tune sheet behavior.
 *
 * Note: In this case [ModalSheet] is always added to the composition. See [ModalSheet] overload with visible parameter,
 * or data object to conditionally add / remove modal sheet to / from the composition.
 *
 * @param sheetState The state of the underlying Material bottom sheet.
 * @param onSystemBack The action invoked by pressing the system back button.
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
 * @param sheetPadding The padding to apply on the whole modal sheet. Could be used to draw above (on y axis) system UI.
 * @param content The content of the bottom sheet.
 */
@ExperimentalSheetApi
@Composable
public fun ModalSheet(
    sheetState: ModalBottomSheetState,
    onSystemBack: (() -> Unit)?,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    sheetPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    FullscreenPopup(
        onSystemBack = onSystemBack,
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetShape = shape,
            sheetElevation = elevation,
            sheetBackgroundColor = backgroundColor,
            sheetContentColor = contentColor,
            scrimColor = scrimColor,
            sheetContent = content,
            content = { /* Empty */ },
            modifier = Modifier
                .padding(sheetPadding)
                .clipToBounds(),
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
