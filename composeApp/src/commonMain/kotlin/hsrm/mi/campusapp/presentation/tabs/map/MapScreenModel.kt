package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import hsrm.mi.campusapp.domain.model.Building
import hsrm.mi.campusapp.presentation.state.AppState
import org.maplibre.spatialk.geojson.Position

class MapScreenModel(
    private val appState: AppState
): ScreenModel {

    val defaultCenter = appState.selectedCampus.value?.center ?: Position(0.0, 0.0)

    var target = mutableStateOf<Building?>(null)

    fun updateTarget(building: Building) {
        target.value = building
    }

    fun clearTarget() {
        target.value = null
    }
}