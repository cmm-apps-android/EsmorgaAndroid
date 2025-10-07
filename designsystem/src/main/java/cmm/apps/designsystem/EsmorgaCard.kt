package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun EsmorgaCard(
    title: String,
    subtitle: String? = null,
    icon: Int? = null,
    hasBottomLine: Boolean = true
) {
    Column {
        HorizontalDivider(thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            icon?.let { resId ->
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = "Custom icon",
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                EsmorgaText(
                    text = title,
                    style = EsmorgaTextStyle.HEADING_2,
                    maxLines = 1,
                )
                subtitle?.let {
                    EsmorgaText(
                        text = subtitle,
                        style = EsmorgaTextStyle.BODY_1,
                        maxLines = 3,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        if (hasBottomLine) {
            HorizontalDivider(thickness = 1.dp)
        }
    }
}