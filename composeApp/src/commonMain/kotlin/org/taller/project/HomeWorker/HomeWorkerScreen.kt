package org.taller.project.HomeWorker

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.material3.rememberWideNavigationRailState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.taller.project.Components.AppModule
import org.taller.project.Login.UserRole
import org.taller.project.Models.ProduccionTrabajadorDetalle
import org.taller.project.Models.TrabajadorConProduccion
import org.taller.project.Models.TrabajadorDto
import org.taller.project.Navigation.Routes

@Composable
fun HomeWorkerScreen(
    navController: NavController,
    viewModel: HomeWorkerViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }

    val sessionState by AppModule.sessionManager.sessionState.collectAsState()
    val isAdmin = sessionState.user?.rol == UserRole.ADMIN

    // Cargar trabajadores al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarTrabajadores()
    }

    // Mostrar errores en Snackbar
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
            if (isAdmin) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    AnimatedVisibility(visible = expanded) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.End
                        ) {

                            SmallActionButton(
                                text = "Trabajadores",
                                icon = Icons.Outlined.Person,
                                primaryColor = Color(0xFF001427)
                            ) {
                                expanded = false
                                navController.navigate(Routes.ADD_WORKER)
                            }

                            SmallActionButton(
                                text = "Usuarios",
                                icon = Icons.Outlined.Badge,
                                primaryColor = Color(0xFF001427)
                            ) {
                                expanded = false
                                navController.navigate(Routes.ADD_USER)
                            }

                            SmallActionButton(
                                text = "Prendas",
                                icon = Icons.Outlined.Checkroom,
                                primaryColor = Color(0xFF001427)
                            ) {
                                expanded = false
                                navController.navigate(Routes.ADD_GARMENT)
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = { expanded = !expanded },
                        containerColor = Color(0xFF001427),
                        shape = RoundedCornerShape(18.dp),
                        elevation = FloatingActionButtonDefaults.elevation(6.dp)
                    ) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Outlined.Close
                            else
                                Icons.Outlined.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
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
                // ── Estado: Cargando ──────────────────────────────────
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
                            text = "Cargando trabajadores...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF001427)
                        )
                    }
                }
                // ── Estado: Sin datos ─────────────────────────────────
                this@Column.AnimatedVisibility(
                    visible = !state.isLoading && state.trabajadores.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PersonOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF001427).copy(alpha = 0.5f)
                        )
                        Text(
                            text = "No hay trabajadores activos",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF001427)
                        )
                    }
                }

                // ── Estado: Con datos ─────────────────────────────────
                this@Column.AnimatedVisibility(
                    visible = !state.isLoading && state.trabajadores.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 160.dp)
                    ) {
                        // ── Contador de trabajadores activos ───────────────────────────────
                        item {
                            Text(
                                text = "${state.trabajadores.size} trabajadores activos",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF001427).copy(alpha = 0.7f)
                            )
                        }
                        // ── Lista de trabajadores ──────────────────────────────────
                        itemsIndexed(
                            items = state.trabajadores,
                            key = { _, trabajador -> trabajador.idTrabajador }
                        ) { index, trabajador ->

                            val offsetX = remember { Animatable(300f) }
                            val alpha = remember { Animatable(0f) }

                            LaunchedEffect(trabajador.idTrabajador) {
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
                                HomeWorkerCard(
                                    trabajador = trabajador,
                                    onClick = {
                                        navController.navigate(
                                            ProduccionTrabajadorDetalle(
                                                trabajador.idTrabajador,
                                                trabajador.nombre,
                                                trabajador.usuario
                                            )
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
}