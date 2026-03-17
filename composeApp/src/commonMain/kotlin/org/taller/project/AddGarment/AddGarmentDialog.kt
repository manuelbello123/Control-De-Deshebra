package org.taller.project.AddGarment

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
import org.taller.project.Models.CatalogoSimpleDto
import org.taller.project.Models.PrecioDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPrendaDialog(
    state: GarmentState,
    isCreating: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int, Int, Int, Int) -> Unit
) {
    var selectedPieza by remember { mutableStateOf<CatalogoSimpleDto?>(null) }
    var selectedColor by remember { mutableStateOf<CatalogoSimpleDto?>(null) }
    var selectedTalla by remember { mutableStateOf<CatalogoSimpleDto?>(null) }
    var selectedTipo by remember { mutableStateOf<CatalogoSimpleDto?>(null) }
    var selectedModelo by remember { mutableStateOf<CatalogoSimpleDto?>(null) }
    var selectedPrecio by remember { mutableStateOf<PrecioDto?>(null) }

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
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Nueva Prenda", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF001427))
                    }
                }

                // Dropdowns
                item { CatalogoDropdown("Pieza", state.piezas, selectedPieza) { selectedPieza = it } }
                item { CatalogoDropdown("Color", state.colores, selectedColor) { selectedColor = it } }
                item { CatalogoDropdown("Talla", state.tallas, selectedTalla) { selectedTalla = it } }
                item { CatalogoDropdown("Tipo", state.tipos, selectedTipo) { selectedTipo = it } }
                item { CatalogoDropdown("Modelo", state.modelos, selectedModelo) { selectedModelo = it } }
                item { CatalogoDropdown("Precio", state.precios, selectedPrecio) { selectedPrecio = it } }

                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF001427)),
                            enabled = !isCreating
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001427)),
                            enabled = !isCreating && selectedPieza != null && selectedColor != null &&
                                    selectedTalla != null && selectedTipo != null && selectedModelo != null && selectedPrecio != null
                        ) {
                            if (isCreating) {
                                CircularProgressIndicator(Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("Crear")
                            }
                        }
                    }
                }
            }
        }
    }
}