package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaRow(
    title: String,
    subtitle: String? = null,
    caption: String? = null,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.weight(1f)
        ) {
            EsmorgaText(
                text = title,
                style = EsmorgaTextStyle.HEADING_2
            )
            subtitle?.let {
                EsmorgaText(
                    text = subtitle,
                    style = EsmorgaTextStyle.BODY_1
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            caption?.let {
                EsmorgaText(
                    text = caption,
                    style = EsmorgaTextStyle.CAPTION
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = "Icono adelante"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun esmorgaRowPreview() {
    EsmorgaRow(
        title = "Title",
        subtitle = "Subtitle",
        caption = "Caption",
        icon = Icons.AutoMirrored.Filled.ArrowForward
    )
}