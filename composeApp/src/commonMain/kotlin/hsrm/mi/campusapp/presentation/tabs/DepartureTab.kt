package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DepartureBoard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.DepartureBoard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.departures_tab_title
import campusapp.composeapp.generated.resources.final_stop
import campusapp.composeapp.generated.resources.no_departure_in_x_minutes
import campusapp.composeapp.generated.resources.no_stops_found
import campusapp.composeapp.generated.resources.show_more
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import hsrm.mi.campusapp.data.api.rmv.RmvAPI.normalizeRmvId
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.repository.StopRepository
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

class DepartureScreenModel: ScreenModel {
    val currentStop = mutableStateOf<Stop?>(null)

    val departures = mutableStateOf<List<Departure>>( /*
            listOf(Departure(JourneyDetailRef("id"),"Bus 6", LocalTime(17, 4, 0), direction = "Wiesbaden Hauptbahnhof"),
                Departure(JourneyDetailRef("id"), "Bus 6", LocalTime(17, 14, 0), direction = "Unter den Eichen"))
        */ emptyList()
    )

    fun loadDepartures(stop: Stop) {
        currentStop.value = stop
        screenModelScope.launch {
            departures.value = RmvAPI.getNextArrivals(stop)

            departures.value.forEach { departure ->
                launch {
                    departure.journey = RmvAPI.getJourneyDetails(departure.ref)
                }
            }
        }
    }
}

object DepartureTab: CampusTab {
    private fun readResolve(): Any = DepartureTab

    override val topAppBarTitle = runBlocking { getString(Res.string.departures_tab_title) }
    override val activeIcon = Icons.Filled.DepartureBoard
    override val inactiveIcon = Icons.Outlined.DepartureBoard

    override val options: TabOptions
        @Composable
        get() {
            val title = topAppBarTitle
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    private val pendingStop = mutableStateOf<Stop?>(null)
    fun selectStop(stop: Stop) {
        pendingStop.value = stop
    }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { DepartureScreenModel() }

        val currentCampus = AppState.selectedCampus
        val stops: List<Stop> = remember(currentCampus) { currentCampus?.let { StopRepository.getStopsForCampusName(currentCampus.name) } ?: emptyList() }

        LaunchedEffect(pendingStop.value) {
            pendingStop.value?.let { stop ->
                screenModel.loadDepartures(stop)
                pendingStop.value = null // Zurücksetzen, nachdem geladen wurde
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            /* if (currentCampus != null) {
                CampusName(currentCampus)
            } else {
                Text("No campus selected")
            } */
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stops) { stop ->

                    val isActive = stop == screenModel.currentStop.value

                    CampusButton(
                        text = stop.name,
                        onClick = {
                            screenModel.loadDepartures(stop)
                            /*StopRepository.selectStop((stop))
                            onStopSelected() */
                        }
                    )
                }
            }
            Spacer(
                modifier = Modifier.padding(5.dp)
            )
            if(screenModel.departures.value.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(Res.string.no_departure_in_x_minutes, RmvAPI.SEARCH_TIMEFRAME_MINUTES))
                }
            } else {
                screenModel.currentStop.value?.let { currentStop ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(screenModel.departures.value) { dep ->
                            DepartureEntry(dep, currentStop)
                        }
                    }
                }

            }


        }
    }


}


@Composable
fun DepartureEntry(departure: Departure, currentStop: Stop) {

    val journey = departure.journey

    val expanded = remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (expanded.value)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "backgroundColor"
    )

    val textColor = if (expanded.value)
        MaterialTheme.colorScheme.onSecondary
    else
        MaterialTheme.colorScheme.onSurface

    val iconRotation by animateFloatAsState(
        targetValue = if (expanded.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "iconRotation"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(backgroundColor)

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = !expanded.value }
                .padding(12.dp)
        )    {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DirectionsBus,
                        contentDescription = "Course Type Icon",
                        modifier = Modifier.size(24.dp),
                        tint = textColor
                    )
                    Text(color = textColor, modifier = Modifier.padding(end = 10.dp), fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.name)
                }
                Text(color = textColor, fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.time.toString())
            }
            AnimatedVisibility(
                visible = expanded.value
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                ) {
                    VerticalDivider(color = textColor, thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 11.dp) // Width Icons / 2 + own width / 2
                    )
                    if(journey == null) {
                        Text(color = textColor, text = stringResource(Res.string.no_stops_found))
                    } else {

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            val stopId = currentStop.id.normalizeRmvId()
                            val currentIndex = journey.stops.indexOfFirst { it.id == stopId } /* id doesn't work because somehow it's not identical over different requests? */
                            val nextStops = journey.stops.drop(currentIndex + 1).take(3)

                            println("CURRENT INDEX $currentIndex")
                            println("NEXT STOPS: $nextStops")

                            if (nextStops.isEmpty()) {
                                Text(color = textColor, text = stringResource(Res.string.final_stop), style = MaterialTheme.typography.bodySmall)
                            } else {
                                nextStops.forEach { stop ->
                                    Text(
                                        text = stop.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = textColor.copy(0.5f)
                                    )
                                }
                            }
                            if(journey.stops.size > 3) {
                                Box(
                                    modifier = Modifier.padding(10.dp).clickable {
                                        println("It worked") /* TODO() */
                                    }
                                ) {
                                    Text(
                                        modifier = Modifier,
                                        text = stringResource(Res.string.show_more),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = textColor
                                    )
                                }
                            }

                        }
                    }
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location Icon",
                        tint = textColor
                    )
                    Text(
                        text = departure.direction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
                Icon(
                    modifier = Modifier.rotate(iconRotation),
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Open Journey",
                    tint = textColor
                )
            }


        }
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.White).height(4.dp)
        )

    }

}