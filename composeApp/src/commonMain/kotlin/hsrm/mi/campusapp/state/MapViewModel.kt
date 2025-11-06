package hsrm.mi.campusapp.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hsrm.mi.campusapp.repository.CampusRepository
import hsrm.mi.campusapp.repository.StopRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.maplibre.compose.camera.CameraPosition

class MapViewModel: ViewModel() {

    var uiState by mutableStateOf(
        MapState(
            cameraPosition = CameraPosition(
                target = CampusRepository.selectedCampus.center,
                zoom = 16.0,
                tilt = 45.0,
                bearing = 0.0
            )
        )
    )
        private set

    init {

        val campusFlow = snapshotFlow { CampusRepository.selectedCampus }
            .distinctUntilChanged()
            .onEach { campus ->
                println("Campus flow: $campus")
                val cameraPosition = CameraPosition(
                    target = campus.center,
                    zoom = 16.0,
                    tilt = 45.0,
                    bearing = 0.0
                )
                uiState = uiState.copy(
                    cameraPosition = cameraPosition
                )
            }
            .launchIn(viewModelScope)


        val stopFlow = snapshotFlow { StopRepository.selectedStop }
            .distinctUntilChanged()
            .onEach { stop ->
                println("Stop flow: $stop")
                val cameraPosition = CameraPosition(
                    target = stop.position,
                    zoom = 16.0,
                    tilt = 45.0,
                    bearing = 0.0
                )
                uiState = uiState.copy(
                    cameraPosition = cameraPosition
                )
            }
            .launchIn(viewModelScope)

    }

}