package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.navigator.tab.TabNavigator
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.next_course
import campusapp.composeapp.generated.resources.no_courses_today
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.tabs.schedule.ScheduleTab
import org.jetbrains.compose.resources.stringResource


@Composable
fun ScheduleInfo(nextCourse: Course?, tabNavigator: TabNavigator) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = "Next course"
            )
            Text(stringResource(Res.string.next_course))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(nextCourse == null) {
                Text(stringResource(Res.string.no_courses_today), style = MaterialTheme.typography.bodyMedium)
            } else {
                CampusButton(
                    text = nextCourse.name,
                    onClick = {
                        tabNavigator.current = ScheduleTab
                    },
                    isActive = true
                )
            }
        }
    }
}