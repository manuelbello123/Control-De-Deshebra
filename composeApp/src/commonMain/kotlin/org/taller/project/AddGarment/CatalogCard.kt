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
import androidx.compose.material.icons.outlined.DragHandle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.taller.project.Models.CatalogoSimpleDto
import org.taller.project.Models.PrecioDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoCard(
    item: Any,
    tipo: TipoCatalogo,
    viewModel: AddGarmentViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val (id, nombre, precio) = when (item) {
        is CatalogoSimpleDto -> Triple(item.id, item.nombre, null)
        is PrecioDto -> Triple(item.id, null, item.precio)
        else -> Triple(0, "", null)
    }

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
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = nombre ?: "$${precio ?: 0.0}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001427)
                )
            }
        }
    }

    if (showEditDialog) {
        EditCatalogoDialog(
            tipo = tipo,
            nombreActual = nombre ?: "",
            precioActual = precio ?: 0.0,
            isUpdating = false,
            onDismiss = {
                showEditDialog = false
                kotlinx.coroutines.MainScope().launch { dismissState.reset() }
            },
            onConfirm = { nuevoNombre, nuevoPrecio ->
                when (tipo) {
                    TipoCatalogo.PIEZAS -> viewModel.actualizarPiezas(id, nuevoNombre)
                    TipoCatalogo.COLORES -> viewModel.actualizarColores(id, nuevoNombre)
                    TipoCatalogo.TALLAS -> viewModel.actualizarTallas(id, nuevoNombre)
                    TipoCatalogo.TIPOS -> viewModel.actualizarTipos(id, nuevoNombre)
                    TipoCatalogo.MODELOS -> viewModel.actualizarModelos(id, nuevoNombre)
                    TipoCatalogo.PRECIOS -> viewModel.actualizarPrecios(id, nuevoPrecio)
                }
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        DeleteCatalogoDialog(
            tipo = tipo,
            nombre = nombre ?: "$${precio ?: 0.0}",
            onConfirm = {
                when (tipo) {
                    TipoCatalogo.PIEZAS -> viewModel.eliminarPiezas(id)
                    TipoCatalogo.COLORES -> viewModel.eliminarColores(id)
                    TipoCatalogo.TALLAS -> viewModel.eliminarTallas(id)
                    TipoCatalogo.TIPOS -> viewModel.eliminarTipos(id)
                    TipoCatalogo.MODELOS -> viewModel.eliminarModelos(id)
                    TipoCatalogo.PRECIOS -> viewModel.eliminarPrecios(id)
                }
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
                kotlinx.coroutines.MainScope().launch { dismissState.reset() }
            }
        )
    }
}
