package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import hsrm.mi.campusapp.presentation.state.AppState

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

    @Composable
    override fun Content() {

        val currentCampus = AppState.selectedCampus

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Campus: ${currentCampus?.name ?: "None"}")
            Button(onClick = {AppState.selectCampus(null)}) {
                Text("Clear Campus")
            }
            Button(
                onClick = {
                    AppState.toggleDarkMode()
                }
            ) {
                Text("Toggle Dark Mode")
            }
        }
    }
}