package hsrm.mi.campusapp.domain.persistence.menu

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {

    @Insert
    suspend fun insert(item: DishEntity): Long

    @Query("SELECT * FROM DishEntity")
    fun getAllAsFlow(): Flow<List<DishEntity>>

    @Delete
    suspend fun deleteDish(dish: DishEntity)

    @Query("DELETE FROM DishEntity")
    suspend fun deleteAllDishes()
}