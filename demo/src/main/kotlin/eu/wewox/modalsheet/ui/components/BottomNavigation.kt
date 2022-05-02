package eu.wewox.modalsheet.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigation(
    current: BottomNavItem,
    onClick: (BottomNavItem) -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.primary,
        elevation = BottomNavigationDefaults.Elevation,
    ) {
        androidx.compose.material.BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onPrimary,
            elevation = 0.dp,
            modifier = Modifier.navigationBarsPadding(),
        ) {
            BottomNavItem.values().forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(item.imageVector, item.title) },
                    label = { Text(item.title) },
                    selected = current == item,
                    onClick = { onClick(item) },
                )
            }
        }
    }
}

enum class BottomNavItem(var title: String, var imageVector: ImageVector) {
    Home("Home", Icons.Default.Home),
    Counter("Counter", Icons.Default.AddCircle),
    Profile("Profile", Icons.Default.Person),
}
