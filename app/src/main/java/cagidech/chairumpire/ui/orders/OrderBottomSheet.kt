package cagidech.chairumpire.ui.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import cagidech.chairumpire.R
import cagidech.chairumpire.reusableUi.CustomDropDownMenuDatePicker
import cagidech.chairumpire.reusableUi.CustomDropDownMenuEnum
import cagidech.chairumpire.reusableUi.CustomDropDownMenuList
import cagidech.chairumpire.reusableUi.CustomDropDownMenuTimePicker
import cagidech.chairumpire.domain.model.Client
import cagidech.chairumpire.domain.model.Order
import cagidech.chairumpire.ui.model.Services
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderBottomSheet(
    viewModel: OrdersViewModel = hiltViewModel(),
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        var serviceSelected by rememberSaveable {
            mutableStateOf(Services.STRINGING)
        }
        var dateSelected by rememberSaveable {
            mutableLongStateOf(0L)
        }
        val timePickerState = rememberTimePickerState()
        val racketTypeState = rememberTextFieldState()
        val stringState = rememberTextFieldState()
        val tensionState = rememberTextFieldState()

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
            CustomDropDownMenuEnum(
                label = stringResource(R.string.service),
                icon = {
                    Icon(
                        when (serviceSelected) {
                            Services.STRINGING -> Icons.Default.Cable
                            Services.TENNIS_CLINIC,
                            Services.TENNIS_PRIVATE -> Icons.Default.SportsTennis

                            Services.PICKLEBALL_CLINIC,
                            Services.PICKLEBALL_SOCIAL,
                            Services.PICKLEBALL_PRIVATE -> Icons.Default.SportsCricket
                        }, contentDescription = ""
                    )
                },
                menuItems = Services.entries,
                onItemClick = {
                    serviceSelected = it
                }
            )

            if (serviceSelected == Services.STRINGING) {
                CustomDropDownMenuList(
                    label = "Client",
                    menuItems = listOf(),
                    icon = { Icon(Icons.Default.Person, contentDescription = "") },
                    onItemClick = {

                    }
                )
                OutlinedTextField(
                    modifier = Modifier.padding(top = 4.dp),
                    state = racketTypeState,
                    label = { Text("Racket Type") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
                OutlinedTextField(
                    modifier = Modifier.padding(top = 4.dp),
                    state = stringState,
                    label = { Text("String") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
                OutlinedTextField(
                    modifier = Modifier.padding(top = 4.dp),
                    state = tensionState,
                    label = { Text("Tension") },
                    trailingIcon = { Text("lb") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine
                )
                CustomDropDownMenuDatePicker(
                    label = "Drop Off Date",
                    onDateSelected = {
                        dateSelected = it
                    }
                )
            } else {
                CustomDropDownMenuDatePicker(
                    label = "Date",
                    onDateSelected = {
                        dateSelected = it
                    }
                )
                CustomDropDownMenuTimePicker(
                    label = "Time",
                    state = timePickerState
                )
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp),
            onClick = {
                viewModel.handleIntent(
                    OrdersIntent.AddOrder(
                        Order(
                            client = Client(
                                name = "Christian",
                                phone = "1234567890",
                                isMember = true
                            ),
                            racketType = racketTypeState.text.toString(),
                            stringType = stringState.text.toString(),
                            tension = tensionState.text.toString(),
                            dropOffDateTime = dateSelected.toString(),
                            pickUpDateTime = dateSelected.toString(),
                            stringerName = "Cagide",
                            dateBilled = dateSelected.toString()
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