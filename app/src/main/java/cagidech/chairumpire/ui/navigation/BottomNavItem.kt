package cagidech.chairumpire.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val icon: ImageVector,
    val title: String
)

val TOP_LEVEL_DESTINATIONS = mapOf(
    Route.Clients to BottomNavItem(Icons.Default.People, "Clients"),
    Route.Orders to BottomNavItem(Icons.AutoMirrored.Default.ListAlt, "Orders"),
    Route.Profile to BottomNavItem(Icons.Default.Person, "Profile")
)