package hsrm.mi.campusapp.presentation.state

import androidx.compose.runtime.mutableStateOf
import hsrm.mi.campusapp.domain.model.Campus

object AppState {
    var selectedCampus = mutableStateOf<Campus?>(null)
        private set

    fun selectCampus(campus: Campus) {
        selectedCampus.value = campus
    }

}