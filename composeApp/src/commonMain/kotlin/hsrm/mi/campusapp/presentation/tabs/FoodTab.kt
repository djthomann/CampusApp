package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.food_tab_title
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
import hsrm.mi.campusapp.data.api.canteenapi.toDomain
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.DishEntity
import hsrm.mi.campusapp.domain.persistence.MenuEntity
import hsrm.mi.campusapp.presentation.components.CampusButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString
import kotlin.time.ExperimentalTime

class FoodScreenModel: ScreenModel {

    private val dishDao = DatabaseHolder.db.getDishDao()
    private val menuDao = DatabaseHolder.db.getMenuDao()

    var menus by mutableStateOf<List<Menu>>(emptyList())
        private set

    @OptIn(ExperimentalTime::class)
    fun loadMenu() {
        screenModelScope.launch {
            val result = CanteenAPI.getMenusForWeek(LocalDate.now())
            menus = result.map { it.toDomain() }
        }
    }

    fun saveMenus() {
        screenModelScope.launch(Dispatchers.IO) {
            menus.forEach { menu ->
                val newMenu = MenuEntity(
                    date = menu.date.toString(),
                    dateString = menu.dateString
                )
                menuDao.insertMenu(newMenu)
                menu.dishes.forEach { dish ->
                    val newDish = DishEntity(
                        name = dish.name,
                        menuId = menu.date.toString(),
                        description = dish.description,
                        price = dish.price,
                        dishAllergens = dish.dishAllergens
                    )
                    dishDao.insert(newDish)
                }
            }
        }
    }

    fun loadMenusWithDishes() {
        screenModelScope.launch {
            menuDao.getMenusWithDishesAsFlow().collect { loadedMenus ->
                print("LOADED:$loadedMenus")
                menus = loadedMenus.map { menuWithDishesEntity -> Menu(
                        date = LocalDate.parse(menuWithDishesEntity.menu.date),
                        dateString = menuWithDishesEntity.menu.dateString,
                        dishes = menuWithDishesEntity.dishes.map { dishEntity ->
                            Dish(
                                name = dishEntity.name,
                                description = dishEntity.description,
                                price = dishEntity.price,
                                dishAllergens = dishEntity.dishAllergens
                            )
                        }
                    )
                }

            }
        }
    }

    fun clearMenus() {
        screenModelScope.launch {
            menuDao.deleteAllMenus()
        }
    }

    fun clearDishes() {
        screenModelScope.launch {
            dishDao.deleteAllDishes()
        }
    }

}

object FoodTab: CampusTab {

    private fun readResolve(): Any = FoodTab

    override val topAppBarTitle = runBlocking { getString(Res.string.food_tab_title) }
    override val activeIcon: ImageVector =  Icons.Filled.Dining
    override val inactiveIcon: ImageVector =  Icons.Outlined.Dining

    override val options: TabOptions
        @Composable
        get() {
            val title = topAppBarTitle
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { FoodScreenModel() }
        val menus = screenModel.menus
        val expandedMenu = remember { mutableStateOf<Menu?>(null) }

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampusButton(
                    text = "Plan holen",
                    onClick = {
                        screenModel.loadMenu()
                    }
                )
                CampusButton(
                    text = "Save Menus",
                    onClick = {
                        val dish = menus[0].dishes[0]
                        println("SAVING DISH: $dish")
                        screenModel.saveMenus()
                    }
                )
                CampusButton(
                    text = "Get Menus",
                    onClick = {
                        screenModel.loadMenusWithDishes()
                    }
                )
                CampusButton(
                    text = "Clear All",
                    onClick = {
                        screenModel.clearMenus()
                        screenModel.clearDishes()
                    }
                )
            }
            Spacer(
                modifier = Modifier.padding(5.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(menus) { MenuEntry(it, expanded = expandedMenu.value == it, onClick = {
                    if (expandedMenu.value != it) {
                        expandedMenu.value = it
                    } else {
                        expandedMenu.value = null
                    }

                }) }
            }
        }
    }
}

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
                    menu.dishes.sortedBy { dish -> dish.price }.forEachIndexed { index, dish ->
                        DishEntry(dish, index % 2 == 0)
                        HorizontalDivider(thickness = 1.dp)
                    }

                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(if(!expanded) textColor else Color.Transparent).height(4.dp)
        )
    }
}

@Composable
fun DishEntry(dish: Dish, isEven: Boolean) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    val backgroundColor = if(isEven) Color.Transparent else Color.DarkGray

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(dish.name, style = MaterialTheme.typography.bodyMedium, color = textColor)
        Text(dish.price, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
    }
}