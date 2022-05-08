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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
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
 * Showcases the use-case when modal sheet content is dynamic.
 * For example an error modal which should be shown when error is not null.
 */
@Composable
fun DynamicModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.DynamicModalSheet.label) }
    ) { padding ->
        var error by rememberSaveable(saver = ErrorDataSaver) { mutableStateOf(null) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Button(onClick = {
                error = ErrorData(
                    title = "Error",
                    message = LoremIpsum
                )
            }) {
                Text(text = "Make an error")
            }
        }

        ErrorModalSheet(
            error = error,
            onDismiss = { error = null }
        )
    }
}

@Composable
private fun ErrorModalSheet(
    error: ErrorData?,
    onDismiss: () -> Unit,
) {
    ModalSheet(
        data = error,
        onDismiss = onDismiss
    ) { data ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(SpacingMedium)
        ) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.h4
            )
            Text(
                text = data.message,
            )
        }
    }
}

private data class ErrorData(val title: String, val message: String)

private val ErrorDataSaver: Saver<MutableState<ErrorData?>, Any> = listSaver(
    save = { listOf(it.value?.title, it.value?.message) },
    restore = {
        val title = it[0]
        val message = it[1]
        mutableStateOf(if (title != null && message != null) ErrorData(title, message) else null)
    }
)

private val LoremIpsum = """
    Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Mauris dictum facilisis augue. 
    Vivamus porttitor turpis ac leo. Quisque porta. Nulla pulvinar eleifend sem. Maecenas sollicitudin. 
    Morbi imperdiet, mauris ac auctor dictum, nisl ligula egestas nulla, et sollicitudin sem purus in lacus. 
    Phasellus rhoncus. Nulla quis diam. Ut tempus purus at lorem. Vestibulum fermentum tortor id mi.  
    Nullam feugiat, turpis at pulvinar vulputate, erat libero tristique tellus, nec bibendum odio risus sit amet ante.
""".trimIndent()
