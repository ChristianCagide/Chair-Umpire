package cagidech.chairumpire.ui.orders

import cagidech.chairumpire.domain.model.Order

sealed interface OrdersUiState {
    data object Loading : OrdersUiState
    data class Success(
        val allOrders: MutableList<Order>,
        val searchQuery: String = "",
        val isBottomSheetExpanded: Boolean = false
    ) : OrdersUiState {
        val orders: MutableList<Order>
            get() = (if (searchQuery.isBlank()) {
                allOrders
            } else {
                allOrders.filter { it.client.name.contains(searchQuery, ignoreCase = true) }
            }).toMutableList()
    }

    data class Error(val message: String) : OrdersUiState
}

sealed interface OrdersIntent {
    data object LoadOrders : OrdersIntent
    data object OpenBottomSheet : OrdersIntent
    data object DismissBottomSheet : OrdersIntent
    data class AddOrder(val order: Order) : OrdersIntent
    data class DeleteOrder(val order: Order) : OrdersIntent
    data class RefreshOrders(val newOrders: List<Order>) : OrdersIntent
    data class UpdateSearchQuery(val query: String) : OrdersIntent
}
