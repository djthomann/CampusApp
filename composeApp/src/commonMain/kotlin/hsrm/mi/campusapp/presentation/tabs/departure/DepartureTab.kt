package hsrm.mi.campusapp.presentation.tabs.departure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DepartureBoard
import androidx.compose.material.icons.outlined.DepartureBoard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.departures_tab_title
import campusapp.composeapp.generated.resources.loading_departures
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.service.StopService
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

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

        val currentCampus by AppState.selectedCampus.collectAsState()
        val stops by StopService.getStopsForCampusName(currentCampus?.name ?: "").collectAsStateWithLifecycle(initialValue = emptyList())

        var selectedEntry by remember { mutableStateOf<SelectedDepartureEntry?>(null) }

        LaunchedEffect(pendingStop.value) {
            pendingStop.value?.let { stop ->
                screenModel.loadDepartures(stop)
                pendingStop.value = null // Zurücksetzen, nachdem geladen wurde
            }
        }

        if(screenModel.currentStop.value == null) {
            // Try loading initial departures
            if(stops.isNotEmpty())  {
                pendingStop.value = stops.first()
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
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stops.forEach {
                    CampusButton(
                        text = it.name,
                        onClick = {
                            screenModel.loadDepartures(it)
                        },
                        isActive = screenModel.currentStop.value == it
                    )
                }
            }
            Spacer(
                modifier = Modifier.padding(5.dp)
            )


            if(screenModel.departures.value.isEmpty()) {
                if (screenModel.isLoadingDepartures) {
                    Text(textAlign = TextAlign.Center, text = stringResource(Res.string.loading_departures))
                } else {
                    EmptyIndicator()
                }

            } else {
                screenModel.currentStop.value?.let { currentStop ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(screenModel.departures.value) { dep ->
                            DepartureEntry(dep, currentStop, selectedDepartureEntry = selectedEntry,
                                expandClick = {
                                    selectedEntry?.let {
                                        if(it.departure == dep) {
                                            selectedEntry = SelectedDepartureEntry(dep, it.getNextState())
                                        }
                                    }
                                },
                                onClick = {
                                    selectedEntry = if(selectedEntry?.departure == dep) {
                                        null
                                    } else {
                                        SelectedDepartureEntry(dep, DepartureEntryState.COLLAPSED_JOURNEY)
                                    }
                                }
                            )
                        }
                    }
                }

            }
        }
    }


}