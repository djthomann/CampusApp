package hsrm.mi.campusapp.data.persistence

import androidx.room.RoomDatabase
import hsrm.mi.campusapp.domain.persistence.AppDatabase

expect fun createTestDbBuilder(): RoomDatabase.Builder<AppDatabase>