package hsrm.mi.campusapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import dev.datlag.kcef.KCEF
import org.jetbrains.compose.resources.stringResource
import java.io.File
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {

    Locale.setDefault(Locale.GERMANY) /* For testing only */

    LaunchedEffect(Unit) {
        try {
            KCEF.initBlocking(builder = {
                installDir(File("kcef-bundle")) // optional
                settings {
                    cachePath = File("kcef-cache").absolutePath
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
    ) {
        App()
    }
}