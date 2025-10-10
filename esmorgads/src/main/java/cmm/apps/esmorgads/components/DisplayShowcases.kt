package cmm.apps.esmorgads.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaCard
import cmm.apps.designsystem.EsmorgaCircularLoader
import cmm.apps.designsystem.EsmorgaLinearLoader
import cmm.apps.designsystem.EsmorgaRadioButton
import cmm.apps.designsystem.EsmorgaRow
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle

@Composable
fun LoaderShowcase() {
    EsmorgaText(text = "Linear Loader", style = EsmorgaTextStyle.BODY_1)
    Spacer(modifier = Modifier.height(8.dp))
    EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(16.dp))
    EsmorgaText(text = "Circular Loaders", style = EsmorgaTextStyle.BODY_1)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        EsmorgaCircularLoader(modifier = Modifier.size(24.dp))
        EsmorgaCircularLoader(modifier = Modifier.size(40.dp))
        EsmorgaCircularLoader(modifier = Modifier.size(60.dp))
    }
}

@Composable
fun CardShowcase() {
    EsmorgaCard(
        title = "Card with Subtitle",
        subtitle = "This is a card subtitle with some description text",
        hasBottomLine = true
    )
    Spacer(modifier = Modifier.height(8.dp))
    EsmorgaCard(
        title = "Card Without Subtitle",
        hasBottomLine = false
    )
}

@Composable
fun RowShowcase() {
    EsmorgaRow(
        title = "Row with subtitle",
        subtitle = "This is a subtitle",
        onClick = { }
    )
    HorizontalDivider()
    EsmorgaRow(
        title = "Row with caption",
        caption = "Caption",
        onClick = { }
    )
    HorizontalDivider()
    EsmorgaRow(
        title = "Simple row",
        onClick = { }
    )
}

@Composable
fun RadioButtonShowcase() {
    var selectedRadio by remember { mutableIntStateOf(0) }

    EsmorgaRadioButton(
        text = "Option 1",
        selected = selectedRadio == 0,
        onClick = { selectedRadio = 0 }
    )
    EsmorgaRadioButton(
        text = "Option 2",
        selected = selectedRadio == 1,
        onClick = { selectedRadio = 1 }
    )
    EsmorgaRadioButton(
        text = "Option 3",
        selected = selectedRadio == 2,
        onClick = { selectedRadio = 2 }
    )
}

