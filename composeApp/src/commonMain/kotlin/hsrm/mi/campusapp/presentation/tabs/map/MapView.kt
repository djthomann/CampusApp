package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.runtime.Composable
import hsrm.mi.campusapp.presentation.state.MapState

@Composable
expect fun MapView(state: MapState)