package hsrm.mi.campusapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.JourneyDetailRef
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.repository.StopRepository
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

class DepartureScreen(
): CampusScreen {

    override val title = "Abfahrten"

    @Composable
    override fun Content() {

        val currentCampus = AppState.selectedCampus.value

        val stops: List<Stop> = remember(currentCampus) { currentCampus?.let { StopRepository.getStopsForCampusName(currentCampus.name) } ?: emptyList() }
        val coroutineScope = rememberCoroutineScope()
        val departures = remember { mutableStateOf<List<Departure>>(
            listOf(Departure(JourneyDetailRef("id"),"Bus 6", LocalTime(17, 4, 0), direction = "Wiesbaden Hauptbahnhof"),
                Departure(JourneyDetailRef("id"), "Bus 6", LocalTime(17, 14, 0), direction = "Unter den Eichen"))
        ) }

        val currentStop = remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            /* if (currentCampus != null) {
                CampusName(currentCampus)
            } else {
                Text("No campus selected")
            } */
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stops) { stop ->

                    val isActive = stop.name == currentStop.value

                    Button(
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = {
                            coroutineScope.launch {
                                currentStop.value = stop.name
                                departures.value = RmvAPI.getNextArrivals(stop)

                                departures.value.map { departure ->
                                    async {
                                        departure.journey = RmvAPI.getJourneyDetails(departure.ref)
                                        println(departure.journey)
                                    }
                                }

                            }
                            /*StopRepository.selectStop((stop))
                            onStopSelected() */
                        }
                    ) {
                        val text = remember { stop.name }
                        Text(text)
                    }
                }
            }
            Spacer(
                modifier = Modifier.padding(5.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(departures.value) { dep ->
                    DepartureEntry(dep)
                }
            }
            if(departures.value.isEmpty()) {
                Text("Keine Abfahrt in den nächsten ${RmvAPI.SEARCH_TIMEFRAME_MINUTES} Minuten...")
            }
        }
    }
}

@Composable
fun DepartureEntry(departure: Departure) {

    val journey = departure.journey

    var expanded = remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (expanded.value)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surface,
        label = "backgroundColor"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (expanded.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "iconRotation"
    )

   Column(
       modifier = Modifier
           .clip(RoundedCornerShape(8.dp))
           .fillMaxWidth()
           .background(backgroundColor)

   ) {

       Column(
           modifier = Modifier
               .fillMaxWidth()
               .clickable { expanded.value = !expanded.value }
               .padding(12.dp)
       )    {
           Row(
               modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween
           ){
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.spacedBy(6.dp)
               ) {
                   Icon(
                       imageVector = Icons.Rounded.DirectionsBus,
                       contentDescription = "Course Type Icon",
                       modifier = Modifier.size(24.dp)
                   )
                   Text(modifier = Modifier.padding(end = 10.dp), fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.name)
               }
               Text(fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.time.toString())
           }
           AnimatedVisibility(
               visible = expanded.value
           ) {
               Row(
                   modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
               ) {
                   VerticalDivider(color = Color.White, thickness = 2.dp,
                       modifier = Modifier
                           .fillMaxHeight()
                           .padding(start = 11.dp) // Width Icons / 2 + own width / 2
                   )
                   if(journey == null) {
                       Text("Keine Haltestellen gefunden")
                   } else {

                       Column(
                           modifier = Modifier
                               .fillMaxHeight()
                               .padding(start = 10.dp),
                           verticalArrangement = Arrangement.spacedBy(2.dp)
                       ) {
                           journey.stops.forEach {
                                   stop -> Text(text = stop.name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                           }
                       }
                   }
               }

           }
           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Row(
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Icon(
                       imageVector = Icons.Outlined.LocationOn,
                       contentDescription = "Location Icon"
                   )
                   Text(
                       text = departure.direction,
                       style = MaterialTheme.typography.bodyMedium
                   )
               }
               Icon(
                   modifier = Modifier.rotate(iconRotation),
                   imageVector = Icons.Filled.KeyboardArrowDown,
                   contentDescription = "Open Journey"
               )
           }


       }
       Box(
           modifier = Modifier.fillMaxWidth().background(Color.White).height(4.dp)
       )

   }

}