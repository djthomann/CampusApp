package hsrm.mi.campusapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {


    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {

                    var selectedItem by remember { mutableIntStateOf(1) }

                    NavigationBarItem(
                        selected = selectedItem == 0,
                        onClick = { selectedItem = 0 },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == 0) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                                contentDescription = "Calendar"
                            )
                        },

                    )
                    NavigationBarItem(
                        selected = selectedItem == 1,
                        onClick = { selectedItem = 1 },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == 1) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home"
                            )
                        },

                    )
                    NavigationBarItem(
                        selected = selectedItem == 2,
                        onClick = { selectedItem = 2 },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == 2) Icons.Filled.Map else Icons.Outlined.Map,
                                contentDescription = "Map"
                            )
                        },

                        )
                }
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {

            }
        }
    }
}