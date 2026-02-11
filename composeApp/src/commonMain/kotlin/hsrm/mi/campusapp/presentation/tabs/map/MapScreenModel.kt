package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import hsrm.mi.campusapp.presentation.state.AppState
import org.maplibre.spatialk.geojson.Position

class MapScreenModel(
    private val appState: AppState
): ScreenModel {

    val defaultCenter = appState.selectedCampus.value?.center ?: Position(0.0, 0.0)

    var target = mutableStateOf<Position?>(null)

    fun updateTarget(position: Position) {
        target.value = position
    }

    fun clearTarget() {
        target.value = null
    }
}