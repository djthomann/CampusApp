package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import hsrm.mi.campusapp.presentation.tabs.DepartureTab
import hsrm.mi.campusapp.presentation.tabs.FoodTab
import hsrm.mi.campusapp.presentation.tabs.HomeTab
import hsrm.mi.campusapp.presentation.tabs.MapTab
import hsrm.mi.campusapp.presentation.tabs.ScheduleTab

@Composable
actual fun MainScaffold(navigator: TabNavigator) {
    Scaffold { padding ->
        Row(Modifier.fillMaxSize().padding(padding)) {
            NavigationRail {
                NavItem(HomeTab)
                NavItem(DepartureTab)
                NavItem(ScheduleTab)
                NavItem(FoodTab)
                NavItem(MapTab)
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