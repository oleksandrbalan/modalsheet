@file:OptIn(ExperimentalSheetApi::class)

package eu.wewox.modalsheet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.wewox.modalsheet.Example
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.R
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

/**
 * Example how progress of modal sheet could be observed.
 */
@Composable
fun ObservableModalSheetScreen() {
    Scaffold(
        topBar = { TopBar(Example.ObservableModalSheet.label) }
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

        ObservableModalSheet(
            visible = visible,
            onDismiss = { visible = false }
        )
    }
}

@Composable
private fun ObservableModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    val collapsedCornerRadius = 100
    var cornerRadius by remember { mutableStateOf(collapsedCornerRadius) }
    ModalSheet(
        visible = visible,
        shape = RoundedCornerShape(topStartPercent = cornerRadius, topEndPercent = cornerRadius),
        backgroundColor = MaterialTheme.colors.primary,
        onDismiss = onDismiss
    ) {
        // Apply fraction only from second half
        val adjustedFraction = ((normalizedFraction - 0.5f) / 0.5f).coerceAtLeast(0f)

        // Calculate top padding based on status bar height
        val topPadding = with(LocalDensity.current) {
            (WindowInsets.statusBars.getTop(this) * adjustedFraction).toDp()
        }

        // Calculate corner radius
        cornerRadius = (collapsedCornerRadius * (1 - adjustedFraction)).toInt()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpacingSmall),
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacingMedium)
                .navigationBarsPadding()
                .alpha(adjustedFraction)
                .padding(top = topPadding)
        ) {
            Text(
                text = "Fraction:",
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "$normalizedFraction",
                style = MaterialTheme.typography.h6
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Logo",
                    modifier = Modifier.size(256.dp),
                )
            }
        }
    }
}
