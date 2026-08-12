package com.example.mynotesapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = InkPrimaryDark,
    secondary = InkSecondaryDark,
    tertiary = InkTertiaryDark,
    background = PaperBackgroundDark,
    surface = PaperSurfaceDark,
    onPrimary = PaperBackgroundDark,
    onSecondary = PaperBackgroundDark,
    onTertiary = PaperBackgroundDark,
    onBackground = OnPaperDark,
    onSurface = OnPaperDark,
    error = ErrorDark,
    onError = OnErrorDark,
)

private val LightColorScheme = lightColorScheme(
    primary = InkPrimaryLight,
    secondary = InkSecondaryLight,
    tertiary = InkTertiaryLight,
    background = PaperBackgroundLight,
    surface = PaperSurfaceLight,
    onPrimary = PaperBackgroundLight,
    onSecondary = PaperBackgroundLight,
    onTertiary = PaperBackgroundLight,
    onBackground = OnPaperLight,
    onSurface = OnPaperLight,
    error = ErrorLight,
    onError = OnErrorLight,
)

@Composable
fun MyNotesApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic (Material You) color is intentionally off by default so the
    // notebook brand palette above isn't overridden by the device wallpaper.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
