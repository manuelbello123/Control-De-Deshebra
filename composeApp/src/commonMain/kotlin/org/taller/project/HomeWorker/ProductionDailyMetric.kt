package org.taller.project.HomeWorker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun ProductionDailyMetric(
    icon: ImageVector,
    value: String,
    label: String,
    highlighted: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (highlighted) {
                    Color(0xFF001427)
                } else {
                    Color(0xFF001427).copy(alpha = 0.5f)
                }
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Medium,
                color = Color(0xFF001427)
            )
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF001427).copy(alpha = 0.5f)
            )
        }
    }
}