package hsrm.mi.campusapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class FoodScreen: CampusScreen {

    override val title = "Food"
    @Composable
    override fun Content() {
        Box() {
            Text("Food")
        }
    }
}