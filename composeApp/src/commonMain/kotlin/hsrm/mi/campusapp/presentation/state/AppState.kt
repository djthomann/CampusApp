package hsrm.mi.campusapp.presentation.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.settings.AppSettings
import kotlinx.coroutines.flow.map

object AppState {

    private val settings: AppSettings = AppSettings()

    private var selectedCampusName = mutableStateOf<String>(settings.campus)
    var selectedCampus: Campus? = null
        get() = CampusRepository.getCampusByName(selectedCampusName.value)
        private set

    val selectedCampusFlow = snapshotFlow { selectedCampusName.value }
        .map { name -> CampusRepository.getCampusByName(name) }


    var isDarkMode = mutableStateOf<Boolean>(settings.isDarkMode)
        private set

    fun toggleDarkMode() {
        val newModeValue = !isDarkMode.value
        isDarkMode.value = newModeValue
        settings.isDarkMode = newModeValue
    }

    fun selectCampus(campus: Campus?) {
        selectedCampusName.value = campus?.name ?: ""
        selectedCampus = campus
        settings.campus = campus?.name ?: ""
    }

}