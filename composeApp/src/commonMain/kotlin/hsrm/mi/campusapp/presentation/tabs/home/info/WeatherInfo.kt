package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.no_weather_data
import hsrm.mi.campusapp.domain.model.Weather
import hsrm.mi.campusapp.presentation.components.WeatherWidget
import hsrm.mi.campusapp.presentation.components.getWeatherIcon
import org.jetbrains.compose.resources.stringResource

@Composable
fun WeatherInfo(currentWeather: Weather?) {

    if(currentWeather == null) {
        Text(stringResource(Res.string.no_weather_data), style = MaterialTheme.typography.bodyMedium)
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            WeatherWidget(weather = currentWeather, modifier = Modifier.wrapContentWidth())
            Icon(
                imageVector = getWeatherIcon(currentWeather),
                contentDescription = "current weather",
                modifier = Modifier.size(20.dp)
            )
        }
    }

}