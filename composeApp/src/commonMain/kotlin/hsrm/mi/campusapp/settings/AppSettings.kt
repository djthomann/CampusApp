package hsrm.mi.campusapp.settings

import com.russhwolf.settings.Settings

class AppSettings {
    private val settings: Settings = Settings()

    var campus: String
        get() = settings.getString("selected_campus", "")
        set(value) = settings.putString("selected_campus", value)

    var darkMode: Boolean
        get() = settings.getBoolean("dark_mode", false)
        set(value) = settings.putBoolean("dark_mode", value)
}