package hsrm.mi.campusapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.app_name
import campusapp.composeapp.generated.resources.logo
import dev.datlag.kcef.KCEF
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.getDatabaseBuilder
import hsrm.mi.campusapp.domain.persistence.getRoomDatabase
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.io.File
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {

    val db = getRoomDatabase(getDatabaseBuilder())
    DatabaseHolder.init(db)

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
        alwaysOnTop = true,
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.logo)
    ) {
        App()
    }
}