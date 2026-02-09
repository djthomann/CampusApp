package hsrm.mi.campusapp.data.persistence

import androidx.room.Room
import hsrm.mi.campusapp.domain.persistence.AppDatabase
import java.io.File

actual fun createTestDbBuilder(): androidx.room.RoomDatabase.Builder<hsrm.mi.campusapp.domain.persistence.AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room_test.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}