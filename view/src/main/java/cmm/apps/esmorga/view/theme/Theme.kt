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
    onBackground = DarkGrey,
    surface = Lavender,
    surfaceVariant = Pearl,
    surfaceContainerLow = WhiteSmoke,
    surfaceContainerLowest = White,
    onSurface = DarkGrey,
    onSurfaceVariant = Sepia
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkClaret,
    onPrimary = VeryLightGrey,
    secondary = DarkPink,
    onSecondary = SoftDarkGrey,
    background = DarkLavender,
    onBackground = VeryLightGrey,
    surface = DarkLavender,
    surfaceVariant = DarkPearl,
    surfaceContainerLow = NightRider,
    surfaceContainerLowest = VeryDarkGrey,
    onSurface = VeryLightGrey,
    onSurfaceVariant = LightSepia
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
        typography = getEsmorgaTypography(colorScheme),
        content = content
    )
}