package hsrm.mi.campusapp.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class CampusButtonType {
    NORMAL, SMALL
}

@Composable
fun CampusButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    buttonType: CampusButtonType = CampusButtonType.NORMAL
) {
    val modifier = if (buttonType == CampusButtonType.SMALL) modifier.height(32.dp) else modifier
    val padding = if (buttonType == CampusButtonType.SMALL) PaddingValues(4.dp) else  ButtonDefaults.ContentPadding

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        contentPadding = padding,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(text, style = if (buttonType == CampusButtonType.SMALL) MaterialTheme.typography.bodySmall else LocalTextStyle.current)
    }
}