package hsrm.mi.campusapp.presentation.tabs.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.presentation.state.AppState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanteenSelection(canteens: List<Canteen>, modifier: Modifier = Modifier, updateCanteen: (Canteen) -> Unit) {
    val appState = koinInject<AppState>()
    val selectedOption = koinInject<AppState>().selectedCanteen
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption.value?.name ?: "None",
            onValueChange = {},
            readOnly = true,
            label = { Text("Mensa") },
            textStyle = MaterialTheme.typography.headlineMedium,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable).fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            canteens.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        updateCanteen(option)
                        expanded = false
                    }
                )
            }
        }
    }
}