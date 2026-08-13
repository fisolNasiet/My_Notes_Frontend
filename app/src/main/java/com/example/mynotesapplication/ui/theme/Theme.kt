package com.example.mynotesapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Fixed brand palette (pitch black + accent colors), not a togglable
// light/dark scheme — the design spec calls for one specific look.
private val AppColorScheme = darkColorScheme(
    primary = AccentYellow,
    onPrimary = OnAccent,
    secondary = AccentBlue,
    onSecondary = OnAccent,
    tertiary = AccentGreen,
    onTertiary = OnAccent,
    background = PitchBlack,
    onBackground = OnBlack,
    surface = ElevatedBlack,
    onSurface = OnBlack,
    error = ErrorRed,
    onError = OnErrorRed,
)

@Composable
fun MyNotesApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
