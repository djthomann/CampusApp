package hsrm.mi.campusapp.data.persistence.exam

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ExamEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ExamEntity>)

    @Query("SELECT * FROM ExamEntity WHERE id = :id")
    suspend fun getById(id: Long): ExamEntity?

    @Transaction
    @Query("SELECT * FROM ExamEntity WHERE id = :id")
    suspend fun getExamWithBuildingById(id: Long): ExamWithBuilding?

    @Query("SELECT * FROM ExamEntity")
    fun getAllAsFlow(): Flow<List<ExamEntity>>

    @Transaction
    @Query("SELECT * FROM ExamEntity")
    fun getAllWithBuildingAsFlow(): Flow<List<ExamWithBuilding>>

    @Delete
    suspend fun delete(entity: ExamEntity)

    @Query("DELETE FROM ExamEntity WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ExamEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM ExamEntity")
    suspend fun count(): Int

}