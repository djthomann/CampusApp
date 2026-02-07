package hsrm.mi.campusapp.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.DesktopWindows
import androidx.compose.material.icons.rounded.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.course_type_extra
import campusapp.composeapp.generated.resources.course_type_lecture
import campusapp.composeapp.generated.resources.course_type_practical
import org.jetbrains.compose.resources.StringResource

enum class CourseType(
    val nameResource: StringResource,
    val color: Color,
    val icon: ImageVector
) {
    LECTURE(Res.string.course_type_lecture, Color.Green, Icons.AutoMirrored.Rounded.MenuBook),
    PRACTICAL(Res.string.course_type_practical, Color.Red, Icons.Rounded.DesktopWindows),

    EXTRA_CURRICULAR(Res.string.course_type_extra, Color.White, Icons.Rounded.Star)
}