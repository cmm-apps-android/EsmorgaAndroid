package cmm.apps.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaButton(text: String, modifier: Modifier = Modifier, primary: Boolean = true, onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(5.dp),
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = if (primary) Color.White else Color.Black,
            containerColor = if (primary) MaterialTheme.colorScheme.primary else Color(0xFFE8CFD6)
        ),
        onClick = { onClick() }) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }

}