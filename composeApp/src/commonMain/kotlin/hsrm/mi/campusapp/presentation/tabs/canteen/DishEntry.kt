package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.domain.model.Dish

@Composable
fun DishEntry(dish: Dish, isEven: Boolean) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    val backgroundColor = if(isEven) Color.Transparent else Color.DarkGray // TODO() Get rid of this

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(1f), text = dish.name, style = MaterialTheme.typography.bodyMedium, color = textColor)
        Text(modifier = Modifier.wrapContentWidth(), text = dish.price, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
    }
}