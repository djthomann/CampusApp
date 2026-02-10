package hsrm.mi.campusapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hsrm.mi.campusapp.data.api.ApiModule
import hsrm.mi.campusapp.data.persistence.AppDatabase
import hsrm.mi.campusapp.data.persistence.DatabaseHolder
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContent {
            KoinApplication(application = {
                androidContext(applicationContext)
                modules(

                    commonModule,
                    platformModule()
                )
            }) {
                ApiModule.init(HttpClient())

                DatabaseHolder.init(koinInject<AppDatabase>())

                App()
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}