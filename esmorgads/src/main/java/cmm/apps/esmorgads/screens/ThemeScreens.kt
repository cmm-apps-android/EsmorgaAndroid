package cmm.apps.esmorgads.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorgads.ui.ColorSwatch

@Composable
fun TypographyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        EsmorgaText(text = "Title Style", style = EsmorgaTextStyle.TITLE)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(text = "Heading 1 Style - Large headlines", style = EsmorgaTextStyle.HEADING_1)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(text = "Heading 2 Style - Medium headlines", style = EsmorgaTextStyle.HEADING_2)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(text = "Body 1 Style - Regular body text for paragraphs and content", style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(text = "Body 1 Accent Style - Accent variant of body text", style = EsmorgaTextStyle.BODY_1_ACCENT)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(text = "Caption Style - Small text for captions", style = EsmorgaTextStyle.CAPTION)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(text = "Button Style - Text for buttons", style = EsmorgaTextStyle.BUTTON)

        Spacer(modifier = Modifier.height(24.dp))

        EsmorgaText(text = "Text Overflow Example", style = EsmorgaTextStyle.HEADING_2)
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(
            text = "This is a very long text that will be truncated when it exceeds the maximum number of lines allowed",
            style = EsmorgaTextStyle.BODY_1,
            maxLines = 2
        )
    }
}

@Composable
fun ColorsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        EsmorgaText(
            text = "Primary Colors",
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ColorSwatch("Primary", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatch("Secondary", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)

        Spacer(modifier = Modifier.height(24.dp))

        EsmorgaText(
            text = "Surface Colors",
            style = EsmorgaTextStyle.HEADING_1,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ColorSwatch("Background", MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatch("Surface", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        ColorSwatch("Surface Variant", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

