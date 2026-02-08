package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ModeOfTravel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.arrive_on_time
import hsrm.mi.campusapp.domain.model.Trip
import hsrm.mi.campusapp.presentation.state.AppState
import org.jetbrains.compose.resources.stringResource


@Composable
fun ArrivalInfo(trip: Trip?, isLoading: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ModeOfTravel,
            contentDescription = "arrive on time"
        )
        Text(stringResource(Res.string.arrive_on_time))
        Spacer(modifier = Modifier.width(12.dp))
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO() Make string resources
        if(AppState.homeStopId != null) {
            if(trip != null) {
                Column {
                    Text(style = MaterialTheme.typography.bodyMedium, text = "Von ${trip.startTime} Bis ${trip.arrivalTime}")
                    trip.legs.filter { leg -> leg.name != "Fußweg" }.forEach { leg ->
                        Text(style = MaterialTheme.typography.bodyMedium, text = "• ${leg.name}: ${leg.origin} → ${leg.destination} ")
                    }
                }
            } else {
                if(isLoading) {
                    Text(text = "Lade Verbindungen...", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text(text = "Keine Verbindung gefunden...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            Text(text = "No home stop selected...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
