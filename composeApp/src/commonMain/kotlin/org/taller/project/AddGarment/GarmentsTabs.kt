package org.taller.project.AddGarment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PrendasTab(
    state: GarmentState,
    viewModel: AddGarmentViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.cargarPrendas()
    }
    Box(modifier = Modifier.fillMaxSize()) {

        // Estado: cargando
        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF001427)
                )
                Text(
                    text = "Cargando prendas...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF001427)
                )
            }
        }

        // Estado: sin datos
        AnimatedVisibility(
            visible = !state.isLoading && state.prendas.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Checkroom,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFF001427).copy(alpha = 0.5f)
                )
                Text(
                    text = "No hay prendas registradas",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF001427)
                )
            }
        }

        // Estado: lista con datos
        AnimatedVisibility(
            visible = !state.isLoading && state.prendas.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ){
                item {
                    Text(
                        text = "${state.prendas.size} prendas",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF001427).copy(alpha = 0.7f)
                    )
                }

                itemsIndexed(
                    items = state.prendas,
                    key = { _, prenda -> prenda.idPrenda }
                ) { index, prenda ->
                    val offsetX = remember { Animatable(300f) }
                    val alpha = remember { Animatable(0f) }

                    LaunchedEffect(Unit) {
                        launch {
                            offsetX.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 500,
                                    delayMillis = index * 80
                                )
                            )
                        }
                        launch {
                            alpha.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 400,
                                    delayMillis = index * 80
                                )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .offset(x = offsetX.value.dp)
                            .graphicsLayer { this.alpha = alpha.value }
                    ) {
                        PrendaCard(
                            prenda = prenda,
                            state = state,
                            onDelete = { viewModel.eliminarPrendas(prenda.idPrenda) },
                            onEdit = { pieza, color, talla, tipo, modelo, precio ->
                                viewModel.actualizarPrendas(
                                    prenda.idPrenda,
                                    pieza,
                                    color,
                                    talla,
                                    tipo,
                                    modelo,
                                    precio
                                )
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}