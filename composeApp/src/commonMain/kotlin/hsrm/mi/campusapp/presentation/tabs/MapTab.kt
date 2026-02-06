package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.map_tab_title
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.state.MapState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position

class MapScreenModel: ScreenModel {

    val defaultCenter = Position(0.0, 0.0)

    val selectedCampus = AppState.selectedCampus

    var uiState by mutableStateOf(
        MapState(
            cameraPosition = CameraPosition(
                target = selectedCampus.value?.center ?: defaultCenter,
                zoom = 16.0,
                tilt = 45.0,
                bearing = 0.0
            )
        )
    )
        private set

    init {
        screenModelScope.launch {
            snapshotFlow { AppState.selectedCampus }
                .collect { campus ->
                    campus.let {
                        uiState = uiState.copy(
                            cameraPosition = uiState.cameraPosition.copy(target = it.value?.center ?: defaultCenter)
                        )
                    }
                }
        }
    }

    fun updateTarget(position: Position) {
        uiState = uiState.copy(
            cameraPosition = uiState.cameraPosition.copy(target = position, zoom = 18.0)
        )
    }
}
object MapTab: CampusTab {
    private fun readResolve(): Any = MapTab

    override val topAppBarTitle =  runBlocking { getString(Res.string.map_tab_title) }
    override val activeIcon: ImageVector = Icons.Filled.Map
    override val inactiveIcon: ImageVector = Icons.Outlined.Map

    private val pendingPosition = mutableStateOf<Position?>(null)

    public fun moveToPosition(position: Position) {
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
        val screenModel = rememberScreenModel { MapScreenModel() }

        LaunchedEffect(pendingPosition.value) {
            pendingPosition.value?.let { position ->
                screenModel.updateTarget(position)
                pendingPosition.value = null // Zurücksetzen, nachdem geladen wurde
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapView(screenModel.uiState)
        }
    }
}

@Composable
expect fun MapView(state: MapState)