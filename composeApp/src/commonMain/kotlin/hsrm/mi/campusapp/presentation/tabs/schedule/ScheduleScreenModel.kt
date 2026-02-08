package hsrm.mi.campusapp.presentation.tabs.schedule

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.service.CourseService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class ScheduleScreenModel: ScreenModel {

    @OptIn(ExperimentalTime::class)
    val currentDate: LocalDate = LocalDate.now()
    var selectedDay = MutableStateFlow(currentDate)

    @OptIn(ExperimentalCoroutinesApi::class)
    val todaysCourses: StateFlow<List<Course>> = selectedDay
        .flatMapLatest { date ->
            CourseService.getCoursesForDayOfWeek(date.dayOfWeek)
        }
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setDay(day: LocalDate) {
        selectedDay.value = day
    }

}