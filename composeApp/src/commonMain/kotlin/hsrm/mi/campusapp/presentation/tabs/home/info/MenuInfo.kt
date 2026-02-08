package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.menu_for_canteen
import campusapp.composeapp.generated.resources.no_menu_today
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.presentation.tabs.canteen.CanteenTab
import org.jetbrains.compose.resources.stringResource


@Composable
fun MenuInfo(canteen: Canteen?, menu: Menu?) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector =  CanteenTab.activeIcon,
                contentDescription = CanteenTab.topAppBarTitle
            )
            Text(if(canteen != null) stringResource(Res.string.menu_for_canteen, canteen.name) else CanteenTab.topAppBarTitle)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                if(menu == null) {
                    Text(stringResource(Res.string.no_menu_today), style = MaterialTheme.typography.bodyMedium)
                } else {
                    menu.dishes.sortedBy { it.price }.forEach { dish -> Text("• ${dish.name}", style = MaterialTheme.typography.bodyMedium) }
                }
            }
        }
    }
}