package hsrm.mi.campusapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DepartureBoard
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DepartureBoard
import androidx.compose.material.icons.outlined.Dining
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import hsrm.mi.campusapp.presentation.screens.CalendarScreen
import hsrm.mi.campusapp.presentation.screens.CampusScreen
import hsrm.mi.campusapp.presentation.screens.DepartureScreen
import hsrm.mi.campusapp.presentation.screens.FoodScreen
import hsrm.mi.campusapp.presentation.screens.HomeScreen
import hsrm.mi.campusapp.presentation.screens.MapScreen
import hsrm.mi.campusapp.presentation.state.MapViewModel
import hsrm.mi.campusapp.presentation.theme.AppTypography
import hsrm.mi.campusapp.presentation.theme.DarkColorScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun App() {

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
    ) {
        Navigator(HomeScreen()) {
            navigator ->
            Scaffold(
                topBar = {
                    val title = (navigator.lastItem as? CampusScreen)?.title
                    if (!title.isNullOrEmpty()) {
                        TopAppBar(
                            title = { Text(title) }
                        )
                    }
                },
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
                        val isFoodSelected = navigator.lastItem is FoodScreen
                        val isHomeSelected = navigator.lastItem is HomeScreen
                        val isMapSelected = navigator.lastItem is MapScreen
                        val isDepartureSelected = navigator.lastItem is DepartureScreen

                        val mapViewModel = remember { MapViewModel() }

                        NavigationBarItem(
                            selected = isCalendarSelected,
                            onClick = { navigator.push(CalendarScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isCalendarSelected) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                                    contentDescription = "Calendar"
                                )
                            })
                        NavigationBarItem(
                            selected = isFoodSelected,
                            onClick = { navigator.push(FoodScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isFoodSelected) Icons.Filled.Dining else Icons.Outlined.Dining,
                                    contentDescription = "Calendar"
                                )
                            })
                        NavigationBarItem(
                            selected = isHomeSelected,
                            onClick = { navigator.push(HomeScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isHomeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            })
                        NavigationBarItem(
                            selected = isDepartureSelected,
                            onClick = { navigator.push(DepartureScreen()) },
                            icon = {
                                Icon(
                                    imageVector = if (isDepartureSelected) Icons.Filled.DepartureBoard else Icons.Outlined.DepartureBoard,
                                    contentDescription = "Departures"
                                )
                            })
                        NavigationBarItem(
                            selected = isMapSelected,
                            onClick = { navigator.push(MapScreen(mapViewModel)) },
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