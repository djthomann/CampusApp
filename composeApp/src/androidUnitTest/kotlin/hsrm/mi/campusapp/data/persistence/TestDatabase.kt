package hsrm.mi.campusapp.data.persistence

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider

actual fun createTestDbBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = ApplicationProvider.getApplicationContext<Context>()

    return Room.inMemoryDatabaseBuilder(
        context,
        AppDatabase::class.java
    )
}