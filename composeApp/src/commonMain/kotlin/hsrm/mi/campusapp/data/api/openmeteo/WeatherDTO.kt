package hsrm.mi.campusapp.data.api.openmeteo

import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDTO(
    @SerialName("temperature_2m")
    val temperature: Float,
    @SerialName("wind_speed_10m")
    val windSpeed: Float,
    val rain: Float?,
    @SerialName("cloud_cover")
    val cloudCover: Int,
    @SerialName("is_day")
    val isDayNum: Int

)

fun WeatherDTO.toDomain(campus: Campus): Weather {
    return Weather(
        campus = campus,
        temperature = temperature,
        windSpeed = windSpeed,
        rain = rain,
        cloudCover = cloudCover,
        isDay = isDayNum == 1
    )
}