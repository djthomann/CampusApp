package hsrm.mi.campusapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbCloudy
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.data.api.openmeteo.CurrentWeather

fun getWeatherIcon(weather: CurrentWeather): ImageVector {
    val isCloudy = weather.cloudCover >= 50
    val isRaining = weather.rain?.let { it > 0.0 } ?: false
    val isDay = weather.isDay

    return when {
        isRaining -> Icons.Filled.Umbrella // Rain

        isCloudy -> {
            if (isDay) Icons.Outlined.WbCloudy // Cloudy day
            else Icons.Outlined.NightsStay // Cloudy night
        }

        isDay -> Icons.Outlined.WbSunny // Sunny day

        else -> Icons.Outlined.Bedtime // Clear night
    }
}

@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier,
    weather: CurrentWeather
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(modifier = Modifier.size(30.dp),imageVector = getWeatherIcon(weather), contentDescription = "Current Weather Icon")
        Spacer(modifier = Modifier.width(12.dp))
        Text("${weather.temperature.toInt()} °C")
        Spacer(modifier = Modifier.width(12.dp))
        Icon(modifier = Modifier.size(30.dp), imageVector = Icons.Outlined.Air, contentDescription = "Wind Icon")
        Text("${weather.windSpeed} km/h")
    }
}