package com.example.mynotesapplication.ui.theme

import androidx.compose.ui.graphics.Color

// Backgrounds
val PitchBlack = Color(0xFF0A0A0A)
val ElevatedBlack = Color(0xFF161616)
val PaleYellow = Color(0xFFFAF0C8)
val PaleYellowSurface = Color(0xFFF3E6AE)

// Text on background
val OnBlack = Color(0xFFFFFFFF)
val OnBlackMuted = Color(0xFFFFFFFF).copy(alpha = 0.6f)
val OnPaleYellow = Color(0xFF1A1408)
val OnPaleYellowMuted = OnPaleYellow.copy(alpha = 0.55f)

// Accent palette used to tint note cards (backs Note.color) — soft coral,
// bright yellow, light green, light blue, per the design spec.
val AccentCoral = Color(0xFFFF9E80)
val AccentYellow = Color(0xFFFFD54F)
val AccentGreen = Color(0xFFB9E4A6)
val AccentBlue = Color(0xFFA8D8F0)

val NoteAccentColors = listOf(
    AccentCoral,
    AccentYellow,
    AccentGreen,
    AccentBlue,
)

// All accent swatches above are light enough for the same near-black text.
val OnAccent = Color(0xFF1A1408)

val ErrorRed = Color(0xFFFF6B5E)
val OnErrorRed = Color(0xFF2A0705)
