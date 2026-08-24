package com.example.mynotesapplication.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Subtle dotted/perforated grid texture for the pitch-black dashboard
 * background.
 */
fun Modifier.dottedGridBackground(
    dotColor: Color = Color.White.copy(alpha = 0.06f),
    spacing: Dp = 24.dp,
    dotRadius: Dp = 1.2.dp,
): Modifier = drawBehind {
    val spacingPx = spacing.toPx()
    val radiusPx = dotRadius.toPx()
    var y = spacingPx / 2
    while (y < size.height) {
        var x = spacingPx / 2
        while (x < size.width) {
            drawCircle(color = dotColor, radius = radiusPx, center = androidx.compose.ui.geometry.Offset(x, y))
            x += spacingPx
        }
        y += spacingPx
    }
}
