package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.domain.repository.CampusRepository

class HomeScreen: CampusScreen {

    override val title = "Campus App"
    @Composable
    override fun Content() {
        /*Box(modifier = Modifier.fillMaxSize()) {
            Text("Test")
        }*/
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(color = Color.LightGray)
        ) {
            items(CampusRepository.campuses) { campus ->
                Button(
                    onClick = { CampusRepository.selectCampus(campus) },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(campus.name)
                }
            }
        }
    }
}