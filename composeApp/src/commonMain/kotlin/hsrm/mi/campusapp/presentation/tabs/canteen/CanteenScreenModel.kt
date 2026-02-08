package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
import hsrm.mi.campusapp.data.api.canteenapi.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.DishEntity
import hsrm.mi.campusapp.domain.persistence.MenuEntity
import hsrm.mi.campusapp.domain.persistence.SideDishEntity
import hsrm.mi.campusapp.domain.service.CanteenService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class CanteenScreenModel: ScreenModel {

    private val sideDishDao = DatabaseHolder.db.getSideDishDao()
    private val dishDao = DatabaseHolder.db.getDishDao()
    private val menuDao = DatabaseHolder.db.getMenuDao()

    var menus by mutableStateOf<List<Menu>>(emptyList())
        private set

    var selectedCanteen by mutableStateOf<Canteen?>(null)
    val canteens: StateFlow<List<Canteen>> = CanteenService.getAllCanteens().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalTime::class)
    suspend fun loadMenuFromAPI(canteen: Canteen): List<Menu> {
        val result = CanteenAPI.getMenusForWeek(canteen, LocalDate.now())
        return result.map { it.toDomain() }
    }

    fun saveMenus(menus: List<Menu>) {

        println("SAVING MENUS")

        screenModelScope.launch(Dispatchers.IO) {
            menus.forEach { menu ->
                val newMenu = MenuEntity(
                    canteen = menu.canteen,
                    date = menu.date.toString(),
                    dateString = menu.dateString
                )
                println("INSERTING MENU $newMenu")
                val menuId = menuDao.insertMenu(newMenu)
                menu.dishes.forEach { dish ->
                    val newDish = DishEntity(
                        name = dish.name,
                        menuId = menuId,
                        description = dish.description,
                        price = dish.price,
                        dishAllergens = dish.dishAllergens
                    )
                    dishDao.insert(newDish)
                }
                menu.sideDishes.forEach { entry ->
                    entry.value.forEach { sideDish ->
                        val newSideDish = SideDishEntity(
                            menuId = menuId,
                            type = entry.key,
                            name = sideDish
                        )
                        sideDishDao.insert(newSideDish)
                    }

                }
            }
        }
    }

    fun loadMenusWithDishes(canteen: Canteen) {
        screenModelScope.launch {
            menuDao.getMenusWithDishesAsFlow().collect { loadedMenus ->
                println("LOADED FROM DB:$loadedMenus")
                menus = loadedMenus.map { menuWithDishesEntity -> Menu(
                    canteen = menuWithDishesEntity.menu.canteen,
                    date = LocalDate.parse(menuWithDishesEntity.menu.date),
                    dateString = menuWithDishesEntity.menu.dateString,
                    dishes = menuWithDishesEntity.dishes.map { dishEntity ->
                        Dish(
                            name = dishEntity.name,
                            description = dishEntity.description,
                            price = dishEntity.price,
                            dishAllergens = dishEntity.dishAllergens
                        )
                    },
                    sideDishes = menuWithDishesEntity.sideDishes.groupBy { it.type }.mapValues { (_, entities) -> entities.map { it.name } }
                )
                }.filter { menu -> menu.canteen == canteen.name }

            }
        }
    }

    // TODO() Faulty logic probably
    fun loadMenu(canteen: Canteen) {

        selectedCanteen = canteen
        // loadMenuFromAPI(canteen)

        println("LOADING MENUS FOR: ${canteen.name}")

        screenModelScope.launch {
            // Lade aus DB
            loadMenusWithDishes(canteen)

            // Wenn nichts im DB, lade von API
            if (menus.isEmpty()) {
                println("Try API for loading menus")
                val loadedMenus = loadMenuFromAPI(canteen)  // suspend, wartet jetzt
                saveMenus(loadedMenus)                       // wartet ebenfalls
                menus = loadedMenus
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

}     // TODO() CLEAN THIS UP!!!!