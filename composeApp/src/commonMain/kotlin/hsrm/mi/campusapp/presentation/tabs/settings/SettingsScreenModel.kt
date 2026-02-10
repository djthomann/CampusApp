package hsrm.mi.campusapp.presentation.tabs.settings

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.StopLocationDTO
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.domain.service.ICanteenService
import hsrm.mi.campusapp.domain.service.IMenuService
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val appState: AppState,
    private val canteenService: ICanteenService,
    private val campusService: ICampusService,
    private val menuService: IMenuService
): ScreenModel {

    val canteens: StateFlow<List<Canteen>> = canteenService.getAllCanteens().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val campuses: StateFlow<List<Campus>> = campusService.getAllCampuses().stateIn(
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

    fun updateCanteen(canteen: Canteen?) {
        appState.updateCanteen(canteen, screenModelScope)
    }

    fun updateCampus(campus: Campus?) {
        appState.updateCampus(campus, screenModelScope)
    }

    fun updateHomeStop(stop: Stop?) {
        appState.selectHomeStop(stop, screenModelScope)
    }

    fun clearDatabase() {
        screenModelScope.launch {
            // TODO() Actually clear all Database tables
            menuService.deleteAllMenus()
        }
    }

}