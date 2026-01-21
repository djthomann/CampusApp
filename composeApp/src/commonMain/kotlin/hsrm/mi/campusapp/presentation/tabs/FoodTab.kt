package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.food_tab_title
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
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
            menus = result
        }
    }

    fun saveMenus() {
        screenModelScope.launch(Dispatchers.IO) {
            menus.forEach { menu ->
                val newMenu = MenuEntity(
                    date = menu.date
                )
                menuDao.insertMenu(newMenu)
                menu.dishes.forEach { dish ->
                    val newDish = DishEntity(
                        name = dish.name,
                        menuId = menu.date,
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
                        date = menuWithDishesEntity.menu.date,
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
            menus.forEach { MenuEntry(it) }
        }
    }
}

@Composable
fun MenuEntry(menu: Menu) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(menu.date)
        menu.dishes.forEach { dish -> DishEntry(dish) }
    }
}

@Composable
fun DishEntry(dish: Dish) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(dish.name, style = MaterialTheme.typography.bodyMedium)
    }
}