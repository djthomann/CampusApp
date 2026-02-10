package hsrm.mi.campusapp.presentation.tabs.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.data.api.rmv.StopLocationDTO
import hsrm.mi.campusapp.data.api.rmv.toDomain
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.presentation.state.AppState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStopSearchBar(results: List<StopLocationDTO>, onSearch: (String) -> Unit, selectHomeStop: (Stop) -> Unit) {
    // Common parameters
    val placeholder = "Search"

    // State management
    var query by remember { mutableStateOf("") }

    // State management
    var expanded by remember { mutableStateOf(false) }

    val selectedStop by koinInject<AppState>().homeStop.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DockedSearchBar(
            shape = RoundedCornerShape(8.dp),
            inputField = {
                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                    },
                    placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurface) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if(query.isNotBlank()) {
                                onSearch(query)
                                expanded = true
                            }
                        }
                    ),
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    query = ""
                                    expanded = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth(),
            content = {
                LazyColumn {
                    if (results.isNotEmpty() && query.isNotEmpty()) {
                        items(results) { result ->
                            ListItem(
                                modifier = Modifier.clickable {
                                    selectHomeStop(result.toDomain())
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                ),
                                headlineContent = { Text(result.name, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.headlineMedium) },
                            )
                        }
                    }
                }
            }
        )
    }
}