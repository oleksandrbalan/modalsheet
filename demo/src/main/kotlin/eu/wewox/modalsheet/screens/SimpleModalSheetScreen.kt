@file:OptIn(ExperimentalSheetApi::class)

package eu.wewox.modalsheet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wewox.modalsheet.Example
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

/**
 * Showcases the most simple usage of the modal sheet composable.
 * Shows the bottom sheet on button click.
 */
@Composable
fun SimpleModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.SimpleModalSheet.label) }
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

        SimpleModalSheet(
            title = "Hello there \uD83D\uDC4B",
            visible = visible,
            onDismiss = { visible = false }
        )
    }
}

/**
 * Simple [ModalSheet] example which contains only texts and button to close bottom sheet.
 *
 * @param title The title to show inside [ModalSheet].
 * @param visible True if modal should be visible.
 * @param onDismiss Called when user touches the scrim or swipes the sheet away.
 */
@Composable
fun SimpleModalSheet(
    title: String,
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    ModalSheet(
        visible = visible,
        onDismiss = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(SpacingMedium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "Swipe down, tap on scrim above, tap on HW back button or use a button below to close modal.",
            )
            Button(onClick = onDismiss) {
                Text(text = "Close modal sheet")
            }
        }
    }
}
