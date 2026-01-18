package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
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
    Scaffold(

    ) {
        Row {
            NavigationRail {
                NavItem(HomeTab)
                NavItem(DepartureTab)
                NavItem(ScheduleTab)
                NavItem(FoodTab)
                NavItem(MapTab)
            }
            CurrentTab()
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