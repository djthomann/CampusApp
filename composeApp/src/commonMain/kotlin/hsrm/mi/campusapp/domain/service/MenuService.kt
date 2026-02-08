package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import hsrm.mi.campusapp.domain.model.toEntity
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.menu.SideDishEntity
import hsrm.mi.campusapp.domain.persistence.menu.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

object MenuService {

    private val sideDishDao = DatabaseHolder.db.getSideDishDao()
    private val dishDao = DatabaseHolder.db.getDishDao()
    private val menuDao = DatabaseHolder.db.getMenuDao()

    private val allMenusFlow = menuDao.getMenusWithDishesAsFlow()
        .map { entities -> entities.map { it.toDomain() }}
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getMenusForCanteenAsFlow(canteen: Canteen?): Flow<List<Menu>> {
        return allMenusFlow.map { menus ->
            menus.filter { menu ->
                menu.canteen == canteen?.name
            }
        }

    }

    fun getMenuForDayAndCanteen(date: LocalDate, canteen: Canteen?): Flow<Menu?> {
        return getMenusForCanteenAsFlow(canteen).map { menus ->
            menus.find { menu ->
                menu.date.toString() == "2026-02-09"
            }
        }
    }

    fun getMenusWithDishesAsFlow(): Flow<List<Menu>> {
        return menuDao.getMenusWithDishesAsFlow().map { it.map { menuWithDishes -> menuWithDishes.toDomain() } }
    }

    suspend fun saveMenus(menus: List<Menu>) {
        menus.forEach { saveMenu(it) }
    }
    suspend fun saveMenu(menu: Menu): Long {

        val entity = menu.toEntity()
        val menuId = menuDao.insertMenu(entity)

        saveDishes(menu.dishes, menuId)

        saveSideDishes(menu.sideDishes, menuId)

        return menuId
    }

    suspend fun saveDishes(dishes: List<Dish>, menuId: Long) {
        dishes.forEach { saveDish(it, menuId) }
    }

    suspend fun saveDish(dish: Dish, menuId: Long): Long {
        return dishDao.insert(dish.toEntity(menuId))
    }

    suspend fun saveSideDishes(map: Map<SideDishType, List<String>>, menuId: Long) {
        map.forEach { (sideDishType, names) ->
            names.forEach { name -> saveSideDish(name, sideDishType, menuId) }
        }
    }

    suspend fun saveSideDish(name: String, type: SideDishType, menuId: Long): Long {
        return sideDishDao.insert(SideDishEntity(
            menuId = menuId,
            type = type,
            name = name
        ))
    }

    suspend fun deleteAllMenus() {
        menuDao.deleteAllMenus()
    }

    suspend fun deleteAllDishes() {
        dishDao.deleteAllDishes()
    }

    suspend fun deleteAllSideDishes() {
        sideDishDao.deleteAllSideDishes()
    }

}