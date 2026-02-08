package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabNavigator
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.stops
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.tabs.departure.DepartureTab
import org.jetbrains.compose.resources.stringResource


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

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stops.forEach {
                    CampusButton(
                        text = it.name,
                        onClick = {
                            DepartureTab.selectStop(it)
                            tabNavigator.current = DepartureTab
                        },
                        isActive = true
                    )
                }
            }
        }
    }
}