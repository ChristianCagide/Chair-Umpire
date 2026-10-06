package cagidech.chairumpire.ui.clients

import androidx.compose.runtime.Immutable
import cagidech.chairumpire.domain.model.Client

@Immutable
data class ClientState(
    val isLoading: Boolean = false,
    val clients: List<Client> = emptyList(),
    val searchQuery: String = "",
    val isBottomSheetExpanded: Boolean = false,
)

sealed interface ClientEvent {
    data class OnError(val message: String) : ClientEvent
}

sealed interface ClientAction {
    data object LoadClients : ClientAction
    data object OpenBottomSheet : ClientAction
    data object DismissBottomSheet : ClientAction
    data class AddClient(val client: Client) : ClientAction
    data class RefreshClients(val newClients: List<Client>) : ClientAction
    data class UpdateSearchQuery(val query: String) : ClientAction
    data class DeleteClient(val client: Client) : ClientAction
}