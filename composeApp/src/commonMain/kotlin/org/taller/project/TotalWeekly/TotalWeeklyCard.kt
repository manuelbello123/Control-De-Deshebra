package org.taller.project.TotalWeekly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import org.taller.project.Models.TotalSemanalDto

@Composable
fun TotalWeeklyCard(total: TotalSemanalDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Fila superior: Semana ISO + Rango de fechas ───────────
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Semana ISO
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF001427),
                            modifier = Modifier.size(20.dp)
                        )
                    Column {
                        Text(
                            text = "Semana ${total.semanaIso}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001427)
                        )
                        Text(
                            text = "${formatDate(total.inicioSemana)} - ${formatDate(total.finSemana)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF001427).copy(alpha = 0.6f)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = Color(0xFF001427),
                thickness = 0.5.dp
            )

            // ── Métricas: Piezas y Sueldo ─────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total de piezas
                MetricBox(
                    icon = Icons.Outlined.Checkroom,
                    label = "Prendas",
                    value = total.totalPiezas.toString(),
                    modifier = Modifier.weight(1f)
                )

                // Total sueldo
                MetricBox(
                    icon = Icons.Outlined.Payments,
                    label = "Sueldo",
                    value = formatMoney(total.totalSueldo),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
