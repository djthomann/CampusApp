package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import campusapp.composeapp.generated.resources.Res
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.state.MapState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.BaseStyle


@Composable
actual fun MapView(state: MapState) {
    val cameraState = rememberCameraState(state.cameraPosition)

    var jsonString by remember { mutableStateOf<String?>(null) }
    val isDarkMode by AppState.isDarkMode
    val variant = if (isDarkMode) "dark" else "light"

    LaunchedEffect(variant) {
        jsonString = Res
            .readBytes("files/liberty-$variant.json")
            .decodeToString()
        print(variant)
    }

    LaunchedEffect(state.cameraPosition) {
        cameraState.animateTo(state.cameraPosition)
    }

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

    jsonString?.let {
        MaplibreMap(
            modifier = Modifier.fillMaxSize(),
            cameraState = cameraState,
            options = mapOptions,
            baseStyle = BaseStyle.Json(it))
    }
}