package hsrm.mi.campusapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.repository.StopRepository
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

class DepartureScreen(
    val onStopSelected: () -> Unit
): CampusScreen {

    override val title = "Haltestellen"

    @Composable
    override fun Content() {

        val currentCampus = AppState.selectedCampus.value

        val stops: List<Stop> = remember(currentCampus) { currentCampus?.let { StopRepository.getStopsForCampusName(currentCampus.name) } ?: emptyList() }
        val coroutineScope = rememberCoroutineScope()
        val departures = remember { mutableStateOf<List<Departure>>(
            listOf(Departure("Bus 6", LocalTime(17, 4, 0), direction = "Wiesbaden Hauptbahnhof"),
                Departure("Bus 6", LocalTime(17, 14, 0), direction = "Unter den Eichen"))
        ) }

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            /* if (currentCampus != null) {
                CampusName(currentCampus)
            } else {
                Text("No campus selected")
            } */
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(stops) { stop ->
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                departures.value = RmvAPI.getNextArrivals(stop)
                            }
                            /*StopRepository.selectStop((stop))
                            onStopSelected() */
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        val text = remember { stop.name }
                        Text(text)
                    }
                }
            }
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

    var expanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
            .clickable {
            expanded.value = !expanded.value
        }
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp, 20.dp, 20.dp, 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(modifier = Modifier.padding(end = 10.dp), fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.name)

            Text(fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.time.toString())
        }
        AnimatedVisibility(
            visible = expanded.value
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp)
                ) {
                    Text("Haltestelle 1")
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp)
                ) {
                    Text("Haltestelle 2")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp, 5.dp, 20.dp, 20.dp) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = departure.direction
            )

            Icon(
                imageVector = if (expanded.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                contentDescription = "Open Journey"
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface)
        )
    }

}