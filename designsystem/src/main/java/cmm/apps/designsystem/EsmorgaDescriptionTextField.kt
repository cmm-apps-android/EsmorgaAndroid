package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaDescriptionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    title: Int,
    placeholder: Int? = null,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    errorText: String? = null,
    isEnabled: Boolean = true,
    onDonePressed: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column {
        EsmorgaText(text = stringResource(id = title), style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 200.dp),
            placeholder = {
                placeholder?.let {
                    Text(
                        text = stringResource(id = it),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            singleLine = singleLine,
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(onDone = { onDonePressed() }),
            enabled = isEnabled,
            shape = RoundedCornerShape(12),
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    val icon = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painterResource(id = icon), contentDescription = null)
                    }
                }
            },
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
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    EsmorgaText(
                        text = "${value.length}/5000",
                        style = EsmorgaTextStyle.CAPTION,
                        modifier = Modifier.wrapContentWidth(Alignment.End)
                    )
                }
            },
            maxLines = 10,
        )
        LaunchedEffect(value) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EsmorgaDescriptionTextFieldPreview() {
    var text by remember { mutableStateOf("Test text") }

    EsmorgaDescriptionTextField(
        value = text,
        onValueChange = { text = it },
        title = android.R.string.untitled,
        errorText = "Este campo es obligatorioEste campo es obligatorioEste campo"
    )
}