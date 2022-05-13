@file:OptIn(ExperimentalSheetApi::class)

package eu.wewox.modalsheet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wewox.modalsheet.Example
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

/**
 * Showcases the custom [ModalSheet], which has custom background, shape, scrim and can be dismissed only with button.
 */
@Composable
fun CustomModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.CustomModalSheet.label) }
    ) { padding ->
        var visible by rememberSaveable { mutableStateOf(false) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Button(onClick = { visible = true }) {
                Text(text = "Show modal sheet")
            }
        }

        CustomModalSheet(
            visible = visible,
            onDismiss = { visible = false }
        )
    }
}

@Composable
private fun CustomModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    ModalSheet(
        visible = visible,
        cancelable = false,
        shape = createWaveShape(),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.secondary,
        scrimColor = MaterialTheme.colors.primary.copy(alpha = 0.65f),
        onDismiss = { /* NoOp */ }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(SpacingMedium)
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(24.dp)
                    .size(96.dp)
            )
            Text(
                text = "Feel free to modify modal's shape, background color, scrim and interaction flags.",
            )
            Button(
                onClick = onDismiss,
                elevation = ButtonDefaults.elevation(0.dp, 0.dp),
            ) {
                Text(text = "Close modal sheet")
            }
        }
    }
}

private fun createWaveShape(multiplier: Int = 8) = GenericShape { size, _ ->
    moveTo(0f, 40f * multiplier)
    lineTo(size.width / 2f - 60f * multiplier, 40f * multiplier)
    quadraticBezierTo(
        size.width / 2f - 40f * multiplier, 40f * multiplier,
        size.width / 2f - 20f * multiplier, 20f * multiplier
    )
    quadraticBezierTo(size.width / 2f, 0f, size.width / 2f + 20f * multiplier, 20f * multiplier)
    quadraticBezierTo(
        size.width / 2f + 40f * multiplier, 40f * multiplier,
        size.width / 2f + 60f * multiplier, 40f * multiplier
    )
    lineTo(size.width, 40f * multiplier)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
}
