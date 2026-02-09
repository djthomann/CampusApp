package hsrm.mi.campusapp.data.persistence

object DatabaseHolder {
    @Volatile
    private var _db: AppDatabase? = null

    val db: AppDatabase
        get() = _db ?: error(
            "DatabaseHolder: Die Datenbank wurde noch nicht initialisiert! " +
                    "Wurde DatabaseHolder.init(db) in der MainActivity (Android) " +
                    "oder main() (JVM) aufgerufen?"
        )

    /**
     * Init beim App Start
     */
    fun init(database: AppDatabase) {
        if (_db == null) {
            _db = database
        }
    }
}