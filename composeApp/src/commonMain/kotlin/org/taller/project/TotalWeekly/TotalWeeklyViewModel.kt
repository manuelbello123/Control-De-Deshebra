package org.taller.project.TotalWeekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TotalWeeklyViewModel(
    private val repository: TotalWeeklyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TotalWeeklyState())
    val state: StateFlow<TotalWeeklyState> = _state.asStateFlow()

    init {
        cargarTotales()
    }

    // ── Obtener totales semanales ──────────────────────────────────────
    fun cargarTotales() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getTotalesSemanales()) {
                is TotalWeeklyResult.Success -> {

                    _state.value = _state.value.copy(
                        isLoading = false,
                        totales = result.totales,
                        error = null
                    )
                }

                is TotalWeeklyResult.Error -> {
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
    fun retry() = cargarTotales()
}