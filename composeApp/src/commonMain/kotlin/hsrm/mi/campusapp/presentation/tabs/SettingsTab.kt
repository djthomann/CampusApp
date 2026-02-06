package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.settings
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.StopLocationDTO
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.CanteenRepository
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

class SettingsScreenModel: ScreenModel {

    var homeStopResults = mutableStateOf(emptyList<StopLocationDTO>())

    fun searchHomeStopByName(input: String) {

        screenModelScope.launch {
            homeStopResults.value = RmvAPI.searchStopByName(input)
        }

    }

}

object SettingsTab: CampusTab {
    private fun readResolve(): Any = ScheduleTab

    override val topAppBarTitle: String = "Settings"
    override val activeIcon: ImageVector = Icons.Filled.MoreVert
    override val inactiveIcon: ImageVector = Icons.Outlined.MoreVert

    override val options: TabOptions
        @Composable
        get() {
            val title = topAppBarTitle
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { SettingsScreenModel() }
        val tabNavigator = LocalTabNavigator.current

        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { tabNavigator.current = HomeTab }) {
                        Icon(
                            imageVector = Icons.Filled.ChevronLeft,
                            contentDescription = "Go Back Home"
                        )
                    }
                    Text(text = stringResource(Res.string.settings))
                }
                Switch(
                    checked = AppState.isDarkMode.value,
                    onCheckedChange = { _ -> AppState.toggleDarkMode() },
                    thumbContent = {
                        if(AppState.isDarkMode.value)
                            Icon(imageVector =  Icons.Filled.DarkMode, contentDescription = "")
                        else
                            Icon(imageVector =  Icons.Filled.LightMode, contentDescription = "")
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CampusSelection(Modifier.weight(1f))
                IconButton(onClick = { AppState.selectCampus(null) }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Canteen"
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CanteenSelection(Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Canteen"
                    )
                }
            }

            if(AppState.homeStopId != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Home Stop:")
                    IconButton(onClick = { AppState.selectHomeStop("") }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear Home Stop"
                        )
                    }
                }
                Text(style = MaterialTheme.typography.bodyMedium, text = AppState.homeStopId!!) // TODO() Improve later with real Stop Object
            } else {
                HomeStopSearchBar(
                    results = screenModel.homeStopResults.value,
                    onSearch = screenModel::searchHomeStopByName
                )
            }



            /* TODO() Implement later */
            /*var value by remember { mutableStateOf("") }
            OutlinedTextField(
                value = value,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } || input.isEmpty()) {
                        value = input
                    }
                },
                label = { Text("Anzahl") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                var text by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("RMV") }
                )
            }*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStopSearchBar(results: List<StopLocationDTO>, onSearch: (String) -> Unit) {
    // Common parameters
    val placeholder = "Search"

    // State management
    var query by remember { mutableStateOf("") }

    // State management
    var expanded by remember { mutableStateOf(false) }

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
                                    AppState.selectHomeStop(result.id)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusSelection(modifier: Modifier = Modifier) {
    val selectedOption = AppState.selectedCampus
    var expanded by remember { mutableStateOf(false) }
    val options = CampusRepository.campuses

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
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        AppState.selectCampus(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanteenSelection(modifier: Modifier = Modifier) {
    val options = CanteenRepository.canteens
    val selectedOption = AppState.selectedCanteen
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption?.name ?: "None",
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
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        AppState.selectCanteen(option)
                        expanded = false
                    }
                )
            }
        }
    }
}