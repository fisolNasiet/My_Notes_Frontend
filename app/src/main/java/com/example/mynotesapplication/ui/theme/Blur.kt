package com.example.mynotesapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Frosted-glass container for floating toolbars/docks.
 *
 * Real backdrop blur (Modifier.blur/RenderEffect) needs API 31+; minSdk here
 * is 26, so below that we fall back to a flat translucent scrim, which still
 * reads as "frosted glass" against the pitch-black dashboard.
 */
@Composable
fun FrostedGlassContainer(
    modifier: Modifier = Modifier,
    shape: Shape = PillShape,
    tint: Color = Color.Black,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.clip(shape)) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier
                            .background(tint.copy(alpha = 0.35f))
                            .blur(radius = 20.dp)
                    } else {
                        Modifier.background(tint.copy(alpha = 0.65f))
                    }
                )
        )
        content()
    }
}
