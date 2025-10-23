package cmm.apps.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun EsmorgaText(text: String, style: EsmorgaTextStyle, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Start, maxLines: Int = Int.MAX_VALUE) {
    Text(
        text = text,
        style = getTextStyle(style),
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun getTextStyle(style: EsmorgaTextStyle): TextStyle {
    return when (style) {
        EsmorgaTextStyle.TITLE -> MaterialTheme.typography.titleLarge
        EsmorgaTextStyle.HEADING_1 -> MaterialTheme.typography.headlineLarge
        EsmorgaTextStyle.HEADING_2 -> MaterialTheme.typography.headlineMedium
        EsmorgaTextStyle.BODY_1 -> MaterialTheme.typography.bodyMedium
        EsmorgaTextStyle.BODY_1_ACCENT -> MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        EsmorgaTextStyle.CAPTION -> MaterialTheme.typography.labelSmall
        EsmorgaTextStyle.CAPTION_UNDERSCORE -> MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.Underline)
        EsmorgaTextStyle.BUTTON -> MaterialTheme.typography.labelLarge
    }
}

enum class EsmorgaTextStyle {
    TITLE,
    HEADING_1,
    HEADING_2,
    BODY_1,
    BODY_1_ACCENT,
    CAPTION,
    CAPTION_UNDERSCORE,
    BUTTON
}