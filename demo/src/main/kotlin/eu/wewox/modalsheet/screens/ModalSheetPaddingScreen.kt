@file:OptIn(ExperimentalSheetApi::class)

package eu.wewox.modalsheet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
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
 * Example of the sheet padding parameter usage.
 */
@Composable
fun ModalSheetPaddingScreen() {
    Scaffold(
        topBar = { TopBar(Example.ModalSheetPadding.label) }
    ) { padding ->
        var visibleSafeContent by rememberSaveable { mutableStateOf(false) }
        var visibleOnlyBottom by rememberSaveable { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Button(onClick = { visibleSafeContent = true }) {
                Text(text = "Show with safe content")
            }
            Button(onClick = { visibleOnlyBottom = true }) {
                Text(text = "Show with only bottom")
            }
        }

        ModalSheetWithPadding(
            visible = visibleSafeContent,
            onVisibleChange = { visibleSafeContent = it },
            sheetPadding = WindowInsets.safeContent.asPaddingValues()
        )

        ModalSheetWithPadding(
            visible = visibleOnlyBottom,
            onVisibleChange = { visibleOnlyBottom = it },
            sheetPadding = WindowInsets.safeContent.only(WindowInsetsSides.Bottom).asPaddingValues()
        )
    }
}

@Composable
private fun ModalSheetWithPadding(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
    sheetPadding: PaddingValues,
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = onVisibleChange,
        sheetPadding = sheetPadding,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingMedium)
        ) {
            Text(
                text = "Sheet padding",
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "M3 modal sheet draws a sheet and scrim above and below (on y axis) system bars. To mimic " +
                    "such behavior it is possible to set sheetPadding, for example WindowInsets.safeContent.",
            )
            Button(onClick = { onVisibleChange(false) }) {
                Text(text = "Close modal sheet")
            }
        }
    }
}
