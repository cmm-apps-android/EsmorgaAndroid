package cmm.apps.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class ButtonUiState()
data class Enabled(val text: String) : ButtonUiState()
data class Disabled(val text: String) : ButtonUiState()
data object Loading : ButtonUiState()

@Composable
fun EsmorgaButton(
    state: ButtonUiState,
    modifier: Modifier = Modifier,
    primary: Boolean = true,
    oneLine: Boolean = false,
    onClick: () -> Unit
) {
    val isEnabled = state is Enabled
    val isLoading = state is Loading
    val text = (state as? Enabled)?.text ?: (state as? Disabled)?.text ?: ""

    Button(
        shape = RoundedCornerShape(5.dp),
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = if (primary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
            containerColor = if (primary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        enabled = isEnabled || isLoading,
        onClick = {
            if (!isLoading) {
                onClick()
            }
        }) {
        if (isLoading) {
            EsmorgaCircularLoader(modifier = Modifier.size(24.dp))
        } else {
            EsmorgaText(text = text, style = EsmorgaTextStyle.BUTTON, maxLines = if (oneLine) 1 else Int.MAX_VALUE)
        }
    }

}