package hsrm.mi.campusapp.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Theme Colors
val DarkGrayPrimary = Color(0xFF4A4A4A)
val LightGrayButton = Color(0xFFB0B0B0)
val LightBackground = Color(0xFFF8F8F8)
val LightSurface = Color(0xFFFFFFFF)
val LightTextColor = Color(0xFF333333)

// Dark Theme Colors
val WhitePrimary = Color(0xFFFFFFFF)

val WhiteSecondary = Color(0x00455dff)
val DarkGrayButton = Color(0xFF4A4A4A)
val DarkBackground = Color(0xFF1C1C1C)
val DarkSurface = Color(0xFF2A2A2A)

internal val LightColorScheme = lightColorScheme(
    primary = DarkGrayPrimary,
    background = LightBackground,
    onBackground = LightTextColor,
    surface = LightSurface,
    onSurface = DarkGrayPrimary,
    secondary = LightGrayButton,
    onSecondary = LightTextColor
)

internal val DarkColorScheme = darkColorScheme(
    primary = Color(0xff455dff),
    onPrimary = Color.White,
    secondary = Color(0x0088b6ff),
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = WhitePrimary,
)