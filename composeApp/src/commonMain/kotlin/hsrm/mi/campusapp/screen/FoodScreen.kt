package hsrm.mi.campusapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class FoodScreen: CampusScreen {

    override val title = "Food"
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.LightGray)
        ) {
            Text("Food")
        }
    }
}