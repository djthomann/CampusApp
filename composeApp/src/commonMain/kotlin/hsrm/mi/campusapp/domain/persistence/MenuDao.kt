package hsrm.mi.campusapp.domain.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuEntity): Long

    @Query("SELECT * FROM MenuEntity WHERE date = :day")
    fun getMenuForDay(day: String): Flow<MenuWithDishes?>
    @Transaction
    @Query("SELECT * FROM MenuEntity")
    fun getMenusWithDishesAsFlow(): Flow<List<MenuWithDishes>>

    @Query("SELECT * FROM MenuEntity")
    fun getAllAsFlow(): Flow<List<MenuEntity>>

    @Delete
    suspend fun deleteMenu(menu: MenuEntity)

    @Query("DELETE FROM MenuEntity")
    suspend fun deleteAllMenus()

}