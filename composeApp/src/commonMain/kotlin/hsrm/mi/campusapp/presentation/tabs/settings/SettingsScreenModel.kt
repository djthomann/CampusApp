package hsrm.mi.campusapp.presentation.tabs.settings

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.StopLocationDTO
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.service.CampusService
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel: ScreenModel {

    val canteens: StateFlow<List<Canteen>> = CanteenService.getAllCanteens().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val campuses: StateFlow<List<Campus>> = CampusService.getAllCampuses().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var homeStopResults = mutableStateOf(emptyList<StopLocationDTO>())

    fun searchHomeStopByName(input: String) {

        screenModelScope.launch {
            homeStopResults.value = RmvAPI.searchStopByName(input)
        }

    }

    fun updateCanteen(canteen: Canteen) {
        AppState.updateCanteen(canteen, screenModelScope)
    }

    fun updateCampus(campus: Campus?) {
        AppState.updateCampus(campus, screenModelScope)
    }

}