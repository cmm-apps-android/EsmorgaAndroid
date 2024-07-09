package cmm.apps.esmorga.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = Claret,
    onPrimary = VeryLightGrey,
    secondary = Pink,
    onSecondary = DarkGrey,
    background = Lavender,
    surface = Lavender,
    surfaceVariant = Pearl,
    surfaceContainerLow = WhiteSmoke,
    surfaceContainerLowest = White,
    onSurface = Sepia
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkClaret,
    onPrimary = VeryLightGrey,
    secondary = DarkPink,
    onSecondary = SoftDarkGrey,
    background = DarkLavender,
    surface = DarkLavender,
    surfaceVariant = DarkPearl,
    surfaceContainerLow = DarkWhiteSmoke,
    surfaceContainerLowest = DarkestWhite,
    onSurface = LightSepia
)

@Composable
fun EsmorgaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EsmorgaTypography,
        content = content
    )
}