package hsrm.mi.campusapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hsrm.mi.campusapp.data.api.ApiModule
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.getDatabaseBuilder
import hsrm.mi.campusapp.domain.persistence.getRoomDatabase
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // TODO() Implement a DI Framework, i.e. Koin

        ApiModule.init(HttpClient())

        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        val db = getRoomDatabase(getDatabaseBuilder(applicationContext), scope = applicationScope)
        DatabaseHolder.init(db)

        installSplashScreen()
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}