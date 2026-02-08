package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import hsrm.mi.campusapp.domain.model.toEntity
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.menu.SideDishEntity
import hsrm.mi.campusapp.domain.persistence.menu.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object MenuService {

    private val sideDishDao = DatabaseHolder.db.getSideDishDao()
    private val dishDao = DatabaseHolder.db.getDishDao()
    private val menuDao = DatabaseHolder.db.getMenuDao()

    fun getMenusWithDishesAsFlow(): Flow<List<Menu>> {
        return menuDao.getMenusWithDishesAsFlow().map { it.map { menuWithDishes -> menuWithDishes.toDomain() } }
    }

    suspend fun saveMenu(menu: Menu): Long {

        val entity = menu.toEntity()

        return menuDao.insertMenu(entity)
    }

    suspend fun saveDish(dish: Dish, menuId: Long): Long {
        return dishDao.insert(dish.toEntity(menuId))
    }

    suspend fun saveSideDish(name: String, type: SideDishType, menuId: Long): Long {
        return sideDishDao.insert(SideDishEntity(
            menuId = menuId,
            type = type,
            name = name
        ))
    }

}