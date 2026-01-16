package hsrm.mi.campusapp.presentation.theme

import androidx.compose.material3.NavigationBarItemColors
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

val OnDarkBackground = Color.White
val DarkSurface = Color(0xFF2A2A2A)

val DarkPrimary = Color(0xff455dff)
val OnDarkPrimary = Color.White

val DarkSecondary = Color(0x0088b6ff)

val OnDarkSecondary = Color.White

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
    primary = DarkPrimary,
    onPrimary = OnDarkPrimary,
    secondary = DarkSecondary,
    onSecondary = OnDarkSecondary,
    background = DarkBackground,
    onBackground = OnDarkBackground,
    surface = DarkSurface,
    onSurface = WhitePrimary,
)

internal val NavigationBarItemColors = NavigationBarItemColors(
    selectedIconColor = OnDarkPrimary,
    selectedTextColor = DarkPrimary,
    selectedIndicatorColor = DarkPrimary,
    unselectedIconColor = OnDarkBackground,
    unselectedTextColor = OnDarkBackground,
    disabledIconColor = DarkSurface,
    disabledTextColor = DarkSurface
)