package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import hsrm.mi.campusapp.presentation.tabs.canteen.CanteenTab
import hsrm.mi.campusapp.presentation.tabs.departure.DepartureTab
import hsrm.mi.campusapp.presentation.tabs.home.HomeTab
import hsrm.mi.campusapp.presentation.tabs.map.MapTab
import hsrm.mi.campusapp.presentation.tabs.schedule.ScheduleTab
import hsrm.mi.campusapp.presentation.tabs.settings.SettingsTab

@Composable
actual fun MainScaffold(navigator: TabNavigator) {
    Scaffold { padding ->
        Row(Modifier.fillMaxSize().padding(padding)) {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavItem(HomeTab)
                NavItem(DepartureTab)
                NavItem(ScheduleTab)
                NavItem(CanteenTab)
                NavItem(MapTab)

                Spacer(Modifier.weight(1f))

                NavItem(SettingsTab)
            }
            AnimatedTabContent(navigator)
        }
    }
}

@Composable
fun NavItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationRailItem(
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } }
    )
}