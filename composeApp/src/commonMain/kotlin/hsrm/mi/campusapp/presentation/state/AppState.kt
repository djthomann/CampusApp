package hsrm.mi.campusapp.presentation.state

import androidx.compose.runtime.mutableStateOf
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.settings.AppSettings

object AppState {

    private val settings: AppSettings = AppSettings()

    private var selectedCampusName = mutableStateOf<String>(settings.campus)
    var selectedCampus: Campus? = null
        get() = CampusRepository.getCampusByName(selectedCampusName.value)
        private set

    var isDarkMode = mutableStateOf<Boolean>(settings.darkMode)
        private set

    fun toggleDarkMode() {
        val newModeValue = !isDarkMode.value
        isDarkMode.value = newModeValue
        settings.darkMode = newModeValue
    }

    fun selectCampus(campus: Campus?) {
        selectedCampusName.value = campus?.name ?: ""
        selectedCampus = campus
        settings.campus = campus?.name ?: ""
    }

}