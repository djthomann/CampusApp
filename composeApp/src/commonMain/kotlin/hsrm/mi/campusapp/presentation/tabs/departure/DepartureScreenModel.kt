package hsrm.mi.campusapp.presentation.tabs.departure

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.toDomain
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Stop
import kotlinx.coroutines.launch

class DepartureScreenModel: ScreenModel {

    var isLoadingDepartures by mutableStateOf<Boolean>(false)
    val currentStop = mutableStateOf<Stop?>(null)

    val departures = mutableStateOf<List<Departure>>(emptyList())

    fun loadDepartures(stop: Stop) {
        departures.value = emptyList()
        isLoadingDepartures = true
        currentStop.value = stop
        screenModelScope.launch {

            departures.value = RmvAPI.getNextDepartures(stop).map { it.toDomain() }

            departures.value.forEach { departure ->
                launch {
                    departure.journey = RmvAPI.getJourneyDetails(departure.journeyDetailRef).toDomain()
                }
            }
            isLoadingDepartures = false
        }
    }
}