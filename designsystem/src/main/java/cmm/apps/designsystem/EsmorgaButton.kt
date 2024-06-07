package cmm.apps.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaButton(text: String, primary: Boolean = true, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (primary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .fillMaxWidth(),
        onClick = { onClick() }) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }

}