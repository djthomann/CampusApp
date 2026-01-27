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
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import campusapp.composeapp.generated.resources.choose_your_campus
import campusapp.composeapp.generated.resources.home
import campusapp.composeapp.generated.resources.stops
import campusapp.composeapp.generated.resources.welcome_campus
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.openmeteo.CurrentWeather
import hsrm.mi.campusapp.data.api.openmeteo.OpenMeteoAPI
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.repository.CampusRepository
import hsrm.mi.campusapp.domain.repository.CourseRepository
import hsrm.mi.campusapp.domain.repository.StopRepository
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.components.WeatherWidget
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

class HomeScreenModel: ScreenModel {

    val currentWeather = mutableStateOf<CurrentWeather?>(null)
    val todaysMeal = mutableStateOf<Menu?>(null)

    fun loadWeather(campus: Campus) {
        screenModelScope.launch {
            currentWeather.value = OpenMeteoAPI.getCurrentWeather(campus)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun loadTodaysMenu() {
        screenModelScope.launch {
            val menuEntity = DatabaseHolder.db.getMenuDao().getMenuForDay(LocalDate.now().toString()).first()
            menuEntity?.let {
                todaysMeal.value = Menu(
                    date = LocalDate.parse(menuEntity.menu.date),
                    dateString = menuEntity.menu.dateString,
                    dishes = menuEntity.dishes.map { dishEntity ->
                        Dish(
                            name = dishEntity.name,
                            description = dishEntity.description,
                            price = dishEntity.price,
                            dishAllergens = dishEntity.dishAllergens
                        )
                    },
                    sideDishes = menuEntity.sideDishes.groupBy { it.type }.mapValues { (_, entities) -> entities.map { it.name } }
                )
            }

        }
    }

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

        val currentCampus = AppState.selectedCampus

        if(currentCampus != null) {
            if (screenModel.currentWeather.value == null) {
                screenModel.loadWeather(currentCampus) // TODO() Refresh after a certain time and after campus switch
            }

            screenModel.loadTodaysMenu()
        }

        val stops: List<Stop> = remember(currentCampus) { currentCampus?.let { StopRepository.getStopsForCampusName(currentCampus.name) } ?: emptyList() }
        val courses = CourseRepository.getCoursesForDayOfWeek(LocalDate.now().dayOfWeek)

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            AnimatedVisibility(
                visible = AppState.selectedCampus == null,
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
                                    onClick = {
                                        AppState.selectCampus(campus)
                                              },
                                    isActive = true
                                )
                            }
                        }
                    }
                }
            }
            currentCampus?.let {
                /*
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WelcomeText(it)

                }*/
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    CampusName(it, modifier = Modifier.weight(1f))
                    screenModel.currentWeather.value?.let { weather ->
                        WeatherWidget(weather = weather, modifier = Modifier.wrapContentWidth())
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))

            }

            if(currentCampus != null) {

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                screenModel.todaysMeal.value?.let {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector =  FoodTab.activeIcon  ,
                                contentDescription = FoodTab.topAppBarTitle
                            )
                            Text(FoodTab.topAppBarTitle)
                        }
                        Column (
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            it.dishes.forEach { dish -> Text("• ${dish.name}", style = MaterialTheme.typography.bodyMedium) }
                        }
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

@Composable
fun WelcomeText(campus: Campus) {

    Text(
        modifier = Modifier.padding(4.dp),
        text = stringResource(Res.string.welcome_campus),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge
    )
}

