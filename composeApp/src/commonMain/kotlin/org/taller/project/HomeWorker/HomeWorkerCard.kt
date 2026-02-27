package org.taller.project.HomeWorker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.taller.project.Models.TrabajadorConProduccion
import org.taller.project.TotalWeekly.formatMoney

@Composable
fun HomeWorkerCard(
    trabajador: TrabajadorConProduccion,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // ── Información del trabajador ────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(0xFF001427).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Color(0xFF001427),
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Nombre
                Text(
                    text = trabajador.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF001427)
                )
            }

            // ── Métricas: Piezas y Sueldo ─────────────────────────────
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Total piezas
                    ProductionDailyMetric(
                        icon = Icons.Outlined.Checkroom,
                        value = "${trabajador.totalPrendas} pzs",
                        label = ""
                    )

                    // Sueldo diario
                    ProductionDailyMetric(
                        icon = Icons.Outlined.Payments,
                        value = formatMoney(trabajador.sueldoDiario),
                        label = "",
                        highlighted = trabajador.sueldoDiario > 0
                    )
                }
                // Ícono de flecha
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "Ver detalles",
                    tint = Color(0xFF001427).copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }

        }
    }
}