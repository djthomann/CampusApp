package hsrm.mi.campusapp.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.DesktopWindows
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.LocalTime

data class Course(
    val name: String,
    val start: LocalTime,
    val end: LocalTime
)

enum class CourseType(
    val germanString: String,
    val color: Color,
    val icon: ImageVector
) {
    LECTURE("Vorlesung", Color.Green, Icons.AutoMirrored.Rounded.MenuBook), PRACTICAL("Praktikum", Color.Red, Icons.Rounded.DesktopWindows)
}

