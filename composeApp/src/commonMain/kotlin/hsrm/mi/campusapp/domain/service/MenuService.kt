package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.data.persistence.menu.DishDao
import hsrm.mi.campusapp.data.persistence.menu.MenuDao
import hsrm.mi.campusapp.data.persistence.menu.SideDishDao
import hsrm.mi.campusapp.data.persistence.menu.SideDishEntity
import hsrm.mi.campusapp.data.persistence.menu.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import hsrm.mi.campusapp.domain.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

class MenuService(
    private val menuDao: MenuDao,
    private val dishDao: DishDao,
    private val sideDishDao: SideDishDao
): IMenuService {

    private val allMenusFlow = menuDao.getMenusWithDishesAsFlow()
        .map { entities -> entities.map { it.toDomain() }}
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun getMenusForCanteenAsFlow(canteen: Canteen?): Flow<List<Menu>> {
        return allMenusFlow.map { menus ->
            menus.filter { menu ->
                menu.canteen == canteen?.name
            }
        }

    }

    override fun getMenuForDayAndCanteen(date: LocalDate, canteen: Canteen?): Flow<Menu?> {
        return getMenusForCanteenAsFlow(canteen).map { menus ->
            menus.find { menu ->
                menu.date == date
            }
        }
    }

    override fun getMenusWithDishesAsFlow(): Flow<List<Menu>> {
        return menuDao.getMenusWithDishesAsFlow().map { it.map { menuWithDishes -> menuWithDishes.toDomain() } }
    }

    override suspend fun saveMenus(menus: List<Menu>) {
        menus.forEach { saveMenu(it) }
    }
    override suspend fun saveMenu(menu: Menu): Long {

        val entity = menu.toEntity()
        val menuId = menuDao.insertMenu(entity)

        saveDishes(menu.dishes, menuId)

        saveSideDishes(menu.sideDishes, menuId)

        return menuId
    }

    override suspend fun saveDishes(dishes: List<Dish>, menuId: Long) {
        dishes.forEach { saveDish(it, menuId) }
    }

    override suspend fun saveDish(dish: Dish, menuId: Long): Long {
        return dishDao.insert(dish.toEntity(menuId))
    }

    override suspend fun saveSideDishes(map: Map<SideDishType, List<String>>, menuId: Long) {
        map.forEach { (sideDishType, names) ->
            names.forEach { name -> saveSideDish(name, sideDishType, menuId) }
        }
    }

    override suspend fun saveSideDish(name: String, type: SideDishType, menuId: Long): Long {
        return sideDishDao.insert(SideDishEntity(
            menuId = menuId,
            type = type,
            name = name
        ))
    }

    override suspend fun deleteAllMenus() {
        menuDao.deleteAllMenus()
    }

    override suspend fun deleteAllDishes() {
        dishDao.deleteAllDishes()
    }

    override suspend fun deleteAllSideDishes() {
        sideDishDao.deleteAllSideDishes()
    }

}