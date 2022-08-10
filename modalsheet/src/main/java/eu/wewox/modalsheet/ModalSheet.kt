package eu.wewox.modalsheet

import androidx.annotation.FloatRange
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableState
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
 * @param data Value that determines the content of the sheet. Sheet is closed (or remains closed) when null is passed.
 * @param onDataChange Called when data changes as a result of the visibility change.
 * @param cancelable When true, this modal sheet can be closed with swipe gesture, tap on scrim or tap on hardware back
 * button. Note: passing 'false' does not disable the interaction with the sheet. Only the resulting state after the
 * sheet settles.
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
    onDataChange: (T?) -> Unit,
    cancelable: Boolean = true,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    content: @Composable ModalSheetScope.(T) -> Unit,
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
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 * Should be used on with the content which is not dependent on the outer data. For dynamic content use [ModalSheet]
 * overload with a 'data' parameter.
 *
 * @param visible True if modal should be visible.
 * @param onVisibleChange Called when visibility changes.
 * @param cancelable When true, this modal sheet can be closed with swipe gesture, tap on scrim or tap on hardware back
 * button. Note: passing 'false' does not disable the interaction with the sheet. Only the resulting state after the
 * sheet settles.
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
    onVisibleChange: (Boolean) -> Unit,
    cancelable: Boolean = true,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    content: @Composable ModalSheetScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipHalfExpanded = true,
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            // Intercept and disallow hide gesture / action
            if (it == ModalBottomSheetValue.Hidden && !cancelable) {
                return@rememberModalBottomSheetState false
            }

            onVisibleChange(it == ModalBottomSheetValue.Expanded)

            true
        }
    )

    if (!visible && sheetState.progress.to == sheetState.progress.from && !sheetState.isVisible) {
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
                onVisibleChange(false)
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

/**
 * Modal sheet that behaves like bottom sheet and draws over system UI.
 * Takes [ModalBottomSheetState] as parameter to fine-tune sheet behavior.
 *
 * Note: In this case [ModalSheet] is always added to the composition. See [ModalSheet] overload with visible parameter,
 * or data object to conditionally add / remove modal sheet to / from the composition.
 *
 * @param sheetState The state of the underlying Material bottom sheet.
 * @param onDismiss Called when user taps on the hardware back button.
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
    sheetState: ModalBottomSheetState,
    onDismiss: (() -> Unit)?,
    shape: Shape = ModalSheetDefaults.shape,
    elevation: Dp = ModalSheetDefaults.elevation,
    backgroundColor: Color = ModalSheetDefaults.backgroundColor,
    contentColor: Color = contentColorFor(backgroundColor),
    scrimColor: Color = ModalSheetDefaults.scrimColor,
    content: @Composable ModalSheetScope.() -> Unit,
) {
    FullscreenPopup(
        onDismiss = onDismiss,
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetShape = shape,
            sheetElevation = elevation,
            sheetBackgroundColor = backgroundColor,
            sheetContentColor = contentColor,
            scrimColor = scrimColor,
            sheetContent = {
                ModalSheetScopeImpl(sheetState).content()
            },
            content = { /* Empty */ }
        )
    }
}

/**
 * The scope for the modal sheet content.
 */
@ExperimentalSheetApi
public interface ModalSheetScope {

    /**
     * Normalized fraction from 0.0 to 1.0, where 0.0 is fully collapsed and 1.0 is fully expanded.
     */
    public val normalizedFraction: Float
        @FloatRange(from = 0.0, to = 1.0)
        get() = with(swipeState) {
            when {
                progress.from == ModalBottomSheetValue.Expanded && progress.to == ModalBottomSheetValue.Expanded ->
                    1f
                progress.from == ModalBottomSheetValue.Hidden && progress.to == ModalBottomSheetValue.Hidden ->
                    1f
                direction <= 0 ->
                    progress.fraction
                else ->
                    1f - progress.fraction
            }
        }

    /**
     * The swipeable state to read values from.
     */
    public val swipeState: SwipeableState<ModalBottomSheetValue>
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

@ExperimentalSheetApi
private class ModalSheetScopeImpl(override val swipeState: SwipeableState<ModalBottomSheetValue>) : ModalSheetScope
