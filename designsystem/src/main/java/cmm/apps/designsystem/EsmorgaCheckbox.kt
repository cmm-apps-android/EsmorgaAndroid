package cmm.apps.designsystem

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EsmorgaCheckbox(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChanged,
        modifier = modifier
    )
}
