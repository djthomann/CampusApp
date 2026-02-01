package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Start
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.friday_single_letter
import campusapp.composeapp.generated.resources.monday_single_letter
import campusapp.composeapp.generated.resources.no_courses_today
import campusapp.composeapp.generated.resources.saturday_single_letter
import campusapp.composeapp.generated.resources.schedule_tab_title
import campusapp.composeapp.generated.resources.sunday_single_letter
import campusapp.composeapp.generated.resources.thursday_single_letter
import campusapp.composeapp.generated.resources.tuesday_single_letter
import campusapp.composeapp.generated.resources.wednesday_single_letter
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusDays
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.repository.CourseRepository
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.Padding
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

class ScheduleScreenModel: ScreenModel {

    @OptIn(ExperimentalTime::class)
    val currentDate: LocalDate = LocalDate.now()
    var selection by mutableStateOf(currentDate)

}

object ScheduleTab: CampusTab {
    private fun readResolve(): Any = ScheduleTab

    override val topAppBarTitle: String = runBlocking { getString(Res.string.schedule_tab_title) }
    override val activeIcon: ImageVector = Icons.Filled.CalendarMonth
    override val inactiveIcon: ImageVector = Icons.Outlined.CalendarMonth

    override val options: TabOptions
        @Composable
        get() {
            val title = topAppBarTitle
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { ScheduleScreenModel() }

        val state = rememberWeekCalendarState(
            startDate = screenModel.currentDate.minusDays(100),
            endDate = screenModel.currentDate.plusDays(100),
            firstVisibleWeekDate = screenModel.currentDate,
            firstDayOfWeek = DayOfWeek.MONDAY
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeekCalendar(
                modifier = Modifier.width(600.dp),
                state = state,
                contentPadding = PaddingValues(10.dp),
                weekHeader = { week ->
                    Row {
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
                },
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

@Composable
fun DayOfWeek.toSingleLetter(): String = when (this) {
    DayOfWeek.MONDAY -> stringResource(Res.string.monday_single_letter)
    DayOfWeek.TUESDAY -> stringResource(Res.string.tuesday_single_letter)
    DayOfWeek.WEDNESDAY -> stringResource(Res.string.wednesday_single_letter)
    DayOfWeek.THURSDAY -> stringResource(Res.string.thursday_single_letter)
    DayOfWeek.FRIDAY -> stringResource(Res.string.friday_single_letter)
    DayOfWeek.SATURDAY -> stringResource(Res.string.saturday_single_letter)
    DayOfWeek.SUNDAY -> stringResource(Res.string.sunday_single_letter)
}

@Composable
private fun Schedule(selection: LocalDate) {

    val courses: List<Course> = CourseRepository.getCoursesForDayOfWeek(selection.dayOfWeek)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (courses.isEmpty()) {
            EmptyIndicator()
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
private fun EmptyIndicator() {

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/lottie/no-courses.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(composition)

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = "Lottie animation"
    )

    Text(stringResource(Res.string.no_courses_today), style = MaterialTheme.typography.headlineMedium)

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
                color = if(isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
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

    val tabNavigator = LocalTabNavigator.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(900.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .clickable {
                    MapTab.moveToPosition(course.building)
                    tabNavigator.current = MapTab
                }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().background(color = course.courseType.color).height(8.dp)
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = course.name, style = MaterialTheme.typography.bodyLarge)
                if(course.lecturer != null) Text(text = course.lecturer, style = MaterialTheme.typography.bodyMedium)
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
                        Text(stringResource(course.courseType.nameResource), style = MaterialTheme.typography.bodyMedium)
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
