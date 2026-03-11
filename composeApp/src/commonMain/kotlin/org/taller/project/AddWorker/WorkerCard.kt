package org.taller.project.AddWorker

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
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.taller.project.AddUser.DeleteUserDialog
import org.taller.project.AddUser.EditUserDialog
import org.taller.project.Models.TrabajadorDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerCard(
    trabajador: TrabajadorDto,
    onToggleActivo: () -> Unit,
    onEdit: (nombre: String, usuario: String) -> Unit,
    onDelete: () -> Unit
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ── Avatar y datos del trabajador ────────────────────
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
                                color = if (trabajador.activo) {
                                    Color(0xFF001427).copy(alpha = 0.1f)
                                } else {
                                    Color(0xFF757575).copy(alpha = 0.1f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = if (trabajador.activo) {
                                Color(0xFF001427)
                            } else {
                                Color(0xFF757575)
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "${trabajador.nombre} ${trabajador.usuario}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (trabajador.activo) {
                                Color(0xFF001427)
                            } else {
                                Color(0xFF757575)
                            }
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // ── Switch de activo ──────────────────────────────────
                    Switch(
                        checked = trabajador.activo,
                        onCheckedChange = { onToggleActivo() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF001427),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFF757575)
                        )
                    )
                    Text(
                        text = if (trabajador.activo) "Activo" else "Inactivo",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (trabajador.activo) {
                            Color(0xFF001427)
                        } else {
                            Color(0xFF001427).copy(alpha = 0.5f)
                        }
                    )
                }
            }
        }
    }

    // Dialog de eliminación
    if (showDeleteDialog) {
        DeleteWorkerDialog(
            nombre = trabajador.nombre,
            usuario = trabajador.usuario,
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
        WorkerEditDialog(
            trabajador = trabajador,
            isUpdating = false, // Puedes conectar esto al state del ViewModel si quieres
            onDismiss = {
                showEditDialog = false
                MainScope().launch {
                    dismissState.reset()
                }
            },
            onConfirm = { nombre, usuario ->
                showEditDialog = false
                onEdit(nombre, usuario)
            }
        )
    }
}