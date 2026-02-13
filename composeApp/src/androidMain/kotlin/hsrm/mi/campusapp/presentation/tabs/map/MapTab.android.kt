package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import campusapp.composeapp.generated.resources.Res
import hsrm.mi.campusapp.domain.model.Building
import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.presentation.state.AppState
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.koinInject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraState
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.Feature.id
import org.maplibre.compose.expressions.dsl.condition
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.eq
import org.maplibre.compose.expressions.dsl.switch
import org.maplibre.compose.expressions.value.StringValue
import org.maplibre.compose.layers.FillExtrusionLayer
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
import org.maplibre.spatialk.geojson.Polygon
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds


@Composable
actual fun MapView(mapScreenModel: MapScreenModel) {
    val cameraState = rememberCameraState(CameraPosition(
        target = mapScreenModel.defaultCenter,
        zoom = 16.0,
        tilt = 45.0,
        bearing = 0.0
    ))

    var jsonString by remember { mutableStateOf<String?>(null) }
    val isDarkMode by koinInject<AppState>().isDarkMode
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

    var selectedBuilding by remember {
        mutableStateOf<Building?>(null)
    }

    val campuses by koinInject<ICampusService>().getAllCampuses().collectAsStateWithLifecycle(initialValue = emptyList())
    val currentCampus by koinInject<AppState>().selectedCampus.collectAsState()

    LaunchedEffect(currentCampus) {
        val campus = currentCampus
        if (campus != null) {
            animateCameraStateToTarget(cameraState, campus.center)
        }
    }

    LaunchedEffect(mapScreenModel.target) {
        mapScreenModel.target.value?.let { target ->
            animateCameraStateToTargetBuilding(cameraState, target)
            mapScreenModel.clearTarget()
            selectedBuilding = target
        }
    }

    LaunchedEffect(variant) {
        jsonString = Res
            .readBytes("files/map-themes/liberty-$variant.json")
            .decodeToString()
        // print(variant)
    }

    LaunchedEffect(selectedFeature) {

        if(selectedFeature == null) {
            selectedBuilding = null
        }

        selectedFeature?.let { feature ->
            val targetPosition = when (val geometry = feature.geometry) {
                is Point -> {
                    Position(geometry.longitude, geometry.latitude)
                }
                is Polygon -> { // TODO() Move computation to JSON source
                    val points = geometry.coordinates.firstOrNull() ?: emptyList()
                    if (points.isNotEmpty()) {
                        val avgLat = points.map { it.latitude }.average()
                        val avgLon = points.map { it.longitude }.average()
                        Position(avgLon, avgLat)
                    } else null
                }
                else -> null
            }

            targetPosition?.let { pos ->

                println("TARGET: $targetPosition")

                val buildingId = feature.properties?.get("@id")?.jsonPrimitive?.content
                val buildingName = feature.properties?.get("name")?.jsonPrimitive?.content

                if(buildingName != null && buildingId != null) {
                    selectedBuilding = Building(
                        id = buildingId,
                        name = buildingName,
                        longitude = pos.longitude,
                        latitude = pos.latitude
                    )
                }

                animateCameraStateToTarget(cameraState, pos)

            }


        }
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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        jsonString?.let {
            MaplibreMap(
                modifier = Modifier.fillMaxSize(),
                cameraState = cameraState,
                options = mapOptions,
                onMapClick = { pos, offset ->
                    val features = cameraState.projection?.queryRenderedFeatures(offset)
                    if (features.isNullOrEmpty()) {
                        selectedFeature = null
                        ClickResult.Consume
                    } else {
                        ClickResult.Pass
                    }
                },
                baseStyle = BaseStyle.Json(it)
            ) {
                locationState?.let { state ->
                    LocationPuck(
                        "user-location",
                        locationState = userLocationState,
                        cameraState = cameraState
                    )
                }

                campuses.forEach { campus ->
                    CampusLayers(campus, selectedBuilding,
                        onFeatureClick = { feature ->
                            selectedFeature = feature
                        },
                        clearFeature = {
                            selectedFeature = null
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedBuilding != null,
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
            selectedBuilding?.let { building ->
                BuildingCard(
                    onDismiss = { selectedBuilding = null },
                    building = building
                )
            }
        }
    }

}

private suspend fun animateCameraStateToTarget(cameraState: CameraState, target: Position) {
    cameraState.animateTo(cameraState.position.copy(
        target = target
    ))
}
private suspend fun animateCameraStateToTargetBuilding(cameraState: CameraState, building: Building) {
    animateCameraStateToTarget(cameraState, Position(building.longitude, building.latitude))
}

@Composable
fun BuildingCard(building: Building, onDismiss: () -> Unit) {

    Card(
        modifier = Modifier.padding(10.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(building.name, color = MaterialTheme.colorScheme.onSecondary)
            IconButton(onClick = { onDismiss() }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear Feature",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun CampusLayers(
    campus: Campus,
    selectedBuilding: Building?,
    onFeatureClick: (Feature<Geometry, JsonObject?>) -> Unit,
    clearFeature: () -> Unit
) {

    val campusBuildings = rememberGeoJsonSource(GeoJsonData.Uri(Res.getUri("files/campus-geodata/${campus.jsonPath}")))

    FillExtrusionLayer(
        id = "buildings-3d-${campus.name}",
        source = campusBuildings,
        onClick = { features ->
            val hit = features.firstOrNull()
            if (hit != null) {
                onFeatureClick(hit)
                ClickResult.Consume
            } else {
                // Probably never gets called
                clearFeature()
                ClickResult.Consume
            }
        },
        height = const(10.0f),
        color = switch(
            condition(
                test = id<StringValue>().cast<StringValue>() eq const(selectedBuilding?.id ?: "").cast<StringValue>(),
                output = const(Color.Green)
            ),
            fallback = const(MaterialTheme.colorScheme.secondary)
        ),
        opacity = const(0.5f)
    )


}