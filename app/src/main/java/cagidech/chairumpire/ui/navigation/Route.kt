package cagidech.chairumpire.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Clients : Route

    @Serializable
    data object Orders : Route

    @Serializable
    data object Profile : Route

}