package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun EsmorgaDialog(
    title: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp, start = 16.dp, top = 32.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                EsmorgaText(
                    text = title,
                    style = EsmorgaTextStyle.BODY_1,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    EsmorgaButton(
                        text = dismissButtonText,
                        onClick = {
                            onDismiss()
                        },
                        oneLine = true,
                        modifier = modifier.weight(1F),
                        primary = false
                    )
                    Spacer(Modifier.width(12.dp))
                    EsmorgaButton(
                        text = confirmButtonText,
                        modifier = modifier.weight(1F),
                        oneLine = true,
                        onClick = {
                            onConfirm()
                        }
                    )
                }
            }
        }
    }
}