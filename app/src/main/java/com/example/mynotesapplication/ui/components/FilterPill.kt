package com.example.mynotesapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mynotesapplication.ui.theme.OnBlack
import com.example.mynotesapplication.ui.theme.PillShape
import com.example.mynotesapplication.ui.theme.PitchBlack

/**
 * The active "All" filter chip: black pill, thin white border, white text,
 * with a small circular badge showing the real note count.
 */
@Composable
fun FilterPill(
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(PillShape)
            .background(PitchBlack)
            .border(BorderStroke(1.dp, OnBlack), PillShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = OnBlack)
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(OnBlack),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 11.sp),
                color = PitchBlack,
            )
        }
    }
}
