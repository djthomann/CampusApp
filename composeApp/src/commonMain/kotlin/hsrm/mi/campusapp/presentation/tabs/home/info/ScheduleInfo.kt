package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabNavigator
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.next_course
import campusapp.composeapp.generated.resources.no_courses_today
import hsrm.mi.campusapp.domain.model.Course
import org.jetbrains.compose.resources.stringResource


@Composable
fun ScheduleInfo(nextCourse: Course?, tabNavigator: TabNavigator) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = "Next course",
                modifier = Modifier.size(30.dp)
            )
            Text(stringResource(Res.string.next_course))
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp), thickness = 1.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(nextCourse == null) {
                Text(stringResource(Res.string.no_courses_today), style = MaterialTheme.typography.bodyMedium)
            } else {
                Text(style = MaterialTheme.typography.bodyMedium, text = "${nextCourse.name} um ${nextCourse.start} Uhr")
            }
        }
    }
}