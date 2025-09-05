package cmm.apps.esmorga.view.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cmm.apps.esmorga.view.R

@OptIn(ExperimentalTextApi::class)
val EpilogueBoldFontFamily =
    FontFamily(
        Font(
            resId = R.font.epilogue_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight)
            )
        ),
    )

@OptIn(ExperimentalTextApi::class)
val EpilogueNormalFontFamily =
    FontFamily(
        Font(
            resId = R.font.epilogue_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight)
            )
        )
    )

@OptIn(ExperimentalTextApi::class)
val JakartaBoldFontFamily =
    FontFamily(
        Font(
            resId = R.font.plus_jakarta_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight)
            )
        ),
    )

@OptIn(ExperimentalTextApi::class)
val JakartaNormalFontFamily =
    FontFamily(
        Font(
            resId = R.font.plus_jakarta_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight)
            )
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
            fontFamily = JakartaBoldFontFamily,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.33).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = JakartaBoldFontFamily,
            fontSize = 22.sp,
            lineHeight = 27.5.sp,
            letterSpacing = (-0.33).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = JakartaBoldFontFamily,
            fontSize = 18.sp,
            lineHeight = 22.5.sp,
            letterSpacing = (-0.27).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = JakartaNormalFontFamily,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        labelLarge = TextStyle(
            fontFamily = EpilogueBoldFontFamily,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            letterSpacing = 0.20.sp
        ),
        labelSmall = TextStyle(
            fontFamily = EpilogueNormalFontFamily,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    )
}