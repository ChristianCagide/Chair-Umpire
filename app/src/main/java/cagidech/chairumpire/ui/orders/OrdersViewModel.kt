package cagidech.chairumpire.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import cagidech.chairumpire.domain.model.Order
import cagidech.chairumpire.domain.usecases.order.DeleteOrderUseCase
import cagidech.chairumpire.domain.usecases.order.GetOrdersUseCase
import cagidech.chairumpire.domain.usecases.order.UpsertOrderUseCase
import cagidech.chairumpire.ui.orders.OrdersUiState.Success
import cagidech.chairumpire.ui.orders.OrdersUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val upsertOrderUseCase: UpsertOrderUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<OrdersUiState>(Loading)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<OrderEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        handleIntent(OrdersIntent.LoadOrders)
    }

    fun handleIntent(intent: OrdersIntent) {
        when (intent) {
            is OrdersIntent.LoadOrders -> loadOrders()
            is OrdersIntent.RefreshOrders -> {
                _uiState.update { Success(allOrders = intent.newOrders.toMutableList()) }
            }

            is OrdersIntent.UpdateSearchQuery -> {
                val currentState = _uiState.value
                if (currentState is Success) {
                    _uiState.update { currentState.copy(searchQuery = intent.query) }
                }
            }

            is OrdersIntent.AddOrder -> {
                upsertOrder(intent.order)
            }

            is OrdersIntent.DeleteOrder -> {
                deleteOrder(intent.order)
            }

            OrdersIntent.DismissBottomSheet -> {
                val currentState = _uiState.value
                if (currentState is Success) {
                    _uiState.update {
                        currentState.copy(isBottomSheetExpanded = false)
                    }
                }
            }

            OrdersIntent.OpenBottomSheet -> {
                val currentState = _uiState.value
                if (currentState is Success) {
                    _uiState.update {
                        currentState.copy(isBottomSheetExpanded = true)
                    }
                }
            }
        }
    }

    private fun loadOrders() {
        _uiState.update { Loading }
        viewModelScope.launch {
            getOrdersUseCase().collect { orders ->
                _uiState.update { Success(allOrders = orders.toMutableList()) }
            }
        }
    }

    private fun upsertOrder(order: Order) {
        viewModelScope.launch {
            when (val result = upsertOrderUseCase(order)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Local.DISK_FULL -> {
                            eventChannel.send(OrderEvent.OnError("Disk Full"))
                        }
                        DataError.Local.UNKNOWN -> {
                            eventChannel.send(OrderEvent.OnError("Unknown error"))
                        }
                    }
                }
                is Result.Success -> {}
            }
        }
    }

    private fun deleteOrder(order: Order) {
        viewModelScope.launch {
            when (val result = deleteOrderUseCase(order)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Local.DISK_FULL -> {
                            eventChannel.send(OrderEvent.OnError("Disk Full"))
                        }
                        DataError.Local.UNKNOWN -> {
                            eventChannel.send(OrderEvent.OnError("Unknown error"))
                        }
                    }
                }
                is Result.Success -> {}
            }
        }
    }
}

sealed interface OrderEvent {
    data class OnError(val message: String) : OrderEvent
}
