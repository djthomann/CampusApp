package hsrm.mi.campusapp.domain.model

data class Weather(
    val campus: Campus,
    val temperature: Float, // In °Celsius
    val windSpeed: Float, // in km/h
    val rain: Float?, // in mm
    val cloudCover: Int, // in percent
    val isDay: Boolean
)