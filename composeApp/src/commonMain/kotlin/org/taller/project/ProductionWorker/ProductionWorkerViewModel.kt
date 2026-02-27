package org.taller.project.ProductionWorker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductionWorkerViewModel(
    private val repository: ProductionWorkerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductionWorkerState())
    val state: StateFlow<ProductionWorkerState> = _state.asStateFlow()


    // ── Cargar sueldos semanales del trabajador ────────────────────────
    fun cargarSueldosSemanales(idTrabajador: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getSueldosSemanales(idTrabajador)) {
                is SueldosSemanalesResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        sueldosSemanales = result.sueldos,
                        error = null
                    )
                }

                is SueldosSemanalesResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Cargar producción de la semana actual ──────────────────────────
    fun cargarProduccionSemanaActual(idTrabajador: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getProduccionSemanaActual(idTrabajador)) {
                is ProduccionSemanalResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        produccionSemanal = result.producciones,
                        error = null
                    )
                }

                is ProduccionSemanalResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Cargar ambos datos en paralelo ─────────────────────────────────
    fun cargarDatosCompletos(idTrabajador: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            // Lanzar todas las cargas
            cargarSueldosSemanales(idTrabajador)
            cargarProduccionSemanaActual(idTrabajador)
            cargarPrendasDisponibles()
        }
    }

    // ── Cargar prendas disponibles para dropdown ───────────────────────
    private fun cargarPrendasDisponibles() {
        viewModelScope.launch {
            val prendas = repository.getPrendasDisponibles()
            _state.value = _state.value.copy(prendasDisponibles = prendas)
        }
    }

    // ── Crear nueva producción ─────────────────────────────────────────
    fun crearProduccion(idTrabajador: Int, idPrenda: Int, cantidad: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isCreating = true,
                error = null,
                successMessage = null
            )

            when (val result = repository.createProduccion(idTrabajador, idPrenda, cantidad)) {
                is CreateProduccionResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Producción registrada exitosamente",
                        error = null
                    )
                    // Recargar la producción semanal para ver el nuevo registro
                    cargarProduccionSemanaActual(idTrabajador)
                }

                is CreateProduccionResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Actualizar producción ──────────────────────────────────────────
    fun actualizarProduccion(
        idProduccion: Int,
        idTrabajador: Int,
        idPrenda: Int,
        cantidad: Int
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isUpdating = true,
                error = null,
                successMessage = null
            )

            when (val result = repository.updateProduccion(idProduccion, idPrenda, cantidad)) {
                is UpdateProduccionResult.Success -> {
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        successMessage = "Producción actualizada exitosamente",
                        error = null
                    )
                    // Recargar producción
                    cargarProduccionSemanaActual(idTrabajador)
                }

                is UpdateProduccionResult.Error -> {
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Eliminar producción ────────────────────────────────────────────
    fun eliminarProduccion(idProduccion: Int, idTrabajador: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isDeleting = true,
                error = null,
                successMessage = null
            )

            when (val result = repository.deleteProduccion(idProduccion)) {
                is DeleteProduccionResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Producción eliminada exitosamente",
                        error = null
                    )
                    // Recargar producción
                    cargarProduccionSemanaActual(idTrabajador)
                }

                is DeleteProduccionResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Limpiar mensajes ───────────────────────────────────────────────
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }

    // ── Retry ──────────────────────────────────────────────────────────
    fun retry(idTrabajador: Int) {
        cargarDatosCompletos(idTrabajador)
    }
}