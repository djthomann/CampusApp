package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import campusapp.composeapp.generated.resources.choose_your_campus
import campusapp.composeapp.generated.resources.home
import campusapp.composeapp.generated.resources.stops
import campusapp.composeapp.generated.resources.welcome_campus
import campusapp.composeapp.generated.resources.work_in_progress
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.CourseRepository
import hsrm.mi.campusapp.domain.repository.StopRepository
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

class HomeScreenModel: ScreenModel {


}

object HomeTab: CampusTab {

    private fun readResolve(): Any = HomeTab

    override val topAppBarTitle = runBlocking { getString(Res.string.app_name) }
    override val activeIcon: ImageVector = Icons.Filled.Home
    override val inactiveIcon: ImageVector = Icons.Outlined.Home

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.home)
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { HomeScreenModel() }

        val tabNavigator = LocalTabNavigator.current

        val currentCampus = AppState.selectedCampus.value
        val stops: List<Stop> = remember(currentCampus) { currentCampus?.let { StopRepository.getStopsForCampusName(currentCampus.name) } ?: emptyList() }
        val courses = CourseRepository.getCoursesForDayOfWeek(LocalDate.now().dayOfWeek)

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
                                CampusButton(
                                    text = campus.name,
                                    onClick = { AppState.selectCampus(campus) },
                                    isActive = true
                                )
                            }
                        }
                    }
                }
            }
            currentCampus?.let {
                WelcomeText(it)
                Spacer(modifier = Modifier.height(20.dp))
            }

            if(currentCampus != null) {

                Column {
                    Row {
                        Icon(
                            imageVector = DepartureTab.activeIcon,
                            contentDescription = DepartureTab.topAppBarTitle
                        )
                        Text(stringResource(Res.string.stops))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(stops) { stop ->
                                CampusButton(
                                    text = stop.name,
                                    onClick = {
                                        DepartureTab.selectStop(stop)
                                        tabNavigator.current = DepartureTab
                                    },
                                    isActive = true
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Row {
                        Icon(
                            imageVector = ScheduleTab.activeIcon ,
                            contentDescription = ScheduleTab.topAppBarTitle
                        )
                        Text(ScheduleTab.topAppBarTitle)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(courses) { course ->
                                CampusButton(
                                    text = course.name,
                                    onClick = {
                                        tabNavigator.current = ScheduleTab
                                    },
                                    isActive = true
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Row {
                        Icon(
                            imageVector =  FoodTab.activeIcon  ,
                            contentDescription = FoodTab.topAppBarTitle
                        )
                        Text(FoodTab.topAppBarTitle)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = stringResource(Res.string.work_in_progress), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Box(modifier = Modifier.padding(12.dp)) {
                /* Should display MapScreen --> Idea scraped? */
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

    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(Res.string.choose_your_campus),
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun WelcomeText(campus: Campus) {

    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(Res.string.welcome_campus),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge
    )
    CampusName(campus)
}

