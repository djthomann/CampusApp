package hsrm.mi.campusapp.presentation.state

import androidx.compose.runtime.mutableStateOf
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.domain.service.ICanteenService
import hsrm.mi.campusapp.domain.service.IStopService
import hsrm.mi.campusapp.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppState(
    private val settings: AppSettings,
    private val campusService: ICampusService,
    private val canteenService: ICanteenService,
    private val stopService: IStopService,
    private val appScope: CoroutineScope
) {

    init {
        appScope.launch(Dispatchers.IO) {
            loadInitialData()
        }

    }

    private suspend fun loadInitialData() {
        val campusName = settings.campus
        if (campusName.isNotBlank()) {
            _selectedCampus.value = campusService.getCampusByName(campusName)
        }

        val canteenName = settings.canteen
        if (canteenName.isNotBlank()) {
            _selectedCanteen.value = canteenService.getCanteenByName(canteenName)
        }

        val homeStopId = settings.homeStopId
        if (homeStopId.isNotBlank()) {
            _homeStop.value = stopService.getStopById(homeStopId)
        }
    }

    private var _selectedCampus = MutableStateFlow<Campus?>(null)
    val selectedCampus = _selectedCampus.asStateFlow()

    private val _selectedCanteen = MutableStateFlow<Canteen?>(null)
    val selectedCanteen = _selectedCanteen.asStateFlow()

    private var _homeStop = MutableStateFlow<Stop?>(null)
    val homeStop = _homeStop.asStateFlow()


    var isDarkMode = mutableStateOf(settings.isDarkMode)
        private set

    fun toggleDarkMode() {
        val newModeValue = !isDarkMode.value
        isDarkMode.value = newModeValue
        settings.isDarkMode = newModeValue
    }

    fun updateCampus(campus: Campus?, scope: CoroutineScope) {
        scope.launch {
            settings.campus = campus?.name ?: ""
            _selectedCampus.value = campus
        }
    }

    fun updateCanteen(canteen: Canteen?, scope: CoroutineScope) {
        scope.launch {
            settings.canteen = canteen?.name ?: ""
            _selectedCanteen.value = canteen
        }
    }

    fun selectHomeStop(stop: Stop?, scope: CoroutineScope) {
        scope.launch {
            if(stop != null) {
                stopService.saveStop(stop)
            }
            settings.homeStopId = stop?.id ?: ""
            _homeStop.value = stop
        }
    }

}