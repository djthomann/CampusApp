package hsrm.mi.campusapp.settings

import com.russhwolf.settings.Settings

class AppSettings {
    private val settings: Settings = Settings()

    enum class SettingsKey() {
        SELECTED_CAMPUS,
        SELECTED_CANTEEN,
        IS_DARK_MODE
    }

    var campus: String
        get() = settings.getString(SettingsKey.SELECTED_CAMPUS.name, "")
        set(value) = settings.putString(SettingsKey.SELECTED_CAMPUS.name, value)

    var canteen: String
        get() = settings.getString(SettingsKey.SELECTED_CANTEEN.name, "")
        set(value) = settings.putString(SettingsKey.SELECTED_CANTEEN.name, value)

    var isDarkMode: Boolean
        get() = settings.getBoolean(SettingsKey.IS_DARK_MODE.name, false)
        set(value) = settings.putBoolean(SettingsKey.IS_DARK_MODE.name, value)
}