package hsrm.mi.campusapp.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import hsrm.mi.campusapp.presentation.state.MapState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState

@Composable
actual fun MapView(state: MapState) {
    val cameraState = rememberCameraState(state.cameraPosition)

    LaunchedEffect(state.cameraPosition) {
        cameraState.animateTo(state.cameraPosition)
    }

    val styleState = rememberStyleState()

    val mapOptions = MapOptions(
        renderOptions = RenderOptions.Standard,
        gestureOptions = GestureOptions.Standard,
        ornamentOptions = OrnamentOptions(
            isCompassEnabled = true,
            isLogoEnabled = true,
            isScaleBarEnabled = false
        )
    )


    /*LaunchedEffect(campus) {
        println("New")
        cameraState.animateTo(
            CameraPosition(
                target = campus.center,
                zoom = 16.0,
                tilt = 45.0,
                bearing = 0.0
            )
        )
    }*/

    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        styleState = styleState,
        options = mapOptions,
        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
    ) {

    }
}