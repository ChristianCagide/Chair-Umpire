package cagidech.chairumpire.reusableUi

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cagidech.chairumpire.ui.model.MenuItem
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T> CustomDropDownMenuEnum(
    modifier: Modifier = Modifier,
    label: String,
    noinline icon: @Composable (() -> Unit)? = null,
    menuItems: EnumEntries<T>,
    crossinline onItemClick: (newSelection: T) -> Unit
) where T : Enum<T>, T : MenuItem {

    val textFieldState = rememberTextFieldState(stringResource(menuItems.first().stringResourceId))
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            state = textFieldState,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isExpanded)
            },
            leadingIcon = icon,
            label = { Text(label) },
            readOnly = true
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            menuItems.forEach {
                val text = stringResource(it.stringResourceId)
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onItemClick(it)
                        textFieldState.setTextAndPlaceCursorAtEnd(text)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}