package cmm.apps.esmorgads.playground

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaDatePicker
import cmm.apps.designsystem.EsmorgaDialog
import java.util.Locale

@Composable
fun DialogPlayground() {
    var showDialog by remember { mutableStateOf(false) }

    EsmorgaButton(
        text = "Open Dialog",
        onClick = { showDialog = true }
    )

    if (showDialog) {
        EsmorgaDialog(
            title = "Example Dialog",
            confirmButtonText = "Confirm",
            dismissButtonText = "Cancel",
            onConfirm = { showDialog = false },
            onDismiss = { showDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerPlayground() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = remember { DatePickerState(locale = Locale.getDefault()) }

    EsmorgaButton(
        text = if (showDatePicker) "Hide Date Picker" else "Show Date Picker",
        onClick = { showDatePicker = !showDatePicker }
    )

    if (showDatePicker) {
        Spacer(modifier = Modifier.height(16.dp))
        EsmorgaDatePicker(state = datePickerState)
    }
}

