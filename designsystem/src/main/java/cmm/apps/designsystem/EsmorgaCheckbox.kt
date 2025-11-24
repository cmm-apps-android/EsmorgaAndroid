package cmm.apps.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

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

@Composable
fun EsmorgaCheckboxRow(
    text: String,
    shouldShowChecked: Boolean,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var rememberedCheck by remember { mutableStateOf(checked) }
    Column(
        modifier = Modifier.clickable(
        role = Role.Checkbox,
        onClick = {
            rememberedCheck = !rememberedCheck
            onCheckedChanged(rememberedCheck)
        }
    )) {
        Row(
            modifier = modifier.padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EsmorgaText(
                text = text,
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(vertical = 16.dp),
            )

            if (shouldShowChecked) {
                Spacer(modifier = Modifier.weight(1f))
                EsmorgaCheckbox(
                    checked = rememberedCheck,
                    onCheckedChanged = {
                        rememberedCheck = it
                        onCheckedChanged(rememberedCheck)
                    }
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
    }
}
