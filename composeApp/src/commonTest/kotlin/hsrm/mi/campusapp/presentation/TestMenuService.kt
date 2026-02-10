package hsrm.mi.campusapp.presentation

import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import hsrm.mi.campusapp.domain.service.IMenuService
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class TestMenuService: IMenuService {
    override fun getMenusForCanteenAsFlow(canteen: Canteen?): Flow<List<Menu>> {
        TODO("Not yet implemented")
    }

    override fun getMenuForDayAndCanteen(
        date: LocalDate,
        canteen: Canteen?
    ): Flow<Menu?> {
        TODO("Not yet implemented")
    }

    override fun getMenusWithDishesAsFlow(): Flow<List<Menu>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMenus(menus: List<Menu>) {
        TODO("Not yet implemented")
    }

    override suspend fun saveMenu(menu: Menu): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveDishes(
        dishes: List<Dish>,
        menuId: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun saveDish(
        dish: Dish,
        menuId: Long
    ): Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveSideDishes(
        map: Map<SideDishType, List<String>>,
        menuId: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun saveSideDish(
        name: String,
        type: SideDishType,
        menuId: Long
    ): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllMenus() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllDishes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllSideDishes() {
        TODO("Not yet implemented")
    }
}