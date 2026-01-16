package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Start
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.repository.CourseRepository
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.Padding
import kotlin.time.ExperimentalTime

class CalendarScreenModel: ScreenModel {

    @OptIn(ExperimentalTime::class)
    val currentDate: LocalDate = LocalDate.now()
    var selection by mutableStateOf(currentDate)

}

class CalendarScreen: CampusScreen {

    override val title = "Stundenplan"

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { CalendarScreenModel() }


        val state = rememberWeekCalendarState(
            startDate = screenModel.currentDate.minusDays(100),
            endDate = screenModel.currentDate.plusDays(100),
            firstVisibleWeekDate = screenModel.currentDate,
            firstDayOfWeek = DayOfWeek.MONDAY
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 10.dp, 10.dp, 0.dp)
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                DayOfWeek.entries.forEach { dayOfWeek ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(0.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(0.dp),
                            text = dayOfWeek.toSingleLetter(),
                            style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            WeekCalendar(
                modifier = Modifier,
                state = state,
                contentPadding = PaddingValues(10.dp),
                dayContent = { day ->
                    Day(day.date, isSelected = screenModel.selection == day.date) { clicked ->
                        if (screenModel.selection != clicked) {
                            screenModel.selection = clicked
                        }
                    }
                }
            )
            Schedule(screenModel.selection)
        }
    }
}

private val dateFormatter by lazy {
    LocalDate.Format {
        this@Format.day(padding = Padding.ZERO)
    }
}

fun DayOfWeek.toShortString(): String = when (this) {
    DayOfWeek.MONDAY -> "Mo"
    DayOfWeek.TUESDAY -> "Di"
    DayOfWeek.WEDNESDAY -> "Mi"
    DayOfWeek.THURSDAY -> "Do"
    DayOfWeek.FRIDAY -> "Fr"
    DayOfWeek.SATURDAY -> "Sa"
    DayOfWeek.SUNDAY -> "So"
}

fun DayOfWeek.toSingleLetter(): String = when (this) {
    DayOfWeek.MONDAY -> "M"
    DayOfWeek.TUESDAY -> "D"
    DayOfWeek.WEDNESDAY -> "M"
    DayOfWeek.THURSDAY -> "D"
    DayOfWeek.FRIDAY -> "F"
    DayOfWeek.SATURDAY -> "S"
    DayOfWeek.SUNDAY -> "S"
}

@Composable
private fun Schedule(selection: LocalDate) {

    val courses: List<Course> = CourseRepository.getCoursesForDayOfWeek(selection.dayOfWeek)

    println("COURSES: $courses")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (courses.isEmpty()) {
            Text("No Courses today :)", style = MaterialTheme.typography.headlineMedium)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) { items(courses) {
                    course -> CourseEntry(course)
            } }
        }
    }


}

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .aspectRatio(1f)
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .aspectRatio(1f)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dateFormatter.format(date),
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
            )

        }
        /* if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color.Black)
                    .align(Alignment.BottomCenter),
            )
        } */
    }
}
@Composable
private fun CourseEntry(course: Course) {
    Row {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().background(color = course.courseType.color).height(8.dp)
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = course.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = course.lecturer, style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 12.dp, 0.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = course.courseType.icon,
                            contentDescription = "Course Type Icon"
                        )
                        Text(course.courseType.germanString, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(course.room, style = MaterialTheme.typography.bodyMedium)
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "Location Icon"
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 12.dp, 0.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Start,
                            contentDescription = "Course starts at"
                        )
                        Text(course.start.toString(), style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text("${course.durationInMinutes} min", style = MaterialTheme.typography.bodyMedium)
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = "Duration Icon"
                        )
                    }
                }
            }
        }

    }
}
