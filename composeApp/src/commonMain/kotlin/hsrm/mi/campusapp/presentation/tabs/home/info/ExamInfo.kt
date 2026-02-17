package hsrm.mi.campusapp.presentation.tabs.home.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hsrm.mi.campusapp.domain.model.Exam
import kotlinx.datetime.format

@Composable
fun ExamInfo(exams: List<Exam>) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Quiz,
            contentDescription = "Exam Icon",
            modifier = Modifier.size(30.dp)
        )
        Text("Unenrolled Exams!")
        Spacer(modifier = Modifier.width(12.dp))
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp), thickness = 1.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        exams.forEach {
            Text(style = MaterialTheme.typography.bodyMedium, text = it.name)
        }
    }
}