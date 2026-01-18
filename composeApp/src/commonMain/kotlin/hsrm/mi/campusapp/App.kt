package hsrm.mi.campusapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import hsrm.mi.campusapp.presentation.tabs.HomeTab
import hsrm.mi.campusapp.presentation.theme.AppTypography
import hsrm.mi.campusapp.presentation.theme.DarkColorScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun App() {

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
    ) {
        TabNavigator(HomeTab) {
            navigator ->
            MainScaffold(navigator)
        }
    }
}

@Composable
expect fun MainScaffold(navigator: TabNavigator)