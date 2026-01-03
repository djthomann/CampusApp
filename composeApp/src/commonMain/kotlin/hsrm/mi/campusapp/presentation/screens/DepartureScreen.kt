package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.StopRepository
import kotlinx.coroutines.launch

class DepartureScreen(
    val onStopSelected: () -> Unit
): CampusScreen {

    override val title = "Stops"
    val campus = CampusRepository.selectedCampus

    @Composable
    override fun Content() {

        val stops = remember(campus) { StopRepository.getStopsForCampusName(campus.name) }
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(color = Color.LightGray)
        ) {
            items(stops) { stop ->
                Button(
                    onClick = {
                        coroutineScope.launch {
                            println(RmvAPI.getNextArrivals(stop))
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
    }
}