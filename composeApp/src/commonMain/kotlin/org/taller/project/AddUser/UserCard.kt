package org.taller.project.AddUser

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
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.taller.project.Models.UsuarioDto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(
    usuario: UsuarioDto,
    onToggleActivo: () -> Unit,
    onEdit: (username: String, rol: String) -> Unit,
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
        // Contenido de la card
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

                // Información del usuario
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Avatar con ícono diferente según el rol
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = if (usuario.rol == "ADMIN") {
                                    Color(0xFF001427).copy(alpha = 0.15f)
                                } else {
                                    Color(0xFF001427).copy(alpha = 0.08f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (usuario.rol == "ADMIN") {
                                Icons.Outlined.AdminPanelSettings
                            } else {
                                Icons.Outlined.Person
                            },
                            contentDescription = null,
                            tint = Color(0xFF001427),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Username y rol
                    Column {
                        Text(
                            text = usuario.username,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF001427)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Shield,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF001427).copy(alpha = 0.6f)
                            )
                            Text(
                                text = usuario.rol,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF001427).copy(alpha = 0.7f),
                                fontWeight = if (usuario.rol == "ADMIN") {
                                    FontWeight.Medium
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        }
                    }
                }

                // Switch de activo/inactivo
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Switch(
                        checked = usuario.activo,
                        onCheckedChange = { onToggleActivo() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF001427),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFF001427).copy(alpha = 0.3f)
                        )
                    )
                    Text(
                        text = if (usuario.activo) "Activo" else "Inactivo",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (usuario.activo) {
                            Color(0xFF001427)
                        } else {
                            Color(0xFF001427).copy(alpha = 0.5f)
                        }
                    )
                }
            }
        }
    }
    // Dialog de eliminacion
    if (showDeleteDialog) {
        DeleteUserDialog(
            username = usuario.username,
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = {
                showDeleteDialog = false
                kotlinx.coroutines.MainScope().launch {
                    dismissState.reset()
                }
            }
        )
    }

    // Dialog de edición
    if (showEditDialog) {
        EditUserDialog(
            usuario = usuario,
            isUpdating = false,  // Puedes conectar esto al state del ViewModel
            onDismiss = {
                showEditDialog = false
                kotlinx.coroutines.MainScope().launch {
                    dismissState.reset()
                }
            },
            onConfirm = { username, rol->
                showEditDialog = false
                onEdit(username, rol)
            }
        )
    }
}
