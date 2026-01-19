package hsrm.mi.campusapp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
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
fun AnimatedTabContent(
    navigator: TabNavigator
) {
    AnimatedContent(
        targetState = navigator.current,
        transitionSpec = {
            (fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.99f))
                .togetherWith(fadeOut(animationSpec = tween(200)))
        },
        label = "TabTransition"
    ) { targetTab ->

        navigator.saveableState(key = "tab_${targetTab.options.index}", targetTab) {
            targetTab.Content()
        }
    }
}

@Composable
expect fun MainScaffold(navigator: TabNavigator)