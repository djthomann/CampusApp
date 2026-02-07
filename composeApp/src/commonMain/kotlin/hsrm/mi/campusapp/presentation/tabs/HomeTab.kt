package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ModeOfTravel
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import campusapp.composeapp.generated.resources.arrive_on_time
import campusapp.composeapp.generated.resources.choose_your_campus
import campusapp.composeapp.generated.resources.home
import campusapp.composeapp.generated.resources.next_course
import campusapp.composeapp.generated.resources.no_courses_today
import campusapp.composeapp.generated.resources.no_menu_today
import campusapp.composeapp.generated.resources.no_weather_data
import campusapp.composeapp.generated.resources.stops
import campusapp.composeapp.generated.resources.welcome_campus
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.openmeteo.OpenMeteoAPI
import hsrm.mi.campusapp.data.api.openmeteo.toDomain
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.toDomain
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Course
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.model.Trip
import hsrm.mi.campusapp.domain.model.Weather
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.service.CampusService
import hsrm.mi.campusapp.domain.service.CourseService
import hsrm.mi.campusapp.domain.service.StopService
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.components.WeatherWidget
import hsrm.mi.campusapp.presentation.components.getWeatherIcon
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.maplibre.spatialk.geojson.Position
import kotlin.time.ExperimentalTime

class HomeScreenModel: ScreenModel {

    val currentWeather = mutableStateOf<Weather?>(null)
    val todaysMeal = mutableStateOf<Menu?>(null)

    val isLoadingArrivalTrip = mutableStateOf(false)
    val arrivalTrip = mutableStateOf<Trip?>(null)

    @OptIn(ExperimentalTime::class)
    val todaysCourses: StateFlow<List<Course>> = CourseService.getCoursesForDayOfWeek(LocalDate.now().dayOfWeek)
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val earliestCourse: StateFlow<Course?> = todaysCourses
        .map { courses ->
            courses.minByOrNull { it.start }
        }
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateCampus(campus: Campus?) {
        AppState.updateCampus(campus, screenModelScope)
    }

    fun loadWeather(campus: Campus) {
        screenModelScope.launch {
            /* Tried loading API in IO Thread. Was bad for performance though
            val result = withContext(Dispatchers.IO) {
                OpenMeteoAPI.getCurrentWeather(campus)
            }
            currentWeather.value = result?.toDomain(campus)*/

            currentWeather.value = OpenMeteoAPI.getCurrentWeather(campus)?.toDomain(campus)
        }
    }

    // TODO() Fix the logic
    @OptIn(ExperimentalTime::class)
    fun loadTodaysMenu() {
        screenModelScope.launch {
            val menuEntity = DatabaseHolder.db.getMenuDao().getMenuForDay(LocalDate.now().toString()).first()
            menuEntity?.let {
                todaysMeal.value = Menu(
                    canteen = menuEntity.menu.canteen,
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

    @OptIn(ExperimentalTime::class)
    fun loadArrivalTrip(stopId: String, course: Course) {

        val stop = Stop(
            id = stopId,
            name = "Placeholder",
            position = Position(longitude = 0.0, latitude = 0.0),
            campus = null
        )

        screenModelScope.launch {
            isLoadingArrivalTrip.value = true
            val trips: List<Trip> = RmvAPI.getArrivalTripFromStopToCampus(stop, course.building.latitude, course.building.longitude, LocalDateTime(date = LocalDate.now(), course.start)).map { it.toDomain() }            // Filter trip with latest startTime
            val tripsOnTime = trips.filter { trip -> trip.arrivalTime <= course.start }

            // Filter display trip
            arrivalTrip.value = if(tripsOnTime.isNotEmpty()) tripsOnTime.first() else null

            // println("TRIP: ${arrivalTrip.value}")
            isLoadingArrivalTrip.value = false
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

        val currentCampus by AppState.selectedCampus.collectAsState()

        LaunchedEffect(currentCampus) {
            val campus = currentCampus
            if (campus != null) {
                screenModel.loadWeather(campus)
                screenModel.loadTodaysMenu()
            }
        }

        val campuses by CampusService.getAllCampuses().collectAsStateWithLifecycle(initialValue = emptyList())
        val stops by StopService.getStopsForCampusName(currentCampus?.name ?: "").collectAsStateWithLifecycle(initialValue = emptyList())
        val courses by screenModel.todaysCourses.collectAsStateWithLifecycle()
        val nextCourse by screenModel.earliestCourse.collectAsStateWithLifecycle()

        LaunchedEffect(nextCourse, AppState.homeStopId) {
            nextCourse?.let { it1 ->
                AppState.homeStopId?.let {
                    screenModel.loadArrivalTrip(it, nextCourse!!)
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

        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            AnimatedVisibility(
                visible = currentCampus == null,
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(200.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ChooseText()
                        AnimatedBrushText("Campus")
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                }
            }


            if(currentCampus != null) {
                Spacer(modifier = Modifier.height(20.dp))
                CampusName(currentCampus!!)
                Spacer(modifier = Modifier.height(20.dp))
                WeatherInfo(screenModel.currentWeather.value)
                Spacer(modifier = Modifier.height(20.dp))
                DepartureInfo(stops, tabNavigator)
                Spacer(modifier = Modifier.height(20.dp))
                ScheduleInfo(nextCourse, tabNavigator) // TODO() Probably migrate to ScreenModel
                nextCourse?.let {
                    Spacer(modifier = Modifier.height(20.dp))
                    ArrivalInfo(screenModel.arrivalTrip.value, screenModel.isLoadingArrivalTrip.value)
                }
                Spacer(modifier = Modifier.height(20.dp))
                MenuInfo(screenModel.todaysMeal.value)
            }

            Box(modifier = Modifier.padding(12.dp)) {
                /* Should display MapScreen --> Idea scraped? */
            }
        }
    }
}

@Composable
fun ArrivalInfo(trip: Trip?, isLoading: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.ModeOfTravel,
            contentDescription = "arrive on time"
        )
        Text(stringResource(Res.string.arrive_on_time))
        Spacer(modifier = Modifier.width(12.dp))
    }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO() Make string resources
        if(AppState.homeStopId != null) {
            if(trip != null) {
                Column {
                    Text(style = MaterialTheme.typography.bodyMedium, text = "Von ${trip.startTime} Bis ${trip.arrivalTime}")
                    trip.legs.filter { leg -> leg.name != "Fußweg" }.forEach { leg ->
                        Text(style = MaterialTheme.typography.bodyMedium, text = "• ${leg.name}: ${leg.origin} → ${leg.destination} ")
                    }
                }
            } else {
                if(isLoading) {
                    Text(text = "Lade Verbindungen...", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text(text = "Keine Verbindung gefunden...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            Text(text = "No home stop selected...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun WeatherInfo(currentWeather: Weather?) {

    if(currentWeather == null) {
        Text(stringResource(Res.string.no_weather_data), style = MaterialTheme.typography.bodyMedium)
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getWeatherIcon(currentWeather),
                contentDescription = "current weather"
            )
            WeatherWidget(weather = currentWeather, modifier = Modifier.wrapContentWidth())
        }
    }

}

@Composable
fun DepartureInfo(stops: List<Stop>, tabNavigator: TabNavigator) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
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
}

@Composable
fun ScheduleInfo(nextCourse: Course?, tabNavigator: TabNavigator) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = "Next course"
            )
            Text(stringResource(Res.string.next_course))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(nextCourse == null) {
                Text(stringResource(Res.string.no_courses_today), style = MaterialTheme.typography.bodyMedium)
            } else {
                CampusButton(
                    text = nextCourse.name,
                    onClick = {
                        tabNavigator.current = ScheduleTab
                    },
                    isActive = true
                )
            }
        }
    }
}

@Composable
fun MenuInfo(menu: Menu?) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector =  FoodTab.activeIcon,
                contentDescription = FoodTab.topAppBarTitle
            )
            Text(FoodTab.topAppBarTitle)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                if(menu == null) {
                    Text(stringResource(Res.string.no_menu_today), style = MaterialTheme.typography.bodyMedium)
                } else {
                    menu.dishes.sortedBy { it.price }.forEach { dish -> Text("• ${dish.name}", style = MaterialTheme.typography.bodyMedium) }
                }
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

/* https://medium.com/androiddevelopers/animating-brush-text-coloring-in-compose-%EF%B8%8F-26ae99d9b402 */
@Composable
fun AnimatedBrushText(text: String) {
    val infiniteTransition = rememberInfiniteTransition()

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
        )
    )

    val brush = remember(offset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * offset
                val heightOffset = size.height * offset
                return LinearGradientShader(
                    colors = listOf(
                        Color(0xff28d400),
                        Color(0xff126100),
                        Color(0xff47b32e)
                    ),
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }

    Text(
        text = text,
        style = TextStyle(
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            brush = brush
        )
    )
}

