package cagidech.chairumpire.ui.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cagidech.chairumpire.domain.DataError
import cagidech.chairumpire.domain.Result
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.domain.usecases.client.DeleteClientUseCase
import cagidech.chairumpire.domain.usecases.client.GetClientsUseCase
import cagidech.chairumpire.domain.usecases.client.UpsertClientUseCase
import cagidech.chairumpire.ui.clients.ClientEvent.*
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
class ClientsViewModel @Inject constructor(
    val upsertClientUseCase: UpsertClientUseCase,
    val getClientsUseCase: GetClientsUseCase,
    val deleteClientUseCase: DeleteClientUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientState())
    val uiState: StateFlow<ClientState> = _uiState.asStateFlow()

    private val eventChannel = Channel<ClientEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        onAction(ClientAction.LoadClients)
    }

    fun onAction(action: ClientAction) {
        when (action) {
            is ClientAction.LoadClients -> loadClients()

            is ClientAction.AddClient -> upsertClient(action.client)

            ClientAction.DismissBottomSheet -> {
                val currentState = _uiState.value
                if (!currentState.isLoading) {
                    _uiState.update {
                        currentState.copy(isBottomSheetExpanded = false)
                    }
                }
            }

            ClientAction.OpenBottomSheet -> {
                val currentState = _uiState.value
                if (!currentState.isLoading) {
                    _uiState.update {
                        currentState.copy(isBottomSheetExpanded = true)
                    }
                }
            }

            is ClientAction.RefreshClients -> {

            }

            is ClientAction.UpdateSearchQuery -> {
                val currentState = _uiState.value
                if (!currentState.isLoading) {
                    _uiState.update { currentState.copy(searchQuery = action.query) }
                }
            }

            is ClientAction.DeleteClient -> deleteClient(action.client)
        }
    }

    private fun loadClients() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getClientsUseCase().collect { clients ->
                _uiState.update { it.copy(isLoading = false, clients = clients) }
            }
        }
    }

    private fun upsertClient(client: Client) {
        viewModelScope.launch {
            when (val result = upsertClientUseCase(client)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Local.DISK_FULL -> {
                            eventChannel.send(OnError("Disk Full"))
                        }

                        DataError.Local.UNKNOWN -> {
                            eventChannel.send(OnError("Unknown error"))
                        }
                    }
                }

                is Result.Success -> {
                }
            }
        }
    }

    private fun deleteClient(client: Client) {
        viewModelScope.launch {
            when (val result = deleteClientUseCase(client)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Local.DISK_FULL -> {
                            eventChannel.send(OnError("Disk Full"))
                        }

                        DataError.Local.UNKNOWN -> {
                            eventChannel.send(OnError("Unknown error"))
                        }
                    }
                }

                is Result.Success -> {
                }
            }
        }
    }

}