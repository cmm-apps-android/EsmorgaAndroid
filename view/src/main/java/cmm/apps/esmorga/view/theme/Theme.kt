package cmm.apps.esmorga.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

//TODO create a proper dark theme
private val DarkColorScheme = darkColorScheme(
    primary = Claret,
    onPrimary = VeryLigthGrey,
    secondary = Pink,
    onSecondary = DarkGrey,
    background = Lavender,
    surface = Lavender,
    surfaceVariant = Pearl,
    surfaceContainerLow = WhiteSmoke,
    surfaceContainerLowest = White,
    onSurface = Sepia
)

private val LightColorScheme = lightColorScheme(
    primary = Claret,
    onPrimary = VeryLigthGrey,
    secondary = Pink,
    onSecondary = DarkGrey,
    background = Lavender,
    surface = Lavender,
    surfaceVariant = Pearl,
    surfaceContainerLow = WhiteSmoke,
    surfaceContainerLowest = White,
    onSurface = Sepia
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