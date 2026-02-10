package hsrm.mi.campusapp.presentation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.domain.service.ICanteenService
import hsrm.mi.campusapp.domain.service.IMenuService
import hsrm.mi.campusapp.domain.service.IStopService
import hsrm.mi.campusapp.presentation.state.AppState
import hsrm.mi.campusapp.presentation.tabs.settings.SettingsTab
import hsrm.mi.campusapp.settings.AppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.Test
import kotlin.test.assertTrue

class SettingsTabTests {

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            modules(
                module {
                    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
                    single { AppSettings() }
                    single<AppState> { AppState(get(), get(), get(), get(), get()) }
                    single<ICanteenService> { TestCanteenService() }
                    single<ICampusService> { TestCampusService() }
                    single<IMenuService> { TestMenuService() }
                    single<IStopService> { TestStopService() }
                }
            )
        }
    }


    @Test
    @OptIn(ExperimentalTestApi::class)
    fun testToggleDarkMode() = runComposeUiTest {

        val koin = org.koin.mp.KoinPlatformTools.defaultContext().get()
        val appState = koin.get<AppState>()

        val lifecycleOwner = object : LifecycleOwner {
            override val lifecycle: Lifecycle = LifecycleRegistry.createUnsafe(this).apply {
                currentState = Lifecycle.State.RESUMED
            }
        }

        setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                TabNavigator(SettingsTab) {
                    CurrentTab()
                }
            }
        }
        val modeBefore = appState.isDarkMode.value

        onNodeWithTag("darkModeSwitch").performClick()

        val modeAfter = appState.isDarkMode.value

        assertTrue { modeAfter != modeBefore }

    }


    @Test
    @OptIn(ExperimentalTestApi::class)
    fun testNavigateToHomeTab() = runComposeUiTest {

    }

}