package eu.wewox.modalsheet.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wewox.modalsheet.ui.theme.SpacingMedium

@Composable
fun TopBar(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h4,
        modifier = Modifier
            .padding(SpacingMedium)
            .statusBarsPadding()
    )
}
