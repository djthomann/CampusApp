package hsrm.mi.campusapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DepartureBoard
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.StopRepository
import hsrm.mi.campusapp.presentation.state.AppState


class HomeScreenModel: ScreenModel {


}

class HomeScreen: CampusScreen {

    override val title = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { HomeScreenModel() }

        val currentCampus = AppState.selectedCampus.value
        val stops: List<Stop> = remember(currentCampus) { currentCampus?.let { StopRepository.getStopsForCampusName(currentCampus.name) } ?: emptyList() }

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {

            AnimatedVisibility(
                visible = AppState.selectedCampus.value == null,
            ) {
                Column {
                    ChooseText()
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(CampusRepository.campuses) { campus ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp).animateItem(
                                        fadeInSpec = tween(durationMillis = 300),
                                        fadeOutSpec = tween(durationMillis = 300),
                                        placementSpec = tween(durationMillis = 300)
                                    )
                            ) {
                                Button(
                                    onClick = { AppState.selectCampus(campus) },
                                ) {
                                    Text(campus.name)
                                }
                            }
                        }
                    }
                }
            }
            currentCampus?.let {
                WelcomeText(it)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector =  Icons.Filled.DepartureBoard,
                    contentDescription = "Departures"
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(stops) { stop ->
                        Button(
                            onClick = {

                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            val text = remember { stop.name }
                            Text(text)
                        }
                    }
                }
            }

            Box(modifier = Modifier.padding(12.dp)) {
                /* Should display MapScreen */
            }
        }
    }
}

@Composable
fun CampusName(campus: Campus) {
    Text(style = MaterialTheme.typography.headlineLarge,
        text = campus.name,
        color = MaterialTheme.colorScheme.onSecondary)
}

@Composable
fun ChooseText() {

    val titleFontSize = MaterialTheme.typography.headlineLarge.fontSize
    val titleColor = MaterialTheme.colorScheme.onBackground

    Text(
        modifier = Modifier.padding(4.dp),
        text =  buildAnnotatedString {
            withStyle(
                style = ParagraphStyle(lineHeight = 50.sp)
            ) {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        fontSize = titleFontSize
                    )
                ) {
                    append("Wähle Deinen\n")
                    append("Campus!")
                }
            }
        }
    )
}

@Composable
fun WelcomeText(campus: Campus) {

    Text(
        modifier = Modifier.padding(4.dp),
        text =  "Willkommen am Campus:",
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge
    )
    CampusName(campus)
}