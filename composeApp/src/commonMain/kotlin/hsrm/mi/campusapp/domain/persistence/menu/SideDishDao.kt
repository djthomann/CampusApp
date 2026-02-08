package hsrm.mi.campusapp.domain.persistence.menu

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SideDishDao {

    @Insert
    suspend fun insert(item: SideDishEntity): Long

    @Query("SELECT * FROM SideDishEntity")
    fun getAllAsFlow(): Flow<List<SideDishEntity>>

    @Delete
    suspend fun deleteSideDish(dish: SideDishEntity)

    @Query("DELETE FROM SideDishEntity")
    suspend fun deleteAllSideDishes()
}