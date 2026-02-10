package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface IMenuService {

    fun getMenusForCanteenAsFlow(canteen: Canteen?): Flow<List<Menu>>
    fun getMenuForDayAndCanteen(date: LocalDate, canteen: Canteen?): Flow<Menu?>
    fun getMenusWithDishesAsFlow(): Flow<List<Menu>>
    suspend fun saveMenus(menus: List<Menu>)
    suspend fun saveMenu(menu: Menu): Long
    suspend fun saveDishes(dishes: List<Dish>, menuId: Long)
    suspend fun saveDish(dish: Dish, menuId: Long): Long
    suspend fun saveSideDishes(map: Map<SideDishType, List<String>>, menuId: Long)
    suspend fun saveSideDish(name: String, type: SideDishType, menuId: Long): Long
    suspend fun deleteAllMenus()
    suspend fun deleteAllDishes()
    suspend fun deleteAllSideDishes()

}