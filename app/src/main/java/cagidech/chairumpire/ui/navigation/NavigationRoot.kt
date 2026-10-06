package cagidech.chairumpire.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import cagidech.chairumpire.ui.clients.ClientsRoute
import cagidech.chairumpire.ui.main.NavigationBar
import cagidech.chairumpire.ui.profile.ProfileRoute
import cagidech.chairumpire.ui.orders.OrdersRoute

@Composable
fun NavigationRoot() {
    val navigationState = rememberNavigationState(
        startRoute = Route.Orders,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                selectedNavKey = navigationState.topLevelRoute,
                onSelectKey = {
                    navigator.navigate(it)
                },
                modifier = Modifier
            )
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            entries = navigationState.toEntries(
                entryProvider = entryProvider {
                    entry<Route.Clients> {
                        ClientsRoute()
                    }
                    entry<Route.Orders> {
                        OrdersRoute()
                    }
                    entry<Route.Profile> {
                        ProfileRoute()
                    }
                }
            ),
            onBack = navigator::goBack
        )
    }
}