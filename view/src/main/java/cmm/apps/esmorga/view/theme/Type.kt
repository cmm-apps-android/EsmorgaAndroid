package cmm.apps.esmorga.view.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cmm.apps.esmorga.view.R

@OptIn(ExperimentalTextApi::class)
val EpilogueFontFamily =
    FontFamily(
        Font(
            R.font.epilogue_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight)
            )
        ),
        Font(
            R.font.epilogue_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight)
            )
        ),
        Font(
            R.font.epilogue_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight)
            )
        ),
    )

@OptIn(ExperimentalTextApi::class)
val JackartaFontFamily =
    FontFamily(
        Font(
            R.font.plus_jakarta_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight)
            )
        ),
        Font(
            R.font.plus_jakarta_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight)
            )
        ),
        Font(
            R.font.plus_jakarta_flex,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight)
            )
        ),
    )

// Set of Material typography styles to start with
val EsmorgaTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = JackartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.33).sp
    ),
    titleMedium = TextStyle(
        fontFamily = JackartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.25).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = JackartaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 23.sp,
        letterSpacing = (-0.25).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = JackartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = JackartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    labelLarge = TextStyle(
        fontFamily = EpilogueFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = JackartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp
    )
)