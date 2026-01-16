package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import hsrm.mi.campusapp.presentation.state.MapState
import hsrm.mi.campusapp.presentation.state.MapViewModel


class MapScreenModel: ScreenModel {
    val mapViewModel = MapViewModel()
}

class MapScreen(): Screen {

    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { MapScreenModel() }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapView(screenModel.mapViewModel.uiState)
        }
    }
}

@Composable
expect fun MapView(state: MapState)