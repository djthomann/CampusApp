package hsrm.mi.campusapp.presentation.tabs.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import campusapp.composeapp.generated.resources.Res
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import dev.datlag.kcef.KCEF
import java.io.File


@Composable
actual fun MapView(mapScreenModel: MapScreenModel) {
    var initialized by remember { mutableStateOf(true) }

    if (initialized) {
        MainView(mapScreenModel) // WebView erst nach Init
    } else {
        Text("Initializing WebView...", modifier = Modifier.fillMaxSize())
    }

    DisposableEffect(Unit) {
        onDispose { KCEF.disposeBlocking() }
    }
}

@Composable
fun MainView(mapScreenModel: MapScreenModel) {
    var bytes by remember {
        mutableStateOf(ByteArray(0))
    }
    LaunchedEffect(Unit) {
        bytes = Res.readBytes("files/map.html")
    }
    val htmlContent = bytes.decodeToString()

    val tmpFile = remember {
        File.createTempFile("tmp_umap", ".html").apply {
            writeText(htmlContent)
            deleteOnExit() // optional
        }
    }
    val state = rememberWebViewState(Res.getUri("files/map.html"))
    WebView(state = state, modifier = Modifier.fillMaxSize())
}