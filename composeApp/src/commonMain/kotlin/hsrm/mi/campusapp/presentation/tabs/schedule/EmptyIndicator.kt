package hsrm.mi.campusapp.presentation.tabs.schedule

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.no_courses_today
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyIndicator() {

    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/lottie/no-courses.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(composition)

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = "Lottie animation"
    )

    Text(stringResource(Res.string.no_courses_today), style = MaterialTheme.typography.headlineMedium)

}