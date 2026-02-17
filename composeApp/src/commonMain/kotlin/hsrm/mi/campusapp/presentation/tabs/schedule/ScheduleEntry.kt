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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import hsrm.mi.campusapp.domain.model.Building
import hsrm.mi.campusapp.presentation.tabs.map.MapTab

@Composable
fun ScheduleEntry(
    name: String,
    subtitle: String?,
    entryType: String,
    entryTypeIcon: ImageVector,
    start: String,
    duration: Int,
    highlightColor: Color,
    building: Building,
    room: String
) {

    val tabNavigator = LocalTabNavigator.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                // .shadow(10.dp, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .width(900.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .clickable {
                    MapTab.focusBuilding(building)
                    tabNavigator.current = MapTab
                }
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().background(color = highlightColor).height(8.dp)
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = name, style = MaterialTheme.typography.bodyLarge)
                if(subtitle != null) Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 12.dp, 0.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = entryTypeIcon,
                            contentDescription = "Schedule Entry Icon"
                        )
                        Text(entryType, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(room, style = MaterialTheme.typography.bodyMedium)
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
                        Text(start, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text("$duration min", style = MaterialTheme.typography.bodyMedium)
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = "Duration Icon"
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
                            imageVector = Icons.Rounded.Apartment,
                            contentDescription = "Course Building"
                        )
                        Text(building.name, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

    }
}