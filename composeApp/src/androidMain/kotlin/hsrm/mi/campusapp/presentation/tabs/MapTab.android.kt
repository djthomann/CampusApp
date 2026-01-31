package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.pin_green
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.repository.CampusRepository
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
import org.maplibre.compose.layers.FillExtrusionLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.location.DesiredAccuracy
import org.maplibre.compose.location.LocationPuck
import org.maplibre.compose.location.rememberAndroidLocationProvider
import org.maplibre.compose.location.rememberUserLocationState
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
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds


@Composable
actual fun MapView(state: MapState) {
    val cameraState = rememberCameraState(state.cameraPosition)

    var jsonString by remember { mutableStateOf<String?>(null) }
    val isDarkMode by AppState.isDarkMode
    val variant = if (isDarkMode) "dark" else "light"

    val locationProvider = rememberAndroidLocationProvider(
        updateInterval = 1.seconds,
        desiredAccuracy = DesiredAccuracy.High,
        minDistanceMeters = 1.0f
    )
    val userLocationState = rememberUserLocationState(locationProvider)
    val locationState by locationProvider.location.collectAsStateWithLifecycle()

    var selectedFeature by remember {
        mutableStateOf<Feature<Geometry, JsonObject?>?>(null)
    }

    val campuses = CampusRepository.campuses

    LaunchedEffect(variant) {
        jsonString = Res
            .readBytes("files/liberty-$variant.json")
            .decodeToString()
        print(variant)
    }

    LaunchedEffect(state.cameraPosition) {
        cameraState.animateTo(state.cameraPosition)
    }

    LaunchedEffect(selectedFeature) {
        selectedFeature?.let {
            val point = it.geometry as Point
            MapTab.moveToPosition(Position(point.longitude, point.latitude))
        }
    }

    val mapOptions = MapOptions(
        renderOptions = RenderOptions.Standard,
        gestureOptions = if(selectedFeature != null) GestureOptions.AllDisabled else GestureOptions.Standard,
        ornamentOptions = OrnamentOptions(
            isCompassEnabled = true,
            isLogoEnabled = true,
            isScaleBarEnabled = false
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
                locationState?.let { state ->
                    LocationPuck(
                        "user-location",
                        locationState = userLocationState,
                        cameraState = cameraState
                    )
                }

                val campusBuildings = rememberGeoJsonSource(GeoJsonData.Uri(Res.getUri("files/campus-buildings.geojson")))
                val marker = painterResource(Res.drawable.pin_green)

                campuses.forEach { campus -> CampusLayers(campus) }

                /* Include for other campuses aswell */
                SymbolLayer(
                    id = "building-points",
                    source = campusBuildings,
                    onClick = { features ->
                        selectedFeature = features.firstOrNull()
                        ClickResult.Consume
                    },
                    iconImage = image(marker, size = DpSize(20.dp, 30.dp)),
                    textField = format(),
                    textColor = const(MaterialTheme.colorScheme.onBackground),
                    textOffset = offset(0.em, 0.6.em),
                )



            }
        }

        AnimatedVisibility(
            visible = selectedFeature != null,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            selectedFeature?.let { feature ->
                FeatureCard(
                    onDismiss = { selectedFeature = null },
                    feature = feature
                )
            }
        }
    }

}

@Composable
fun FeatureCard(onDismiss: () -> Unit, feature: Feature<Geometry, JsonObject?>) {

    Card(
        modifier = Modifier.padding(10.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHigh),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            feature.properties?.get("name")?.let { Text(it.jsonPrimitive.content, color = MaterialTheme.colorScheme.onSurface) }
            IconButton(onClick = { onDismiss() }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear Feature",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun CampusLayers(campus: Campus) {

    val campusBuildings = rememberGeoJsonSource(GeoJsonData.Uri(Res.getUri("files/${campus.jsonPath}")))

    FillExtrusionLayer(
        id = "buildings-3d-${campus.name}",
        source = campusBuildings,
        height = const(10.0f),
        color = const(MaterialTheme.colorScheme.secondary),
        opacity = const(0.5f)
    )
}