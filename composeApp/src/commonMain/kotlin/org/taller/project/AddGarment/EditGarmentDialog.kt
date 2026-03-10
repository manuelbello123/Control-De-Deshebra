package org.taller.project.AddGarment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import org.taller.project.Models.PrendaDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGarmentDialog(
    prenda: PrendaDto,
    state: GarmentState,
    isUpdating: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, Int, Int, Int) -> Unit
) {
    // Pre-cargar valores buscando en las listas por nombre/precio
    var selectedPieza by remember {
        mutableStateOf(state.piezas.find { it.nombre == prenda.pieza })
    }
    var selectedColor by remember {
        mutableStateOf(state.colores.find { it.nombre == prenda.color })
    }
    var selectedTalla by remember {
        mutableStateOf(state.tallas.find { it.nombre == prenda.talla })
    }
    var selectedTipo by remember {
        mutableStateOf(state.tipos.find { it.nombre == prenda.tipo })
    }
    var selectedModelo by remember {
        mutableStateOf(state.modelos.find { it.nombre == prenda.modelo })
    }
    var selectedPrecio by remember {
        mutableStateOf(state.precios.find { it.precio == prenda.precio })
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Editar Prenda",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001427)
                        )
                    }
                }
                // Dropdowns
                item { CatalogoDropdown("Pieza", state.piezas, selectedPieza) { selectedPieza = it } }
                item { CatalogoDropdown("Color", state.colores, selectedColor) { selectedColor = it } }
                item { CatalogoDropdown("Talla", state.tallas, selectedTalla) { selectedTalla = it } }
                item { CatalogoDropdown("Tipo", state.tipos, selectedTipo) { selectedTipo = it } }
                item { CatalogoDropdown("Modelo", state.modelos, selectedModelo) { selectedModelo = it } }
                item { CatalogoDropdown("Precio", state.precios, selectedPrecio) { selectedPrecio = it } }

                // Botones
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF001427)
                            ),
                            enabled = !isUpdating
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                selectedPieza?.let { p ->
                                    selectedColor?.let { c ->
                                        selectedTalla?.let { t ->
                                            selectedTipo?.let { ti ->
                                                selectedModelo?.let { m ->
                                                    selectedPrecio?.let { pr ->
                                                        onConfirm(p.id, c.id, t.id, ti.id, m.id, pr.id)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF001427)
                            ),
                            enabled = !isUpdating &&
                                    selectedPieza != null &&
                                    selectedColor != null &&
                                    selectedTalla != null &&
                                    selectedTipo != null &&
                                    selectedModelo != null &&
                                    selectedPrecio != null
                        ) {
                            if (isUpdating) {
                                CircularProgressIndicator(
                                    Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Guardar")
                            }
                        }
                    }
                }
            }
        }
    }
}