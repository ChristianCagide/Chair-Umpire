package cagidech.chairumpire.reusableUi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState


@Composable
fun SingleButtonGroup(isMemberState: MutableState<Boolean>) {
    ButtonGroup(
        overflowIndicator = {
            Text("...")
        }
    ) {
        this.toggleableItem(
            checked = !isMemberState.value,
            label = "No",
            onCheckedChange = { isMemberState.value = false },
            icon = {
                Icon(
                    Icons.Default.Check,
                    contentDescription = ""
                ).takeIf { isMemberState.value }
            }
        )
        this.toggleableItem(
            checked = isMemberState.value,
            label = "Yes",
            onCheckedChange = { isMemberState.value = true },
            icon = {
                Icon(
                    Icons.Default.Check,
                    contentDescription = ""
                ).takeIf { isMemberState.value }
            }
        )
    }

}