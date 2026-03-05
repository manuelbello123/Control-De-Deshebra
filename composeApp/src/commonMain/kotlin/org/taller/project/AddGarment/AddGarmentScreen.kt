package org.taller.project.AddGarment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun AddGarmentScreen(
    navController: NavController,
    viewModel: AddGarmentViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCatalogo by remember { mutableStateOf(TipoCatalogo.PIEZAS) }
    var showAddPrendaDialog by remember { mutableStateOf(false) }
    var showAddCatalogoDialog by remember { mutableStateOf(false) }

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarTodosLosDatos()
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
                viewModel.retry()
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
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) {
                        showAddPrendaDialog = true
                    } else {
                        showAddCatalogoDialog = true
                    }
                },
                containerColor = Color(0xFF001427),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Agregar")
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

            // ── Tabs ──────────────────────────────────────────────────
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF001427),
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .height(3.dp)
                            .background(Color(0xFF001427))
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Prendas",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Categorías",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            // ── Contenido según tab ───────────────────────────────────
            when (selectedTab) {
                0 -> PrendasTab(
                    state = state,
                    viewModel = viewModel
                )
                1 -> CatalogosTab(
                    state = state,
                    viewModel = viewModel,
                    selectedCatalogo = selectedCatalogo,
                    onCatalogoChange = { selectedCatalogo = it }
                )
            }
        }

        // ── Dialogs ───────────────────────────────────────────────────
        if (showAddPrendaDialog) {
            AddPrendaDialog(
                state = state,
                isCreating = state.isCreating,
                onDismiss = { showAddPrendaDialog = false },
                onConfirm = { pieza, color, talla, tipo, modelo, precio ->
                    viewModel.crearPrendas(pieza, color, talla, tipo, modelo, precio)
                    showAddPrendaDialog = false
                }
            )
        }

        if (showAddCatalogoDialog) {
            AddCatalogoDialog(
                tipo = selectedCatalogo,
                isCreating = state.isCreating,
                onDismiss = { showAddCatalogoDialog = false },
                onConfirm = { nombre, precio ->
                    when (selectedCatalogo) {
                        TipoCatalogo.PIEZAS -> viewModel.crearPiezas(nombre)
                        TipoCatalogo.COLORES -> viewModel.crearColores(nombre)
                        TipoCatalogo.TALLAS -> viewModel.crearTallas(nombre)
                        TipoCatalogo.TIPOS -> viewModel.crearTipos(nombre)
                        TipoCatalogo.MODELOS -> viewModel.crearModelos(nombre)
                        TipoCatalogo.PRECIOS -> viewModel.crearPrecios(precio)
                    }
                    showAddCatalogoDialog = false
                }
            )
        }
    }
}
