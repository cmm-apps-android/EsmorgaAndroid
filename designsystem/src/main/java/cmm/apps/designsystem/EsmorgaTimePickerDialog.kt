package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsmorgaTimePickerDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    formattedTime: (Int, Int) -> String,
    confirmText: String,
    cancelText: String,
    timeState: TimePickerState
) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp, start = 16.dp, top = 32.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    state = timeState,
                    layoutType = TimePickerLayoutType.Vertical,
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EsmorgaButton(
                        text = cancelText,
                        onClick = {
                            onDismiss()
                        },
                        modifier = modifier.weight(1F),
                        primary = false
                    )
                    Spacer(Modifier.width(12.dp))
                    EsmorgaButton(
                        text = confirmText,
                        modifier = modifier.weight(1F),
                        onClick = {
                            onConfirm(formattedTime(timeState.hour, timeState.minute))
                        }
                    )
                }
            }
        }
    }
}
