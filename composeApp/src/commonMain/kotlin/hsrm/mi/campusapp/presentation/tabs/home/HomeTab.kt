package hsrm.mi.campusapp.presentation.tabs.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import campusapp.composeapp.generated.resources.choose_your_campus
import campusapp.composeapp.generated.resources.home
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.domain.service.ICourseService
import hsrm.mi.campusapp.domain.service.IMenuService
import hsrm.mi.campusapp.domain.service.IStopService
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import hsrm.mi.campusapp.presentation.tabs.home.info.ArrivalInfo
import hsrm.mi.campusapp.presentation.tabs.home.info.MenuInfo
import hsrm.mi.campusapp.presentation.tabs.home.info.ScheduleInfo
import hsrm.mi.campusapp.presentation.tabs.home.info.WeatherInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime

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
        val appState = koinInject<AppState>()
        val menuService = koinInject<IMenuService>()
        val courseService = koinInject<ICourseService>()
        val stopService = koinInject<IStopService>()
        val screenModel = rememberScreenModel { HomeScreenModel(appState, menuService, courseService) }

        val tabNavigator = LocalTabNavigator.current

        val currentCampus by appState.selectedCampus.collectAsState()
        val currentCanteen by appState.selectedCanteen.collectAsState()

        LaunchedEffect(currentCampus) {
            val campus = currentCampus
            if (campus != null) {
                screenModel.loadWeather(campus)
            }
        }

        val campuses by koinInject<ICampusService>().getAllCampuses().collectAsStateWithLifecycle(initialValue = emptyList())
        val stops by stopService.getStopsForCampusName(currentCampus?.name ?: "").collectAsStateWithLifecycle(initialValue = emptyList())
        val courses by screenModel.todaysCourses.collectAsStateWithLifecycle()
        val nextCourse by screenModel.earliestCourse.collectAsStateWithLifecycle()
        val todaysMenu by screenModel.todaysMeal.collectAsStateWithLifecycle()
        val homeStop by appState.homeStop.collectAsState()

        LaunchedEffect(nextCourse, homeStop) {
            nextCourse?.let { course ->
                homeStop.let { stop ->
                    screenModel.loadArrivalTrip(stop?.id ?: "", course)
                }
            }
        }

        var visibleCount by remember { mutableIntStateOf(0) }

        LaunchedEffect(campuses) {
            campuses.forEachIndexed { index, _ ->
                visibleCount = index + 1
                delay(250)
            }
        }

        val scrollState = rememberScrollState()

        if(currentCampus == null) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                item {
                    ChooseText()
                    AnimatedBrushText("Campus")
                    Spacer(modifier = Modifier.height(20.dp))
                }
                itemsIndexed(campuses) { index, campus ->
                    AnimatedVisibility(
                        visible = index < visibleCount,
                        enter = slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(300))
                    ) {
                        CampusButton(
                            text = campus.name,
                            onClick = { screenModel.updateCampus(campus) },
                            isActive = true
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp).verticalScroll(scrollState)
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                CampusName(currentCampus!!)
                Spacer(modifier = Modifier.height(20.dp))
                WeatherInfo(screenModel.currentWeather.value)
                // Spacer(modifier = Modifier.height(20.dp))
                // DepartureInfo(stops, tabNavigator)
                Spacer(modifier = Modifier.height(20.dp))
                ScheduleInfo(nextCourse, tabNavigator) // TODO() Probably migrate to ScreenModel
                nextCourse?.let {
                    Spacer(modifier = Modifier.height(20.dp))
                    ArrivalInfo(screenModel.arrivalTrip.value, screenModel.isLoadingArrivalTrip.value)
                }
                Spacer(modifier = Modifier.height(20.dp))
                MenuInfo(currentCanteen, todaysMenu)

            }
        }
    }
}

@Composable
fun CampusName(campus: Campus, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge,
        text = campus.name,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun ChooseText() {

    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(Res.string.choose_your_campus),
        style = MaterialTheme.typography.headlineLarge
    )
}

