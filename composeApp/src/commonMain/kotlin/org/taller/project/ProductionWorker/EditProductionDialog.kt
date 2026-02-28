package org.taller.project.ProductionWorker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.ProduccionExpandida

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductionDialog(
    produccion: ProduccionExpandida,
    prendasDisponibles: List<PrendaDto>,
    isUpdating: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (idPrenda: Int, cantidad: Int) -> Unit,
) {
    // Pre-cargar prenda actual buscando por los datos de la producción
    val prendaActual = prendasDisponibles.find {
        it.pieza == produccion.pieza &&
                it.color == produccion.color &&
                it.talla == produccion.talla &&
                it.tipo == produccion.tipo &&
                it.modelo == produccion.modelo
    }

    var selectedPrenda by remember { mutableStateOf(prendaActual) }
    var cantidad by remember { mutableStateOf(produccion.cantidad.toString()) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Editar Producción",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF001427)
                    )
                }

                // Info de fecha/hora (solo informativo, no editable)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF001427).copy(alpha = 0.05f))
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFF001427).copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "Fecha y hora original",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF001427).copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${produccion.fecha} · ${produccion.hora}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF001427)
                        )
                    }
                }

                // Dropdown de prendas
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedPrenda?.let {
                            "${it.pieza} · ${it.color} · ${it.talla} · ${it.tipo} · ${it.modelo}"
                        } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Prenda") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Checkroom,
                                contentDescription = null,
                                tint = Color(0xFF001427)
                            )
                        },
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
                        prendasDisponibles.forEach { prenda ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = "${prenda.pieza} · ${prenda.color} · ${prenda.talla} · ${prenda.tipo} · ${prenda.modelo}",
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF001427)
                                        )
                                    }
                                },
                                onClick = {
                                    selectedPrenda = prenda
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Campo de cantidad
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { if (it.all { char -> char.isDigit() }) cantidad = it },
                    label = { Text("Cantidad") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Numbers,
                            contentDescription = null,
                            tint = Color(0xFF001427)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001427),
                        focusedLabelColor = Color(0xFF001427),
                        cursorColor = Color(0xFF001427)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF001427)
                        ),
                        enabled = !isUpdating
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            selectedPrenda?.let { prenda ->
                                cantidad.toIntOrNull()?.let { cant ->
                                    if (cant > 0) {
                                        onConfirm(prenda.idPrenda, cant, )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF001427)
                        ),
                        enabled = !isUpdating &&
                                selectedPrenda != null &&
                                cantidad.toIntOrNull() != null &&
                                cantidad.toInt() > 0
                    ) {
                        if (isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
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