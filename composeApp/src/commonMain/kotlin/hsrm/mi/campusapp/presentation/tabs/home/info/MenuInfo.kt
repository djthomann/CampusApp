package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.no_menu_today
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.presentation.tabs.canteen.CanteenTab
import org.jetbrains.compose.resources.stringResource


@Composable
fun MenuInfo(canteen: Canteen?, menu: Menu?) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector =  CanteenTab.inactiveIcon,
                contentDescription = CanteenTab.topAppBarTitle,
                modifier = Modifier.size(30.dp)
            )
            Text(canteen?.name ?: CanteenTab.topAppBarTitle)
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp), thickness = 1.dp)
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