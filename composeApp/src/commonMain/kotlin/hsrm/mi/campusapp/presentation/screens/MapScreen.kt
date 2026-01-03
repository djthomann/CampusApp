package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import hsrm.mi.campusapp.presentation.state.MapState
import hsrm.mi.campusapp.presentation.state.MapViewModel

class MapScreen(
    private val viewModel: MapViewModel
): Screen {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MapView(viewModel.uiState)
        }
    }
}

@Composable
expect fun MapView(state: MapState)