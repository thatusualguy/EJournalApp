package io.github.thatusualguy.ejournal.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.thatusualguy.ejournal.R


val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Roboto,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily.Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
/* Other default text styles to override

caption = TextStyle(
fontFamily = FontFamily.Default,
fontWeight = FontWeight.Normal,
fontSize = 12.sp
)
*/
)


private val FontFamily.Companion.Roboto: FontFamily
    get() {
        return FontFamily(
            Font(R.font.roboto_regular),
            Font(R.font.roboto_bold, weight = FontWeight.Bold),
            Font(R.font.roboto_light, weight = FontWeight.Light)
        )
    }