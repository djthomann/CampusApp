package hsrm.mi.campusapp.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.repository.CampusRepository
import hsrm.mi.campusapp.repository.StopRepository

class DepartureScreen(
    val onStopSelected: () -> Unit
): CampusScreen {

    override val title = "Stops"
    val campus = CampusRepository.selectedCampus

    @Composable
    override fun Content() {

        val stops = remember(campus) { StopRepository.getStopsForCampusName(campus.name) }

        LazyColumn {
            items(stops) { stop ->
                Button(
                    onClick = {
                        StopRepository.selectStop((stop))
                        onStopSelected()
                              },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(stop.name)
                }
            }
        }
    }
}