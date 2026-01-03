package hsrm.mi.campusapp.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import dev.datlag.kcef.KCEF
import hsrm.mi.campusapp.presentation.state.MapState
import java.io.File

@Composable
actual fun MapView(state: MapState) {
    var initialized by remember { mutableStateOf(true) }

    if (initialized) {
        MainView(state) // WebView erst nach Init
    } else {
        Text("Initializing WebView...", modifier = Modifier.fillMaxSize())
    }

    DisposableEffect(Unit) {
        onDispose { KCEF.disposeBlocking() }
    }
}

@Composable
fun MainView(state: MapState) {
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>MapLibre Demo</title>
            <link href="https://unpkg.com/maplibre-gl@2.4.0/dist/maplibre-gl.css" rel="stylesheet"/>
            <script src="https://unpkg.com/maplibre-gl@2.4.0/dist/maplibre-gl.js"></script>
            <style>
                body, html { margin: 0; padding: 0; height: 100%; }
                #map { width: 100%; height: 100%; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                const map = new maplibregl.Map({
                    container: 'map',
                    style: 'https://tiles.openfreemap.org/styles/liberty',
                    center: [8.217, 50.0964],
                    zoom: 16,
                    pitch: 45,
                    bearing: -17.6
                });

                map.addControl(new maplibregl.NavigationControl());

                map.on('load', () => {
                    map.addLayer({
                        id: '3d-buildings',
                        source: 'osm-buildings',
                        'source-layer': 'building',
                        type: 'fill-extrusion',
                        filter: ['==', 'extrude', 'true'],
                        minzoom: 15,
                        paint: {
                            'fill-extrusion-color': '#aaa',
                            'fill-extrusion-height': ['get', 'height'],
                            'fill-extrusion-base': ['get', 'min_height'],
                            'fill-extrusion-opacity': 0.6
                        }
                    });
                });
            </script>
        </body>
        </html>
    """.trimIndent()

    val tmpFile = File("tmp_umap.html").apply { writeText(htmlContent) }
    val state = rememberWebViewState(tmpFile.absoluteFile.toURI().toString())
    WebView(state = state, modifier = Modifier.fillMaxSize())
}
