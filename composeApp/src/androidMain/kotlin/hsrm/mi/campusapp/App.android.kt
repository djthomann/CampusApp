package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import hsrm.mi.campusapp.presentation.tabs.DepartureTab
import hsrm.mi.campusapp.presentation.tabs.FoodTab
import hsrm.mi.campusapp.presentation.tabs.HomeTab
import hsrm.mi.campusapp.presentation.tabs.MapTab
import hsrm.mi.campusapp.presentation.tabs.ScheduleTab
import hsrm.mi.campusapp.presentation.theme.NavigationBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun MainScaffold(navigator: TabNavigator) {
    Scaffold(
        topBar = {
            val title = (navigator.current as? CampusTab)?.topAppBarTitle
            if (!title.isNullOrEmpty()) {
                TopAppBar(
                    title = { Text(title) }
                )
            }
        },
        content = {
                padding ->
            Box(modifier = Modifier.padding(padding)) {
                AnimatedTabContent(navigator)
            }
        },
        bottomBar = {
            NavigationBar() {
                NavItem(FoodTab)
                NavItem(ScheduleTab)
                NavItem(HomeTab)
                NavItem(DepartureTab)
                NavItem(MapTab)
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
        colors = NavigationBarItemColors
    )
}