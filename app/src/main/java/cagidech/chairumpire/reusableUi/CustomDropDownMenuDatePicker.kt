package cagidech.chairumpire.reusableUi

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenuDatePicker(
    label: String,
    onDateSelected: (Long) -> Unit
) {

    val datePickerState = rememberDatePickerState()
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
            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = "") },
            label = { Text(label) },
            readOnly = true
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            DatePickerDialog(
                onDismissRequest = {},
                confirmButton = {
                    Button(
                        onClick = {
                            val dateMillis = datePickerState.selectedDateMillis ?: 0L
                            val formattedDate = SimpleDateFormat(
                                "MMM dd, yyyy",
                                Locale.getDefault()
                            ).format(Date(dateMillis))
                            onDateSelected(dateMillis)
                            textFieldState.setTextAndPlaceCursorAtEnd(formattedDate)
                            isExpanded = false
                        }
                    ) {
                        Text("Confirm")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}