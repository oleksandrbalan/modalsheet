package eu.wewox.modalsheet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import eu.wewox.modalsheet.Example
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

@Composable
fun ScrollableModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.ScrollableModalSheet.label) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(SpacingMedium)
        ) {
            var scrollableVisible by rememberSaveable { mutableStateOf(false) }
            var scrollableWithFixedVisible by rememberSaveable { mutableStateOf(false) }

            Text(
                text = "Note: LazyColumn and LazyVerticalGrid are not supported for now.",
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Button(onClick = { scrollableVisible = true }) {
                    Text(text = "Show modal sheet #1")
                }

                Button(onClick = { scrollableWithFixedVisible = true }) {
                    Text(text = "Show modal sheet #2")
                }
            }

            ScrollableModalSheet(
                visible = scrollableVisible,
                onDismiss = { scrollableVisible = false }
            )

            ScrollableWithFixedPartsModalSheet(
                visible = scrollableWithFixedVisible,
                onDismiss = { scrollableWithFixedVisible = false }
            )
        }
    }
}

@Composable
private fun ScrollableModalSheet(
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
                .verticalScroll(scrollState)
                .fillMaxWidth()
                .padding(SpacingMedium)
                .systemBarsPadding()
        ) {
            Text(
                text = "This Scrolls",
                style = MaterialTheme.typography.h4
            )
            repeat(100) {
                Text(
                    text = "Item #$it",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ScrollableWithFixedPartsModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    ModalSheet(
        visible = visible,
        onDismiss = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .systemBarsPadding()
                    .padding(top = 56.dp, bottom = 64.dp, start = SpacingMedium, end = SpacingMedium)
            ) {
                repeat(100) {
                    Text(
                        text = "Item #$it",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Text(
                text = "Fixed Header",
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(SpacingMedium)
                    .statusBarsPadding()
            )
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(SpacingMedium)
                    .navigationBarsPadding()
            ) {
                Text(text = "Fixed Button")
            }
        }
    }
}
