package hsrm.mi.campusapp.presentation.tabs.home

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.openmeteo.OpenMeteoAPI
import hsrm.mi.campusapp.data.api.openmeteo.toDomain
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.toDomain
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.model.Trip
import hsrm.mi.campusapp.domain.model.Weather
import hsrm.mi.campusapp.domain.service.CourseService
import hsrm.mi.campusapp.domain.service.MenuService
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.maplibre.spatialk.geojson.Position
import kotlin.time.ExperimentalTime

class HomeScreenModel: ScreenModel {

    val currentWeather = mutableStateOf<Weather?>(null)


    val isLoadingArrivalTrip = mutableStateOf(false)
    val arrivalTrip = mutableStateOf<Trip?>(null)

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    val todaysMeal: StateFlow<Menu?> = AppState.selectedCanteen
        .flatMapLatest { canteen ->
            println("FlatMapLatest triggered für Canteen: ${canteen?.name}")

            if (canteen == null) {
                flowOf(null)
            } else {
                val today = LocalDate.now()
                MenuService.getMenuForDayAndCanteen(today, canteen)
            }
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    @OptIn(ExperimentalTime::class)
    val todaysCourses: StateFlow<List<Course>> = CourseService.getCoursesForDayOfWeek(LocalDate.now().dayOfWeek)
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val earliestCourse: StateFlow<Course?> = todaysCourses
        .map { courses ->
            courses.minByOrNull { it.start }
        }
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateCampus(campus: Campus?) {
        AppState.updateCampus(campus, screenModelScope)
    }

    fun loadWeather(campus: Campus) {
        screenModelScope.launch {
            /* Tried loading API in IO Thread. Was bad for performance though
            val result = withContext(Dispatchers.IO) {
                OpenMeteoAPI.getCurrentWeather(campus)
            }
            currentWeather.value = result?.toDomain(campus)*/

            currentWeather.value = OpenMeteoAPI.getCurrentWeather(campus)?.toDomain(campus)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun loadArrivalTrip(stopId: String, course: Course) {

        val stop = Stop(
            id = stopId,
            name = "Placeholder",
            position = Position(longitude = 0.0, latitude = 0.0),
            campus = null
        )

        screenModelScope.launch {
            isLoadingArrivalTrip.value = true
            val trips: List<Trip> = RmvAPI.getArrivalTripFromStopToCampus(stop, course.building.latitude, course.building.longitude, LocalDateTime(date = LocalDate.now(), course.start)).map { it.toDomain() }            // Filter trip with latest startTime
            val tripsOnTime = trips.filter { trip -> trip.arrivalTime <= course.start }

            // Filter display trip
            arrivalTrip.value = if(tripsOnTime.isNotEmpty()) tripsOnTime.first() else null

            // println("TRIP: ${arrivalTrip.value}")
            isLoadingArrivalTrip.value = false
        }
    }

}