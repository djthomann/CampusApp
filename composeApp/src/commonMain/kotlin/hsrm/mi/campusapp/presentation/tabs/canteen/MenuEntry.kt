package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.domain.model.Menu

@Composable
fun MenuEntry(menu: Menu, expanded: Boolean, onClick: () -> Unit) {

    val backgroundColor by animateColorAsState(
        targetValue = if (expanded)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "backgroundColor"
    )

    val textColor = if (expanded)
        MaterialTheme.colorScheme.onSecondary
    else
        MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = if (expanded) 6.dp else 0.dp),
            ) {
                Text(menu.dateString, color = textColor)
            }
            AnimatedVisibility(
                visible = expanded
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Gerichte", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                    menu.dishes.sortedBy { dish -> dish.price }.forEachIndexed { index, dish ->
                        DishEntry(dish, index % 2 == 0)
                        HorizontalDivider(thickness = 1.dp)
                    }
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val isWide = maxWidth > 600.dp

                        if (isWide) {
                            SideDishesRow(menu.sideDishes)
                        } else {
                            SideDishesColumn(menu.sideDishes)
                        }
                    }

                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(if(!expanded) textColor else Color.Transparent).height(4.dp)
        )
    }
}