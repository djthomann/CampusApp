package hsrm.mi.campusapp.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

/* Setup nested navigation with this Screen instead of SettingsTab */
class SettingsScreen: Screen {
    @Composable
    override fun Content() {
        Text("Settings")
    }
}