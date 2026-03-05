package org.taller.project.AddGarment

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import org.taller.project.Models.CatalogoSimpleDto
import org.taller.project.Models.PrecioDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogosTab(
    state: GarmentState,
    viewModel: AddGarmentViewModel,
    selectedCatalogo: TipoCatalogo,
    onCatalogoChange: (TipoCatalogo) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarCatalogos()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Selector de catálogo
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.padding(16.dp, 12.dp)
        ) {
            OutlinedTextField(
                value = selectedCatalogo.nombreDisplay,
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecciona una categoría") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF001427),
                    focusedLabelColor = Color(0xFF001427),
                    cursorColor = Color(0xFF001427)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                TipoCatalogo.values().forEach { tipo ->
                    DropdownMenuItem(
                        text = { Text(tipo.nombreDisplay) },
                        onClick = {
                            onCatalogoChange(tipo)
                            expanded = false
                        }
                    )
                }
            }
        }

        // Lista del catálogo seleccionado
        val items = when (selectedCatalogo) {
            TipoCatalogo.PIEZAS -> state.piezas
            TipoCatalogo.COLORES -> state.colores
            TipoCatalogo.TALLAS -> state.tallas
            TipoCatalogo.TIPOS -> state.tipos
            TipoCatalogo.MODELOS -> state.modelos
            TipoCatalogo.PRECIOS -> state.precios
        }

        if (items.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF001427).copy(alpha = 0.5f)
                    )
                    Text(
                        text = "No hay ${selectedCatalogo.nombreDisplay.lowercase()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF001427)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Text(
                        text = "${items.size} ${selectedCatalogo.nombreDisplay.lowercase()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF001427).copy(alpha = 0.7f)
                    )
                }

                itemsIndexed(items, key = { _, item ->
                    when (item) {
                        is CatalogoSimpleDto -> "${selectedCatalogo.ruta}_${item.id}"
                        is PrecioDto -> "precio_${item.id}"
                        else -> "unknown_${item.hashCode()}"
                    }
                }) { index, item ->
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
                        CatalogoCard(
                            item = item,
                            tipo = selectedCatalogo,
                            viewModel = viewModel
                        )
                        Spacer (Modifier.height(12.dp))

                    }

                }
            }
        }
    }
}