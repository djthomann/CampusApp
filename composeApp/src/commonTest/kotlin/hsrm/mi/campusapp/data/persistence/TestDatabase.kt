package hsrm.mi.campusapp.data.persistence

import androidx.room.RoomDatabase

expect fun createTestDbBuilder(): RoomDatabase.Builder<AppDatabase>