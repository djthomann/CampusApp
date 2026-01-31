package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import hsrm.mi.campusapp.presentation.tabs.DepartureTab
import hsrm.mi.campusapp.presentation.tabs.FoodTab
import hsrm.mi.campusapp.presentation.tabs.HomeTab
import hsrm.mi.campusapp.presentation.tabs.MapTab
import hsrm.mi.campusapp.presentation.tabs.ScheduleTab
import hsrm.mi.campusapp.presentation.tabs.SettingsTab
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun MainScaffold(navigator: TabNavigator) {

    Scaffold(
        topBar = {
            if(AppState.selectedCampus != null || navigator.current == SettingsTab) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    title = { Text(stringResource(Res.string.app_name)) },
                    actions = {
                        IconButton(onClick = {
                            navigator.current = SettingsTab
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Open Settings",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        },
        content = {
                padding ->
            Box(modifier = Modifier.padding(padding)) {
                // AnimatedTabContent(navigator)
                CurrentTab()
            }
        },
        bottomBar = {
            if(AppState.selectedCampus != null || navigator.current == SettingsTab) {
                NavigationBar() {
                    NavItem(FoodTab)
                    NavItem(ScheduleTab)
                    NavItem(HomeTab)
                    NavItem(DepartureTab)
                    NavItem(MapTab)
                }
            }
        }
    )
}

@Composable
private fun RowScope.NavItem(tab: CampusTab) {

    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current == tab

    NavigationBarItem(
        selected = selected,

        onClick = { tabNavigator.current = tab},
        icon = { Icon(imageVector = if(selected) tab.activeIcon else tab.inactiveIcon, contentDescription = tab.options.title) },
    )
}