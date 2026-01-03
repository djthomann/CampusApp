package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.StopRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

class DepartureScreen(
    val onStopSelected: () -> Unit
): CampusScreen {

    override val title = "Stops"
    val campus = CampusRepository.selectedCampus

    @Composable
    override fun Content() {

        val stops = remember(campus) { StopRepository.getStopsForCampusName(campus.name) }
        val coroutineScope = rememberCoroutineScope()
        val departures = remember { mutableStateOf<List<Departure>>(
            listOf(Departure("Bus 6", LocalTime(17, 4, 0), direction = "Wiesbaden Hauptbahnhof"),
                Departure("Bus 6", LocalTime(17, 14, 0), direction = "Unter den Eichen"))
        ) }

        Column(
            modifier = Modifier.fillMaxSize().background(color = Color.LightGray)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().background(color = Color.LightGray)
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
            Column(

            ) {
                departures.value.forEach { dep -> DepartureEntry(dep) }
            }
        }
    }
}

@Composable
fun DepartureEntry(departure: Departure) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Color.Cyan).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(modifier = Modifier.padding(end = 10.dp), fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.name)
        Text(modifier = Modifier.weight(1f), text = departure.direction)
        Text(fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.time.toString())
    }
}