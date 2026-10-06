package cagidech.chairumpire.ui.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.domain.model.Order
import cagidech.chairumpire.reusableUi.ObserveAsEvent
import kotlinx.coroutines.launch

@Composable
fun OrdersRoute() {
    val ordersViewModel = hiltViewModel<OrdersViewModel>()
    val ordersState by ordersViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState = remember {
        SnackbarHostState()
    }

    ObserveAsEvent(ordersViewModel.events) { event ->
        when (event) {
            is OrderEvent.OnError -> snackbarState.showSnackbar(event.message)
        }
    }

    OrdersScreen(
        uiState = ordersState,
        handleIntent = ordersViewModel::handleIntent,
        snackbarState = snackbarState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    uiState: OrdersUiState,
    handleIntent: (OrdersIntent) -> Unit,
    snackbarState: SnackbarHostState
) {
    val orderBottomSheetState = rememberBottomSheetState(SheetValue.Hidden)
    val scope = rememberCoroutineScope()

    fun hideSheet() {
        scope.launch {
            orderBottomSheetState.hide()
        }.invokeOnCompletion {
            if (!orderBottomSheetState.isVisible) {
                handleIntent(
                    OrdersIntent.DismissBottomSheet
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    handleIntent(
                        OrdersIntent.OpenBottomSheet
                    )
                }
            ) {
                Icon(Icons.Filled.Add, "Add order")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is OrdersUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is OrdersUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                is OrdersUiState.Success -> {
                    if (uiState.isBottomSheetExpanded) {
                        OrderBottomSheet(
                            sheetState = orderBottomSheetState,
                            onDismissRequest = {
                                hideSheet()
                            }
                        )
                    }
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Search Bar
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = {
                                handleIntent(
                                    OrdersIntent.UpdateSearchQuery(
                                        it
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Search by customer name...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = {
                                        handleIntent(
                                            OrdersIntent.UpdateSearchQuery(
                                                ""
                                            )
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Search"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )

                        if (uiState.orders.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No matching orders found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.orders, key = { it.id }) { order ->
                                    OrderCard(order = order)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.client.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (order.client.isMember) Color.Green
                    else Color.Red
                ) {
                    Text(
                        text = if (order.client.isMember) "Member" else "Non-Member",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = if (order.client.isMember) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            OrderInfoRow(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = order.client.phone
            )
            OrderInfoRow(
                icon = Icons.Default.SportsTennis,
                label = "Racket",
                value = order.racketType
            )
            OrderInfoRow(
                icon = Icons.Default.Timeline,
                label = "String",
                value = order.stringType
            )
            OrderInfoRow(
                icon = Icons.Default.Tune,
                label = "Tension",
                value = order.tension
            )
            OrderInfoRow(
                icon = Icons.AutoMirrored.Default.Login,
                label = "Drop-off",
                value = order.dropOffDateTime
            )
            OrderInfoRow(
                icon = Icons.AutoMirrored.Default.Logout,
                label = "Pick-up",
                value = order.pickUpDateTime
            )
            OrderInfoRow(
                icon = Icons.Default.Person,
                label = "Stringer",
                value = order.stringerName
            )
            OrderInfoRow(
                icon = Icons.Default.Receipt,
                label = "Billed Date",
                value = order.dateBilled
            )
        }
    }
}

@Composable
fun OrderInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}