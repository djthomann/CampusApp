package hsrm.mi.campusapp.presentation.state

import androidx.compose.runtime.mutableStateOf
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.service.CampusService
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object AppState {

    init {

        // Load from settings
        CoroutineScope(Dispatchers.IO).launch {

            val campusName = settings.campus
            if(campusName.isNotBlank()) {
                val campus = CampusService.getCampusByName(campusName)
                _selectedCampus.value = campus
            }

            val canteenName = settings.canteen
            if (canteenName.isNotBlank()) {
                val canteen = CanteenService.getCanteenByName(canteenName)
                _selectedCanteen.value = canteen
            }
        }
    }

    private val settings: AppSettings = AppSettings()

    private var _selectedCampus = MutableStateFlow<Campus?>(null)
    val selectedCampus = _selectedCampus.asStateFlow()

    private val _selectedCanteen = MutableStateFlow<Canteen?>(null)
    val selectedCanteen = _selectedCanteen.asStateFlow()

    private var _homeStopId = mutableStateOf<String>(settings.homeStopId)

    /* TODO() Save real Stop object later and retrieve it here */
    var homeStopId: String? = null
        get() = _homeStopId.value.ifEmpty { null }
        private set


    var isDarkMode = mutableStateOf<Boolean>(settings.isDarkMode)
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

    fun selectHomeStop(stopId: String?) {
        _homeStopId.value = stopId?: ""
        homeStopId = stopId
        settings.homeStopId = stopId?: ""
    }

}