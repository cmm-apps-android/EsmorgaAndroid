package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    title: Int,
    placeholder: Int,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 4,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    errorText: String? = null,
    isEnabled: Boolean = true,
    maxChars: Int? = null,
    onDonePressed: () -> Unit = {},
    visibilityToggleDescription: Int? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column {
        EsmorgaText(text = stringResource(id = title), style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            onValueChange = { newValue ->
                if (maxChars == null || newValue.length <= maxChars) {
                    onValueChange(newValue)
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = placeholder),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = isEnabled,
            modifier = if (!singleLine) {
                modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .verticalScroll(scrollState)
            } else {
                modifier.fillMaxWidth()
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            ),
            supportingText = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (errorText != null) {
                        EsmorgaText(
                            text = errorText,
                            style = EsmorgaTextStyle.CAPTION,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            maxLines = 2
                        )
                    } else {
                        Spacer(modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp))
                    }
                    if (maxChars != null) {
                        EsmorgaText(
                            text = "${value.length}/$maxChars",
                            style = EsmorgaTextStyle.CAPTION,
                            modifier = Modifier.wrapContentWidth(Alignment.End)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12),
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    val image = if (passwordVisible) painterResource(id = R.drawable.ic_visibility_off) else painterResource(id = R.drawable.ic_visibility)
                    val description = if (visibilityToggleDescription != null) {
                        stringResource(id = visibilityToggleDescription)
                    } else {
                        null
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        val visibleStateText = stringResource(R.string.password_field_state_visible)
                        val hiddenStateText = stringResource(R.string.password_field_state_hidden)
                        Icon(
                            image,
                            description,
                            modifier = Modifier
                                .semantics { stateDescription = if (passwordVisible)
                                    visibleStateText
                                else
                                    hiddenStateText
                                }
                        )
                    }
                }
            },
            keyboardActions = KeyboardActions(onDone = { onDonePressed() })
        )
    }
}