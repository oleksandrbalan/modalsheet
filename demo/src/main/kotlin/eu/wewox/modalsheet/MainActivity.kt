package eu.wewox.modalsheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import eu.wewox.modalsheet.screens.BackHandlerInModalSheetScreen
import eu.wewox.modalsheet.screens.CustomModalSheetScreen
import eu.wewox.modalsheet.screens.DynamicModalSheetScreen
import eu.wewox.modalsheet.screens.ModalSheetPaddingScreen
import eu.wewox.modalsheet.screens.ScrollableModalSheetScreen
import eu.wewox.modalsheet.screens.SheetAboveBottomBarScreen
import eu.wewox.modalsheet.screens.SheetStateModalSheetScreen
import eu.wewox.modalsheet.screens.SimpleModalSheetScreen
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.ModalSheetDemoTheme
import eu.wewox.modalsheet.ui.theme.SpacingMedium

/**
 * Main activity for demo application.
 * Contains simple "Crossfade" based navigation to various examples.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ModalSheetDemoTheme {
                var example by rememberSaveable { mutableStateOf<Example?>(null) }

                BackHandler(enabled = example != null) {
                    example = null
                }

                Crossfade(targetState = example) { selected ->
                    when (selected) {
                        null -> RootScreen(onExampleClick = { example = it })
                        Example.SimpleModalSheet -> SimpleModalSheetScreen()
                        Example.SheetAboveBottomBar -> SheetAboveBottomBarScreen()
                        Example.DynamicModalSheet -> DynamicModalSheetScreen()
                        Example.ScrollableModalSheet -> ScrollableModalSheetScreen()
                        Example.CustomModalSheet -> CustomModalSheetScreen()
                        Example.SheetStateModalSheet -> SheetStateModalSheetScreen()
                        Example.ModalSheetPadding -> ModalSheetPaddingScreen()
                        Example.BackHandlerInModalSheet -> BackHandlerInModalSheetScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun RootScreen(onExampleClick: (Example) -> Unit) {
    Scaffold(
        topBar = { TopBar("Modal Sheet Demo") }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(Example.values()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExampleClick(it) }
                        .padding(SpacingMedium)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = it.label,
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.body2
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
