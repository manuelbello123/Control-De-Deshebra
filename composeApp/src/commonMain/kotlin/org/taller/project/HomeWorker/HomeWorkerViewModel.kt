package org.taller.project.HomeWorker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeWorkerViewModel(
    private val repository: HomeWorkerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeWorkerState())
    val state: StateFlow<HomeWorkerState> = _state.asStateFlow()

    // ── Obtener trabajadores activos con producción del día ────────────
    fun cargarTrabajadores() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getTrabajadoresConProduccion()) {
                is HomeWorkerResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        trabajadores = result.trabajadores,
                        error = null
                    )
                }

                is HomeWorkerResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Limpiar error ──────────────────────────────────────────────────
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    // ── Retry ──────────────────────────────────────────────────────────
    fun retry() = cargarTrabajadores()
}