package org.taller.project.AddGarment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import kotlinx.coroutines.launch
import org.taller.project.Models.PrendaDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrendaCard(
    prenda: PrendaDto,
    state: GarmentState,
    onDelete: () -> Unit,
    onEdit: (Int, Int, Int, Int, Int, Int) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                            Icon(
                                Icons.Default.Delete,
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                "Eliminar",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
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
                            Text(
                                "Editar",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Outlined.Edit,
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
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
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(Color.White)
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
                            imageVector = Icons.Outlined.Checkroom,
                            contentDescription = null,
                            tint = Color(0xFF001427),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        "${prenda.pieza} · ${prenda.color} · ${prenda.talla} · ${prenda.tipo} · ${prenda.modelo}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF001427)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF001427).copy(alpha = 0.08f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "$${prenda.precio}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF001427)
                        )
                    }
                }
            }
        }
    }
    if (showEditDialog) {
        EditGarmentDialog(
            prenda = prenda,
            state = state,
            isUpdating = state.isUpdating,
            onDismiss = {
                showEditDialog = false
                kotlinx.coroutines.MainScope().launch { dismissState.reset() }
            },
            onConfirm = { pieza, color, talla, tipo, modelo, precio ->
                showEditDialog = false
                onEdit(pieza, color, talla, tipo, modelo, precio)
            }
        )
    }
    if (showDeleteDialog) {
        DeletePrendaDialog(
            prenda = prenda,
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = {
                showDeleteDialog = false
                kotlinx.coroutines.MainScope().launch { dismissState.reset() }
            }
        )
    }
}