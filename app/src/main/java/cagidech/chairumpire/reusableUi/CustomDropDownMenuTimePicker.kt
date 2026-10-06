package cagidech.chairumpire.reusableUi

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.isPm
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenuTimePicker(
    label: String,
    state: TimePickerState
) {

    val textFieldState = rememberTextFieldState()
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            state = textFieldState,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isExpanded)
            },
            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = "") },
            label = { Text(label) },
            readOnly = true
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            TimePickerDialog(
                onDismissRequest = {},
                title = { Text(label) },
                confirmButton = {
                    Button(
                        onClick = {
                            val hour = state.hour.toString().padStart(2, '0')
                            val minute = state.minute.toString().padStart(2, '0')
                            val is24 = state.is24hour
                            val isPm = state.isPm
                            val timeString =
                                "$hour:$minute" + if (!is24) if (isPm) " PM" else " AM" else " hrs"
                            textFieldState.setTextAndPlaceCursorAtEnd(timeString)
                            isExpanded = false
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            ) {
                TimePicker(
                    state = state
                )
            }
        }
    }
}