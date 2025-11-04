package hsrm.mi.campusapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class MapScreen: Screen {

    @Composable
    override fun Content() {
        Box() {
            Text("Map")
        }
    }
}