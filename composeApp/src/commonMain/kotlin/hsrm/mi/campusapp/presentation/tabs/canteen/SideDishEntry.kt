package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.domain.model.SideDishType

@Composable
fun SideDishesRow(sideDishes: Map<SideDishType, List<String>>) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column {
                Text("Beilagen", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                sideDishes[SideDishType.GARNISH]?.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column {
                Text("Salate", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                sideDishes[SideDishType.SALAD]?.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column {
                Text("Dessert", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                sideDishes[SideDishType.DESSERT]?.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
        }
    }
}

@Composable
fun SideDishesColumn(sideDishes: Map<SideDishType, List<String>>) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Column {
            Text("Beilagen", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
            sideDishes[SideDishType.GARNISH]?.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
        Column {
            Text("Salate", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
            sideDishes[SideDishType.SALAD]?.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
        Column {
            Text("Dessert", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
            sideDishes[SideDishType.DESSERT]?.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
    }

}