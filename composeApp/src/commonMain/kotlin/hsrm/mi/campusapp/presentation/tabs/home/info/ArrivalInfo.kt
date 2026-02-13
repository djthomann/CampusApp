package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeOfTravel
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.arrive_on_time
import hsrm.mi.campusapp.domain.model.Trip
import hsrm.mi.campusapp.presentation.state.AppState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


@Composable
fun ArrivalInfo(trip: Trip?, isLoading: Boolean) {

    val homeStop by koinInject<AppState>().homeStop.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ModeOfTravel,
            contentDescription = "arrive on time",
            modifier = Modifier.size(30.dp)
        )
        Text(stringResource(Res.string.arrive_on_time))
        Spacer(modifier = Modifier.width(12.dp))
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp), thickness = 1.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO() Make string resources
        if(homeStop != null) {

            AnimatedVisibility(
                visible = (trip != null),
                enter = slideInVertically(
                    initialOffsetY = { -it }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it }
                ) + fadeOut()
            ) {
                Column {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = "Von ${trip?.startTime} Bis ${trip?.arrivalTime}"
                    )
                    trip?.legs?.filter { it.name != "Fußweg" }?.forEach { leg ->
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "• ${leg.name}:"
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "\t\tVon: ${leg.origin}"
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "\t\tNach: ${leg.destination}"
                        )
                    }
                }
            }

            if(trip == null) {

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
