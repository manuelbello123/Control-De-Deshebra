package org.taller.project.ProductionWorker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.ProduccionExpandida

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionCard(
    produccion: ProduccionExpandida,
    prendasDisponibles: List<PrendaDto>,
    onDelete: () -> Unit,
    onEdit: (idPrenda: Int, cantidad: Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    showDeleteDialog = true
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    showEditDialog = true
                    false
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFD32F2F), RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(28.dp))
                            Text("Eliminar", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xFF001427), RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Editar", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Icon(Icons.Outlined.Edit, null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }
                }
                else -> {}
            }
        },
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true
    ) {
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Checkroom,
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 1.dp),
                            tint = Color(0xFF001427)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "${produccion.pieza} · ${produccion.color} · ${produccion.talla} · ${produccion.tipo} · ${produccion.modelo}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF001427)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF001427).copy(alpha = 0.08f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${produccion.cantidad} pzs",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF001427)
                        )
                    }
                }

                HorizontalDivider(
                    color = Color(0xFF001427),
                    thickness = 0.5.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF001427)
                        )
                        Text(
                            text = produccion.fecha,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF001427)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF001427)
                        )
                        Text(
                            text = produccion.hora,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF001427)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Badge,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF001427)
                        )
                        Text(
                            text = produccion.capturadoPor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF001427)
                        )
                    }
                }
            }
        }
    }

    // Dialog de eliminación
    if (showDeleteDialog) {
        DeleteProductionDialog(
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = {
                showDeleteDialog = false
                MainScope().launch {
                    dismissState.reset()
                }
            }
        )
    }

    // Dialog de edición
    if (showEditDialog) {
        EditProductionDialog(
            produccion = produccion,
            prendasDisponibles = prendasDisponibles,
            isUpdating = false, // Puedes conectar esto al state si quieres
            onDismiss = {
                showEditDialog = false
                MainScope().launch {
                    dismissState.reset()
                }
            },
            onConfirm = { idPrenda, cantidad->
                showEditDialog = false
                onEdit(idPrenda, cantidad)
            }
        )
    }
}