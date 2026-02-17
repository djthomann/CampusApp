package hsrm.mi.campusapp.presentation.tabs.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Start
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.Exam
import hsrm.mi.campusapp.presentation.tabs.map.MapTab
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExamEntry(exam: Exam) {
    ScheduleEntry(
        name = exam.name,
        subtitle = if(exam.studentEnrolled) "Angemeldet" else "Nicht Angemeldet!",
        entryType = "Klausur",
        entryTypeIcon = Icons.Default.Quiz,
        start = exam.time.toString(),
        duration = 90,
        highlightColor = Color.Black,
        building = exam.building,
        room = exam.room
    )
}