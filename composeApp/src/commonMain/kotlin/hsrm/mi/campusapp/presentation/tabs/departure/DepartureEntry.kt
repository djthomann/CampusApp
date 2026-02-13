package hsrm.mi.campusapp.presentation.tabs.departure

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsRailway
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.final_stop
import campusapp.composeapp.generated.resources.no_stops_found
import campusapp.composeapp.generated.resources.show_less
import campusapp.composeapp.generated.resources.show_more
import hsrm.mi.campusapp.data.api.rmv.RmvAPI.normalizeRmvId
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.model.Vehicle
import org.jetbrains.compose.resources.stringResource

enum class DepartureEntryState {
    COLLAPSED_JOURNEY, OPEN
}

class SelectedDepartureEntry(
    var departure: Departure,
    var entryState: DepartureEntryState
) {
    fun getNextState(): DepartureEntryState {
        return if (entryState == DepartureEntryState.COLLAPSED_JOURNEY) {
            DepartureEntryState.OPEN
        } else {
            DepartureEntryState.COLLAPSED_JOURNEY
        }
    }
}

fun getIconForVehicle(vehicle: Vehicle): ImageVector {
    return when(vehicle) {
        Vehicle.BUS -> Icons.Filled.DirectionsBus
        Vehicle.S_BAHN -> Icons.Filled.Train
        Vehicle.REGIONAL_TRAIN -> Icons.Filled.DirectionsRailway
        Vehicle.UNKNOWN -> Icons.Filled.Commute // fallback
    }
}

@Composable
fun DepartureEntry(departure: Departure, currentStop: Stop, selectedDepartureEntry: SelectedDepartureEntry?, expandClick: () -> Unit, onClick: () -> Unit) {

    val journey = departure.journey

    val expanded = selectedDepartureEntry != null && selectedDepartureEntry.departure == departure

    val journeyOpen = expanded && selectedDepartureEntry.entryState == DepartureEntryState.OPEN

    val backgroundColor by animateColorAsState(
        targetValue = if (expanded)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "backgroundColor"
    )

    val textColor = if (expanded)
        MaterialTheme.colorScheme.onSecondary
    else
        MaterialTheme.colorScheme.onSurface

    val iconRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "iconRotation"
    )

    Column(
        modifier = Modifier
            // .shadow(5.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(backgroundColor)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
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
                        imageVector = getIconForVehicle(departure.vehicle),
                        contentDescription = "Course Type Icon",
                        modifier = Modifier.size(24.dp),
                        tint = textColor
                    )
                    Text(color = textColor, modifier = Modifier.padding(end = 10.dp), fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.name)
                }
                Text(color = textColor, fontSize =  24.sp, fontWeight = FontWeight.Bold, text = departure.time.toString())
            }
            AnimatedVisibility(
                visible = expanded
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

                            val journeyStops = journey.stops.drop(currentIndex + 1)
                            val nextStops = if(!journeyOpen) {
                                journeyStops.take(3)
                            } else {
                                journeyStops.dropLast(1)
                            }

                            // println("CURRENT INDEX $currentIndex")
                            // println("NEXT STOPS: $nextStops")

                            if (nextStops.isEmpty()) {
                                Text(color = textColor, text = stringResource(Res.string.final_stop), style = MaterialTheme.typography.bodySmall)
                            } else {
                                nextStops.forEach { stop ->
                                    Text(
                                        text = stop.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = textColor.copy(alpha = 0.5f)
                                    )
                                }
                            }
                            if(journey.stops.size > 3) {
                                Box(
                                    modifier = Modifier.padding(10.dp).clickable {
                                        expandClick()
                                    }
                                ) {
                                    Text(
                                        modifier = Modifier,
                                        text = if(!journeyOpen) stringResource(Res.string.show_more) else stringResource(Res.string.show_less),
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
                        modifier = Modifier.weight(1f),
                        text = departure.direction,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                    Icon(
                        modifier = Modifier.rotate(iconRotation),
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Open Journey",
                        tint = textColor
                    )
                }

            }


        }
        Box(
            modifier = Modifier.fillMaxWidth().background(if(!expanded) textColor else Color.Transparent).height(4.dp)
        )

    }

}