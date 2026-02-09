package hsrm.mi.campusapp.data.persistence

import androidx.room.Room
import java.io.File

actual fun createTestDbBuilder(): androidx.room.RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room_test.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}