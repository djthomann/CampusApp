package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.TabOptions

object FoodTab: CampusTab {

    private fun readResolve(): Any = FoodTab

    override val topAppBarTitle = "Speiseplan"
    override val activeIcon: ImageVector =  Icons.Filled.Dining
    override val inactiveIcon: ImageVector =  Icons.Outlined.Dining

    override val options: TabOptions
        @Composable
        get() {
            val title = "Food"
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Der Speiseplan wird noch umgesetzt")
        }
    }

}