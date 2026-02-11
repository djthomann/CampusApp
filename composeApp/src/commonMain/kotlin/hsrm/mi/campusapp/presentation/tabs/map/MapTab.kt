package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.map_tab_title
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.compose.koinInject
import org.maplibre.spatialk.geojson.Position

object MapTab: CampusTab {
    private fun readResolve(): Any = MapTab

    override val topAppBarTitle =  runBlocking { getString(Res.string.map_tab_title) }
    override val activeIcon: ImageVector = Icons.Filled.Map
    override val inactiveIcon: ImageVector = Icons.Outlined.Map

    private val pendingPosition = mutableStateOf<Position?>(null)

    fun moveToPosition(position: Position) {
        pendingPosition.value = position
    }

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
        val appState = koinInject<AppState>()
        val screenModel = rememberScreenModel { MapScreenModel(appState) }

        LaunchedEffect(pendingPosition.value) {
            pendingPosition.value?.let { position ->
                screenModel.updateTarget(position)
                pendingPosition.value = null // Zurücksetzen, nachdem geladen wurde
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapView(screenModel)
        }
    }
}

