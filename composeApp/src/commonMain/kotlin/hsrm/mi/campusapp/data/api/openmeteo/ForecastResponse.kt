package hsrm.mi.campusapp.data.api.openmeteo

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val current: WeatherDTO
)