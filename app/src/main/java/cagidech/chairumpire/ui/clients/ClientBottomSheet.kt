package cagidech.chairumpire.ui.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import cagidech.chairumpire.reusableUi.SingleButtonGroup
import cagidech.chairumpire.domain.model.Client
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientBottomSheet(
    viewModel: ClientsViewModel = hiltViewModel(),
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        val nameState = rememberTextFieldState()
        val phoneState = rememberTextFieldState()
        val isMemberState = remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedTextField(
                modifier = Modifier.padding(top = 4.dp),
                state = nameState,
                label = { Text("Client Name") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                lineLimits = TextFieldLineLimits.SingleLine
            )
            OutlinedTextField(
                modifier = Modifier.padding(top = 4.dp),
                state = phoneState,
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                inputTransformation = InputTransformation.maxLength(10),
                supportingText = {
                    Text("${phoneState.text.length} / 10")
                }
            )

            Text("Member?", modifier = Modifier.padding(top = 8.dp))

            SingleButtonGroup(isMemberState)
        }

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp),
            onClick = {
                viewModel.onAction(
                    ClientAction.AddClient(
                        Client(
                            id = 0,
                            name = nameState.text.toString(),
                            phone = phoneState.text.toString(),
                            isMember = isMemberState.value,
                        )
                    )
                )
                onDismissRequest()
            }
        ) {
            Text("Add")
        }
    }
}