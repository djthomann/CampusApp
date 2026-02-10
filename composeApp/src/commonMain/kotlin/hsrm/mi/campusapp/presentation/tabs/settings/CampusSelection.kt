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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.presentation.state.AppState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusSelection(campuses: List<Campus>, modifier: Modifier = Modifier, updateCampus: (Campus) -> Unit) {
    val selectedOption by koinInject<AppState>().selectedCampus.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption?.name ?: "None",
            onValueChange = { },
            readOnly = true,
            textStyle = MaterialTheme.typography.headlineMedium,
            label = { Text("Campus") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable).fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            campuses.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        updateCampus(option)
                        expanded = false
                    }
                )
            }
        }
    }
}