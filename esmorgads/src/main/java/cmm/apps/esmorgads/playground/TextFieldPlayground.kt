package cmm.apps.esmorgads.playground

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorgads.R
import cmm.apps.esmorgads.ui.ToggleSwitch

@Composable
fun TextFieldPlayground() {
    var textValue by remember { mutableStateOf("") }
    var isSingleLine by remember { mutableStateOf(true) }
    var isEnabled by remember { mutableStateOf(true) }
    var keyboardType by remember { mutableIntStateOf(0) }
    val keyboardTypes = listOf("Text", "Password", "Email", "Number")

    Column {
        ToggleSwitch(label = "Single Line", checked = isSingleLine, onCheckedChange = { isSingleLine = it })
        Spacer(modifier = Modifier.height(8.dp))
        ToggleSwitch(label = "Enabled", checked = isEnabled, onCheckedChange = { isEnabled = it })

        Spacer(modifier = Modifier.height(8.dp))

        EsmorgaText(text = "Keyboard Type", style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            keyboardTypes.forEachIndexed { index, type ->
                Surface(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { keyboardType = index }
                        .weight(1f),
                    color = if (keyboardType == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        EsmorgaText(
                            text = type,
                            style = EsmorgaTextStyle.CAPTION
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        EsmorgaTextField(
            value = textValue,
            onValueChange = { textValue = it },
            title = R.string.app_name,
            placeholder = R.string.app_name,
            singleLine = isSingleLine,
            maxLines = if (isSingleLine) 1 else 4,
            isEnabled = isEnabled,
            keyboardType = when (keyboardType) {
                1 -> KeyboardType.Password
                2 -> KeyboardType.Email
                3 -> KeyboardType.Number
                else -> KeyboardType.Text
            }
        )
    }
}

