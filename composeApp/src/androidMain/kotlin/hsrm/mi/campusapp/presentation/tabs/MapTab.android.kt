package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.pin_green
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.state.MapState
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.painterResource
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.format
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.dsl.offset
import org.maplibre.compose.expressions.dsl.span
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.map.RenderOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.Geometry


@Composable
actual fun MapView(state: MapState) {
    val cameraState = rememberCameraState(state.cameraPosition)

    var jsonString by remember { mutableStateOf<String?>(null) }
    val isDarkMode by AppState.isDarkMode
    val variant = if (isDarkMode) "dark" else "light"

    var selectedFeature by remember {
        mutableStateOf<Feature<Geometry, JsonObject?>?>(null)
    }

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
        gestureOptions = if(selectedFeature != null) GestureOptions.AllDisabled else GestureOptions.Standard,
        ornamentOptions = OrnamentOptions(
            isCompassEnabled = true,
            isLogoEnabled = true,
            isScaleBarEnabled = true
        )
    )


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        jsonString?.let {
            MaplibreMap(
                modifier = Modifier.fillMaxSize(),
                cameraState = cameraState,
                options = mapOptions,
                baseStyle = BaseStyle.Json(it)
            ) {

                val campusBuildings = rememberGeoJsonSource(GeoJsonData.Uri(Res.getUri("files/campus-buildings.geojson")))
                val marker = painterResource(Res.drawable.pin_green)

                SymbolLayer(
                    id = "building-points",
                    source = campusBuildings,
                    onClick = { features ->
                        selectedFeature = features.firstOrNull()
                        println("Clicked on Symbol")
                        ClickResult.Consume
                    },
                    iconImage = image(marker, size = DpSize(20.dp, 30.dp)),
                    textField =
                        format(
                            span(image("stop"))
                        ),
                    textColor = const(MaterialTheme.colorScheme.onBackground),
                    textOffset = offset(0.em, 0.6.em),
                )



            }
        }

        selectedFeature?.let { feature ->
            Card(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    feature.properties?.get("name")?.let { Text(it.jsonPrimitive.content) }
                    CampusButton(
                        text = "Close",
                        onClick = {
                            selectedFeature = null
                        }
                    )
                }
            }
        }
    }

}