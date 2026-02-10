package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.state.MapState
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.spatialk.geojson.Position

class MapScreenModel(
    private val appState: AppState
): ScreenModel {

    val defaultCenter = Position(0.0, 0.0)

    val selectedCampus = appState.selectedCampus

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
            snapshotFlow { appState.selectedCampus }
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