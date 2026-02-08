package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
import hsrm.mi.campusapp.data.api.canteenapi.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.domain.service.MenuService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class CanteenScreenModel: ScreenModel {

    var selectedCanteen by mutableStateOf<Canteen?>(null)
    val canteens: StateFlow<List<Canteen>> = CanteenService.getAllCanteens().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val menus: StateFlow<List<Menu>> = snapshotFlow { selectedCanteen }
        .flatMapLatest { canteen ->
            if (canteen == null) flowOf(emptyList())
            else MenuService.getMenusWithDishesAsFlow().map { list ->
                list.filter { it.canteen == canteen.name }
            }
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalTime::class)
    private suspend fun loadMenuFromAPI(canteen: Canteen): List<Menu> {
        return CanteenAPI.getMenusForWeek(canteen, LocalDate.now()).map { it.toDomain() }
    }

    fun saveMenus(menus: List<Menu>) {

        println("SAVING MENUS")

        screenModelScope.launch(Dispatchers.IO) {
            // Move this logic to MenuService
            menus.forEach { menu ->
                println("INSERTING MENU $menu")
                val menuId = MenuService.saveMenu(menu)
                menu.dishes.forEach { dish ->
                    MenuService.saveDish(dish, menuId)
                }
                menu.sideDishes.forEach { entry ->
                    entry.value.forEach { sideDish ->
                        MenuService.saveSideDish(sideDish, entry.key, menuId)
                    }
                }
            }
        }
    }

    // TODO() Faulty logic probably
    fun loadMenu(canteen: Canteen) {

        println("LOADING MENUS FOR $canteen")
        selectedCanteen = canteen

        screenModelScope.launch {
            // Wir warten kurz, bis der DB-Flow oben (menus) den ersten Wert geliefert hat
            // Falls die DB für diese Mensa leer ist -> API Call
            if (menus.value.isEmpty()) {
                println("Try API for loading menus for ${canteen.name}")
                try {
                    val loadedMenus = loadMenuFromAPI(canteen)
                    println("LOADED MENUS: $loadedMenus")
                    saveMenus(loadedMenus)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}     // TODO() CLEAN THIS UP!!!!