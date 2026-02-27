package org.taller.project.ProductionWorker

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.taller.project.History.DayHeader
import org.taller.project.History.toDayName
import org.taller.project.Models.ProductionByDay
import org.taller.project.Models.ProductionDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductionWorkerScreen(
    navController: NavController,
    viewModel: ProductionWorkerViewModel,
    idTrabajador: Int,
    nombre: String
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddDialog by remember { mutableStateOf(false) }

    // Cargar datos al iniciar
    LaunchedEffect(idTrabajador) {
        viewModel.cargarDatosCompletos(idTrabajador)
    }

    // Mostrar errores
    LaunchedEffect(state.error) {
        state.error?.let { mensaje ->
            val resultado = snackbarHostState.showSnackbar(
                message = mensaje,
                actionLabel = "Reintentar",
                duration = SnackbarDuration.Long
            )
            if (resultado == SnackbarResult.ActionPerformed) {
                viewModel.retry(idTrabajador)
            }
            viewModel.clearError()
        }
    }

    // Mostrar éxitos
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { mensaje ->
            snackbarHostState.showSnackbar(
                message = mensaje,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (state.error != null) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        Color(0xFF001427)
                    },
                    contentColor = if (state.error != null) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        Color.White
                    },
                    actionColor = if (state.error != null) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Color.White.copy(alpha = 0.8f)
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        floatingActionButton = {
            if (!state.isLoading && state.produccionSemanal.isNotEmpty() || state.produccionSemanal.isEmpty()) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Color(0xFF001427),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, "Agregar producción")
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxSize()) {

                this@Column.AnimatedVisibility(
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
                            text = "Cargando producción...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF001427)
                        )
                    }
                }
                // Estado: sin datos
                this@Column.AnimatedVisibility(
                    visible = !state.isLoading && state.produccionSemanal.isEmpty() && state.error == null,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Inbox,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF001427).copy(alpha = 0.5f)
                        )
                        Text(
                            text = "No hay producción esta semana",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF001427)
                        )
                    }
                }
                // Estado: lista con datos
                this@Column.AnimatedVisibility(
                    visible = !state.isLoading && state.produccionSemanal.isNotEmpty() && state.sueldosSemanales.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    val produccionAgrupada: List<ProductionDay> =
                        state.produccionSemanal
                            .groupBy { it.fecha }
                            .map { (fecha, lista) ->

                                ProductionDay(
                                    dayName = fecha.toDayName(),
                                    fecha = fecha,
                                    productions = lista
                                )
                            }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {

                        // ── Card de resumen semanal ───────────────────────────
                        item {
                            val semanaActual = state.sueldosSemanales.firstOrNull()
                            if (semanaActual != null) {
                                WeeklySummaryCard(sueldo = semanaActual)
                            }
                        }

                        item {
                            Text(
                                text = "${state.produccionSemanal.size} registros en la semana",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF001427).copy(alpha = 0.7f)
                            )
                        }
                        produccionAgrupada.forEach { dayGroup ->

                            item(key = "header_${dayGroup.fecha}") {
                                DayHeader(
                                    dayName = dayGroup.dayName,
                                    count = dayGroup.productions.size
                                )
                            }

                            itemsIndexed(
                                items = dayGroup.productions,
                                key = { _, produccion -> produccion.idProduccion }
                            ) { index, produccion ->

                                val offsetX = remember { Animatable(300f) }
                                val alpha = remember { Animatable(0f) }

                                LaunchedEffect(Unit) {
                                    launch {
                                        offsetX.animateTo(
                                            0f,
                                            tween(500, delayMillis = index * 80)
                                        )
                                    }
                                    launch {
                                        alpha.animateTo(
                                            1f,
                                            tween(400, delayMillis = index * 80)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .offset(x = offsetX.value.dp)
                                        .graphicsLayer { this.alpha = alpha.value }
                                ) {

                                    ProductionCard(
                                        produccion = produccion,
                                        prendasDisponibles = state.prendasDisponibles,
                                        onDelete = {
                                            viewModel.eliminarProduccion(
                                                produccion.idProduccion,
                                                idTrabajador
                                            )
                                        },
                                        onEdit = { idPrenda, cantidad ->
                                            viewModel.actualizarProduccion(
                                                idProduccion = produccion.idProduccion,
                                                idTrabajador = idTrabajador,
                                                idPrenda = idPrenda,
                                                cantidad = cantidad
                                            )
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }

                    }
                }
            }

        }
        // ── Dialog para agregar producción ────────────────────────────
        if (showAddDialog) {
            AddProductionDialog(
                prendas = state.prendasDisponibles,
                isCreating = state.isCreating,
                onDismiss = { showAddDialog = false },
                onConfirm = { idPrenda, cantidad ->
                    viewModel.crearProduccion(idTrabajador, idPrenda, cantidad)
                    showAddDialog = false
                }
            )
        }
    }
}