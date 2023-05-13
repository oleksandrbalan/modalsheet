@file:OptIn(ExperimentalSheetApi::class, ExperimentalAnimationApi::class)

package eu.wewox.modalsheet.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
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
 * Example of modal sheet with overridden system back button behavior.
 */
@Composable
fun BackHandlerInModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.BackHandlerInModalSheet.label) }
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

        ThreeStatesModalSheet(
            visible = visible,
            onVisibleChange = { visible = it }
        )
    }
}

@Composable
private fun ThreeStatesModalSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = onVisibleChange,
        onSystemBack = null,
    ) {
        var state by remember { mutableStateOf(0) }

        AnimatedContent(targetState = state) { currentState ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SpacingSmall),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(SpacingMedium)
            ) {
                Text(
                    text = "State #${currentState + 1}",
                    style = MaterialTheme.typography.h4
                )

                Text(
                    text = "Switch between states with next and prev buttons. Also system back button returns to the " +
                        "previous state or dismisses a modal sheet.",
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(SpacingSmall)
                ) {
                    if (currentState > 0) {
                        Button(onClick = { state -= 1 }) {
                            Text(text = "Prev")
                        }
                    }
                    if (currentState < 2) {
                        Button(onClick = { state += 1 }) {
                            Text(text = "Next")
                        }
                    }
                }
            }
        }

        BackHandler(visible) {
            if (state == 0) {
                onVisibleChange(false)
            } else {
                state -= 1
            }
        }
    }
}
