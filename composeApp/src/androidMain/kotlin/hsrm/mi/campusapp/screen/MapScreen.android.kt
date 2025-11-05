package hsrm.mi.campusapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.dellisd.spatialk.geojson.Position
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState

@Composable
actual fun MapView() {
    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position( 8.217, 50.0964),
            zoom = 16.0,
            tilt = 45.0,
            bearing = 0.0
        )
    )

    val styleState = rememberStyleState()

    val mapOptions = MapOptions(
        renderOptions = RenderOptions.Standard,
        gestureOptions = GestureOptions.Standard,
        ornamentOptions = OrnamentOptions(
            isCompassEnabled = true,
            isLogoEnabled = true
        )
    )

    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        styleState = styleState,
        options = mapOptions,
        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
    ) {

    }
}