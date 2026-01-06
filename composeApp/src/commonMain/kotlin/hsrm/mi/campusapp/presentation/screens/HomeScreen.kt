package hsrm.mi.campusapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import hsrm.mi.campusapp.domain.repository.CampusRepository


class HomeScreenModel: ScreenModel {
    var selectedCampus = mutableStateOf<Campus?>(null)
}

class HomeScreen: CampusScreen {

    override val title = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { HomeScreenModel() }

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {

            AnimatedVisibility(
                visible = screenModel.selectedCampus.value == null,
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
                                    onClick = { screenModel.selectedCampus.value = campus },
                                ) {
                                    Text(campus.name)
                                }
                            }
                        }
                    }
                }
            }
            screenModel.selectedCampus.value?.let {
                WelcomeText(it)
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
        color = MaterialTheme.colorScheme.onBackground
    )
    CampusName(campus)
}