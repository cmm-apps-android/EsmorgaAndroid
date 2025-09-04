package cmm.apps.esmorga.view.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cmm.apps.esmorga.view.R

val EpilogueFontFamily =
    FontFamily(
        Font(
            resId = R.font.epilogue_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.epilogue_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.epilogue_regular,
            weight = FontWeight.Normal
        ),
    )

val JackartaFontFamily =
    FontFamily(
        Font(
            resId = R.font.plus_jakarta_sans_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resId = R.font.plus_jakarta_sans_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.plus_jakarta_sans_regular,
            weight = FontWeight.Normal
        ),
    )

// Set of Material typography styles to start with
/*
    If you use ThemeMaterial.colorScheme.XXXX it doesn't work.
 */
@Composable
fun getEsmorgaTypography(colorScheme: ColorScheme): Typography {
    return Typography(
        titleLarge = TextStyle(
            fontFamily = JackartaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.33).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = JackartaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 27.5.sp,
            letterSpacing = (-0.33).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = JackartaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 22.5.sp,
            letterSpacing = (-0.27).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = JackartaFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        labelLarge = TextStyle(
            fontFamily = EpilogueFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.20.sp
        ),
        labelSmall = TextStyle(
            fontFamily = EpilogueFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    )
}
