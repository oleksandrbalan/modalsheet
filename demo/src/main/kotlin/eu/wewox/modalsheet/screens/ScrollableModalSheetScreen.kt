@file:OptIn(ExperimentalSheetApi::class)

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
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
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

/**
 * Showcases the scroll support in the [ModalSheet] body.
 */
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
            var verticalScrollVisible by rememberSaveable { mutableStateOf(false) }
            var verticalScrollWithFixedVisible by rememberSaveable { mutableStateOf(false) }
            var lazyColumnVisible by rememberSaveable { mutableStateOf(false) }
            var lazyGridVisible by rememberSaveable { mutableStateOf(false) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Button(onClick = { verticalScrollVisible = true }) {
                    Text(text = "Vertical scroll")
                }

                Button(onClick = { verticalScrollWithFixedVisible = true }) {
                    Text(text = "Vertical scroll with fixed")
                }

                Button(onClick = { lazyColumnVisible = true }) {
                    Text(text = "Lazy list")
                }

                Button(onClick = { lazyGridVisible = true }) {
                    Text(text = "Lazy grid")
                }
            }

            ScrollableModalSheet(
                visible = verticalScrollVisible,
                onVisibleChange = { verticalScrollVisible = it }
            )

            ScrollableWithFixedPartsModalSheet(
                visible = verticalScrollWithFixedVisible,
                onVisibleChange = { verticalScrollWithFixedVisible = it }
            )

            LazyColumnModalSheet(
                visible = lazyColumnVisible,
                onVisibleChange = { lazyColumnVisible = it }
            )

            LazyGridModalSheet(
                visible = lazyGridVisible,
                onVisibleChange = { lazyGridVisible = it }
            )
        }
    }
}

@Composable
private fun ScrollableModalSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = onVisibleChange
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
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
    onVisibleChange: (Boolean) -> Unit,
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = onVisibleChange
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .systemBarsPadding()
                    .padding(
                        top = 56.dp,
                        bottom = 64.dp,
                        start = SpacingMedium,
                        end = SpacingMedium
                    )
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
                onClick = { onVisibleChange(false) },
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

@Composable
private fun LazyColumnModalSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = onVisibleChange
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingMedium)
                .systemBarsPadding()
        ) {
            item {
                Text(
                    text = "This Scrolls",
                    style = MaterialTheme.typography.h4
                )
            }

            items(100) {
                Text(
                    text = "Item #$it",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun LazyGridModalSheet(
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
) {
    ModalSheet(
        visible = visible,
        onVisibleChange = onVisibleChange
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingMedium)
                .systemBarsPadding()
        ) {
            items(100) {
                Text(
                    text = "Item #$it",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
