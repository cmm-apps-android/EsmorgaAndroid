package cmm.apps.esmorgads.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.esmorgads.R

@Composable
fun ButtonShowcase() {
    EsmorgaButton(
        text = "Primary Button",
        primary = true,
        onClick = { }
    )
    Spacer(modifier = Modifier.height(8.dp))
    EsmorgaButton(
        text = "Secondary Button",
        primary = false,
        onClick = { }
    )
    Spacer(modifier = Modifier.height(8.dp))
    EsmorgaButton(
        text = "Disabled Button",
        isEnabled = false,
        onClick = { }
    )
    Spacer(modifier = Modifier.height(8.dp))
    var isLoading by remember { mutableStateOf(false) }
    EsmorgaButton(
        text = "Toggle Loading",
        isLoading = isLoading,
        onClick = { isLoading = !isLoading }
    )
}

@Composable
fun TextFieldShowcase() {
    var textValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var multilineValue by remember { mutableStateOf("") }

    EsmorgaTextField(
        value = textValue,
        onValueChange = { textValue = it },
        title = R.string.app_name,
        placeholder = R.string.app_name,
        imeAction = ImeAction.Next
    )
    Spacer(modifier = Modifier.height(16.dp))
    EsmorgaTextField(
        value = passwordValue,
        onValueChange = { passwordValue = it },
        title = R.string.app_name,
        placeholder = R.string.app_name,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    )
    Spacer(modifier = Modifier.height(16.dp))
    EsmorgaTextField(
        value = multilineValue,
        onValueChange = { multilineValue = it },
        title = R.string.app_name,
        placeholder = R.string.app_name,
        singleLine = false,
        maxLines = 4,
        maxChars = 200
    )
}

