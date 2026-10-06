package cagidech.chairumpire.ui.clients

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.reusableUi.ObserveAsEvent
import kotlinx.coroutines.launch

@Composable
fun ClientsRoute(
    clientsViewModel: ClientsViewModel = hiltViewModel()
) {
    val clientsState by clientsViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState = remember {
        SnackbarHostState()
    }

    ObserveAsEvent(clientsViewModel.events) { event ->
        when (event) {
            is ClientEvent.OnError -> snackbarState.showSnackbar(event.message)
        }
    }

    ClientsScreen(
        uiState = clientsState,
        onAction = clientsViewModel::onAction,
        snackbarState = snackbarState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(
    uiState: ClientState,
    onAction: (ClientAction) -> Unit,
    snackbarState: SnackbarHostState
) {
    val clientBottomSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    fun hideSheet() {
        scope.launch {
            clientBottomSheetState.hide()
        }.invokeOnCompletion {
            if (!clientBottomSheetState.isVisible) {
                onAction(
                    ClientAction.DismissBottomSheet
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
                    onAction(
                        ClientAction.OpenBottomSheet
                    )
                }
            ) {
                Icon(Icons.Filled.Add, "Add client")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularWavyProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (uiState.isBottomSheetExpanded) {
                    ClientBottomSheet(
                        sheetState = clientBottomSheetState,
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
                            onAction(
                                ClientAction.UpdateSearchQuery(
                                    it
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Search by name...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    onAction(
                                        ClientAction.UpdateSearchQuery(
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

                    if (uiState.clients.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No matching clients found",
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
                            items(uiState.clients, key = { it.id }) { client ->
                                ClientCard(client = client, onAction)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientCard(
    client: Client,
    onAction: (ClientAction) -> Unit
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val density = LocalDensity.current

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        isContextMenuVisible = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    }
                )
            }
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = pressOffset.copy(y = pressOffset.y - itemHeight)
        ) {
            DropdownMenuItem(
                onClick = {
                    isContextMenuVisible = false
                    onAction(ClientAction.DeleteClient(client))
                },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Delete")
                        Icon(Icons.Default.Delete, contentDescription = "")
                    }
                }
            )
            DropdownMenuItem(
                onClick = {
                    isContextMenuVisible = false
                },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Edit")
                        Icon(Icons.Default.Edit, contentDescription = "")
                        Icon(Icons.Default.Edit, contentDescription = "")
                    }
                }
            )
        }

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
                    text = client.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (client.isMember) Color.Green
                    else Color.Red
                ) {
                    Text(
                        text = if (client.isMember) "Member" else "Non-Member",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = if (client.isMember) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

            OrderInfoRow(
                icon = Icons.Default.Phone,
                label = "Phone",
                value = client.phone
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
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
