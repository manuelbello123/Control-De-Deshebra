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
import kotlinx.coroutines.launch
import org.taller.project.Models.TrabajadorDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerCard(
    trabajador: TrabajadorDto,
    onToggleActivo: () -> Unit,
    onEdit: (nombre: String, usuario: String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                showEditDialog = true
                false
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.targetValue

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFF001427),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Editar",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false
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

                    // Nombre y usuario
                    Column {
                        Text(
                            text = trabajador.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (trabajador.activo) {
                                Color(0xFF001427)
                            } else {
                                Color(0xFF757575)
                            }
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AlternateEmail,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF001427).copy(alpha = 0.6f)
                            )
                            Text(
                                text = trabajador.usuario,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF001427).copy(alpha = 0.6f)
                            )
                        }
                    }
                }

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
            }
        }
    }


    // Dialog de edición
    if (showEditDialog) {
        WorkerEditDialog(
            trabajador = trabajador,
            isUpdating = false, // Puedes conectar esto al state del ViewModel si quieres
            onDismiss = {
                showEditDialog = false
                kotlinx.coroutines.MainScope().launch {
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