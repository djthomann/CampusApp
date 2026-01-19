package hsrm.mi.campusapp.data.api.openmeteo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val current: CurrentWeather
)

@Serializable
data class CurrentWeather(

    @SerialName("temperature_2m")
    val temperature: Float,
    val rain: Float?,
    @SerialName("cloud_cover")
    val cloudCover: Int,
    @SerialName("is_day")
    private val isDayNum: Int

) {
    val isDay: Boolean get() = isDayNum == 1
}