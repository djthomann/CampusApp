package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import campusapp.composeapp.generated.resources.Res
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.state.MapState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.toJson


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
            isScaleBarEnabled = true
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
            baseStyle = BaseStyle.Json(it)
        ) {

            val stopPoints = rememberGeoJsonSource(GeoJsonData.Uri(Res.getUri("files/stops.geojson")))

            CircleLayer(id = "point1", source = stopPoints, color = const(Color.Cyan), radius = const(10.dp),
                onClick = { features ->
                    println("Clicked on feature ${features[0].toJson()}")
                    ClickResult.Consume
                }
            )
        }
    }
}