package hsrm.mi.campusapp.presentation.tabs.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/* https://medium.com/androiddevelopers/animating-brush-text-coloring-in-compose-%EF%B8%8F-26ae99d9b402 */
@Composable
fun AnimatedBrushText(modifier: Modifier = Modifier, text: String, fontSize: TextUnit = 60.sp, fontWeight: FontWeight = FontWeight.ExtraBold,) {
    val infiniteTransition = rememberInfiniteTransition()

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
        )
    )

    val brush = remember(offset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * offset
                val heightOffset = size.height * offset
                return LinearGradientShader(
                    colors = listOf(
                        Color(0xff28d400),
                        Color(0xff126100),
                        Color(0xff47b32e)
                    ),
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }

    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            brush = brush
        )
    )
}