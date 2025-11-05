package hsrm.mi.campusapp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import java.io.File

fun main() = application {

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
        title = "CampusApp",
    ) {
        App()
    }
}