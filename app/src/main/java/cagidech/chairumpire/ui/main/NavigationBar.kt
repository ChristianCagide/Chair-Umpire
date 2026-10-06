package cagidech.chairumpire.ui.main

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cagidech.chairumpire.ui.navigation.TOP_LEVEL_DESTINATIONS

@Composable
fun NavigationBar(
    selectedNavKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { (navKey, data) ->
            NavigationBarItem(
                selected = navKey == selectedNavKey,
                onClick = {
                    onSelectKey(navKey)
                },
                icon = {
                    Icon(data.icon, data.title)
                },
                label = { Text(data.title) }
            )
        }
    }

}