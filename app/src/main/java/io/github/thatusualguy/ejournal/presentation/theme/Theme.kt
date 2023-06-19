package io.github.thatusualguy.ejournal.presentation.theme

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.github.thatusualguy.ejournal.domain.repo.AppTheme
import io.github.thatusualguy.ejournal.domain.repo.Settings

private val DarkColorPalette = darkColors(
    primary = Blue,
    primaryVariant = Blue,
    onPrimary = Color.White,
    secondary = DarkBlue
)

private val LightColorPalette = lightColors(
    primary = DarkBlue,
    primaryVariant = DarkBlue,
    secondary = Blue,
    background = Color(231, 231, 231, 255),
    surface = Color.White
//            primary = Blue,
//    primaryVariant = DarkBlue,
//    secondary = Yellow

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)


@Composable
fun EJournalTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    if (darkTheme)
        systemUiController.setSystemBarsColor(
            color = Color.Black
        )
    else systemUiController.setSystemBarsColor(
        color = colors.primary
    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}