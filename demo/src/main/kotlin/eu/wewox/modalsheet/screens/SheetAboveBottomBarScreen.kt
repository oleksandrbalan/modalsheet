package eu.wewox.modalsheet.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
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
import androidx.compose.ui.text.font.FontWeight
import eu.wewox.modalsheet.ModalSheet
import eu.wewox.modalsheet.ui.components.BottomNavItem
import eu.wewox.modalsheet.ui.components.BottomNavigation
import eu.wewox.modalsheet.ui.components.TopBar
import eu.wewox.modalsheet.ui.theme.SpacingMedium
import eu.wewox.modalsheet.ui.theme.SpacingSmall

@Composable
fun SheetAboveBottomBarScreen() {
    var item by remember { mutableStateOf(BottomNavItem.Home) }
    Scaffold(
        bottomBar = {
            BottomNavigation(
                current = item,
                onClick = { item = it },
            )
        }
    ) { padding ->
        Crossfade(targetState = item) { current ->
            Scaffold(
                topBar = { TopBar(current.title) },
                modifier = Modifier.padding(padding)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    when (current) {
                        BottomNavItem.Home -> HomeScreen()
                        BottomNavItem.Counter -> CounterScreen()
                        BottomNavItem.Profile -> ProfileScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen() {
    var visible by rememberSaveable { mutableStateOf(false) }

    Button(onClick = { visible = true }) {
        Text(text = "Show modal sheet")
    }

    SimpleModalSheet(
        title = "Hello from Home",
        visible = visible,
        onDismiss = { visible = !visible }
    )
}

@Composable
private fun CounterScreen() {
    var visible by rememberSaveable { mutableStateOf(false) }
    var counter by rememberSaveable { mutableStateOf(0) }

    Column {
        Counter(
            counter = counter,
            onPlus = { counter++ },
            onMinus = { counter-- },
        )
        Button(onClick = { visible = true }) {
            Text(text = "Show modal sheet")
        }
    }

    ModalSheet(
        visible = visible,
        onDismiss = { visible = false }
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
                text = "Hello from Counter",
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "Modal also can share state with a scope where it is placed. For example a counter value.",
            )
            Counter(
                counter = counter,
                onPlus = { counter++ },
                onMinus = { counter-- },
            )
        }
    }
}

@Composable
private fun ProfileScreen() {
    val statuses = listOf("Not set", "Online", "Offline", "Away", "Busy")

    var visible by rememberSaveable { mutableStateOf(false) }
    var status by rememberSaveable { mutableStateOf(statuses.first()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = status,
            style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
        )
        Button(onClick = { visible = true }) {
            Text(text = "Change status")
        }
    }

    ModalSheet(
        visible = visible,
        onDismiss = { visible = false }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SpacingSmall),
                modifier = Modifier.padding(SpacingMedium)
            ) {
                Text(
                    text = "Hello from Profile",
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = "Modal can access scope states and modify them, thus is it convenient to use it as picker.",
                )
            }
            statuses.forEach {
                Text(
                    text = it,
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            status = it
                            visible = false
                        }
                        .padding(SpacingMedium)
                )
            }
        }
    }
}

@Composable
private fun Counter(
    counter: Int,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpacingSmall)
    ) {
        Button(onClick = onMinus, colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)) {
            Text(text = "-")
        }
        Text(text = counter.toString())
        Button(onClick = onPlus, colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)) {
            Text(text = "+")
        }
    }
}
