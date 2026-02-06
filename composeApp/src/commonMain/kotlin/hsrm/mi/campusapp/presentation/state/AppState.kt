package hsrm.mi.campusapp.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.persistence.CanteenDao
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.toDomain
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.CanteenRepository
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AppState {

    init {

        // Load from settings
        CoroutineScope(Dispatchers.IO).launch {

            val campusName = settings.campus
            if(campusName.isNotBlank()) {
                val campus = DatabaseHolder.db.getCampusDao().getByName(campusName)?.toDomain()
                _selectedCampus.value = campus
            }

            val canteenName = settings.canteen
            if (canteenName.isNotBlank()) {
                val canteen = DatabaseHolder.db.getCanteenDao().getByName(canteenName)?.toDomain()
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