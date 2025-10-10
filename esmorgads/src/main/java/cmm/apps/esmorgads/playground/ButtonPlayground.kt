package cmm.apps.esmorgads.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.esmorgads.ui.ToggleSwitch

@Composable
fun ButtonPlayground() {
    var isPrimary by remember { mutableStateOf(true) }
    var isEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var oneLine by remember { mutableStateOf(false) }

    Column {
        ToggleSwitch(label = "Primary", checked = isPrimary, onCheckedChange = { isPrimary = it })
        Spacer(modifier = Modifier.height(8.dp))
        ToggleSwitch(label = "Enabled", checked = isEnabled, onCheckedChange = { isEnabled = it })
        Spacer(modifier = Modifier.height(8.dp))
        ToggleSwitch(label = "Loading", checked = isLoading, onCheckedChange = { isLoading = it })
        Spacer(modifier = Modifier.height(8.dp))
        ToggleSwitch(label = "One Line", checked = oneLine, onCheckedChange = { oneLine = it })

        Spacer(modifier = Modifier.height(16.dp))

        EsmorgaButton(
            text = "Test Button with Configuration",
            primary = isPrimary,
            isEnabled = isEnabled,
            isLoading = isLoading,
            oneLine = oneLine,
            onClick = { }
        )
    }
}

