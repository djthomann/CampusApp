package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.map_tab_title
import hsrm.mi.campusapp.presentation.state.MapState
import hsrm.mi.campusapp.presentation.state.MapViewModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString

class MapScreenModel: ScreenModel {
    val mapViewModel = MapViewModel()
}
object MapTab: CampusTab {
    private fun readResolve(): Any = MapTab

    override val topAppBarTitle =  runBlocking { getString(Res.string.map_tab_title) }
    override val activeIcon: ImageVector = Icons.Filled.Map
    override val inactiveIcon: ImageVector = Icons.Outlined.Map

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
        val screenModel = rememberScreenModel { MapScreenModel() }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapView(screenModel.mapViewModel.uiState)
        }
    }
}

@Composable
expect fun MapView(state: MapState)