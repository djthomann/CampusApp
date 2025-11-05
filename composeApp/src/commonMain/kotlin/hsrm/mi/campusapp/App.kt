package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hsrm.mi.campusapp.screen.CalendarScreen
import hsrm.mi.campusapp.screen.HomeScreen
import hsrm.mi.campusapp.screen.MapScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun App() {

    MaterialTheme {
        Navigator(HomeScreen()) {
            navigator ->
            Scaffold(
                topBar = { TopAppBar(title = { Text("Meine App") }) },
                content = {
                        padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        CurrentScreen()
                    }
                },
                bottomBar = {
                    NavigationBar {

                        val navigator = LocalNavigator.currentOrThrow

                        val isCalendarSelected = navigator.lastItem is CalendarScreen
                        val isHomeSelected = navigator.lastItem is HomeScreen
                        val isMapSelected = navigator.lastItem is MapScreen

                        NavigationBarItem(
                            selected = isCalendarSelected,
                            onClick = { navigator.push(CalendarScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isCalendarSelected) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                                    contentDescription = "Calendar"
                                )
                            },
                            )
                        NavigationBarItem(
                            selected =isHomeSelected,
                            onClick = { navigator.push(HomeScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isHomeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            },

                            )
                        NavigationBarItem(
                            selected = isMapSelected,
                            onClick = { navigator.push(MapScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isMapSelected) Icons.Filled.Map else Icons.Outlined.Map,
                                    contentDescription = "Map"
                                )
                            },

                            )
                    }
                }
            )
        }
    }
}