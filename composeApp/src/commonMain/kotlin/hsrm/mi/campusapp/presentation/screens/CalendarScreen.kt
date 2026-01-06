package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

    override val title = "Calendar"

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
                .fillMaxSize()
                .background(Color.White),
        ) {
            WeekCalendar(
                modifier = Modifier,
                state = state,
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

@Composable
private fun Schedule(selection: LocalDate) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Gray)
    ) {
        Text(selection.dayOfWeek.name)
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
                .clip(RoundedCornerShape(4.dp))
                .aspectRatio(1f)
                .background(if (isSelected) Color.Red else Color.Black)
                ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.dayOfWeek.toShortString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                color = Color.White
            )
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
