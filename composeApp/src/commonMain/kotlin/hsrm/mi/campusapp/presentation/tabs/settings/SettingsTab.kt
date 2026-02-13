package hsrm.mi.campusapp.presentation.tabs.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.settings
import hsrm.mi.campusapp.domain.service.IBuildingService
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.domain.service.ICanteenService
import hsrm.mi.campusapp.domain.service.IMenuService
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import hsrm.mi.campusapp.presentation.tabs.home.HomeTab
import hsrm.mi.campusapp.presentation.tabs.schedule.ScheduleTab
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

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

        // Put in Koin Module!
        val appState = koinInject<AppState>()
        val canteenService = koinInject<ICanteenService>()
        val campusService = koinInject<ICampusService>()
        val menuService = koinInject<IMenuService>()
        val buildingService = koinInject<IBuildingService>()
        val screenModel = rememberScreenModel { SettingsScreenModel(appState, canteenService, campusService, menuService, buildingService) }
        val tabNavigator = LocalTabNavigator.current

        val canteens by screenModel.canteens.collectAsStateWithLifecycle()
        val campuses by screenModel.campuses.collectAsStateWithLifecycle()

        val selectedStop by appState.homeStop.collectAsState()

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
                IconButton(
                    onClick = {
                        screenModel.clearDatabase()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ClearAll,
                        contentDescription = "Clear Database"
                    )
                }
                Switch(
                    modifier = Modifier.testTag("darkModeSwitch"),
                    checked = appState.isDarkMode.value,
                    onCheckedChange = { _ -> appState.toggleDarkMode() },
                    thumbContent = {
                        if(appState.isDarkMode.value)
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
                CampusSelection(campuses = campuses, Modifier.weight(1f), updateCampus =  screenModel::updateCampus)
                IconButton(onClick = { screenModel.updateCampus(null) }) {
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
                CanteenSelection(
                    canteens = canteens,
                    modifier = Modifier.weight(1f),
                    updateCanteen = screenModel::updateCanteen
                    )
                IconButton(onClick = { screenModel.updateCanteen(null) }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Canteen"
                    )
                }
            }

            if(selectedStop != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.Flag,
                        contentDescription = "Home Stop Icon"
                    )
                    Text(style = MaterialTheme.typography.bodyMedium, text = selectedStop!!.name)
                    IconButton(onClick = { screenModel.updateHomeStop(null) }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear Home Stop"
                        )
                    }
                }

            } else {
                HomeStopSearchBar(
                    results = screenModel.homeStopResults.value,
                    onSearch = screenModel::searchHomeStopByName,
                    selectHomeStop = screenModel::updateHomeStop
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