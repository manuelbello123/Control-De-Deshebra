package org.taller.project.History

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.taller.project.Login.InMemorySessionManager
import org.taller.project.Models.ProductionByDay
import org.taller.project.Network.NetworkUtils


class HistoryViewModel(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = repository.getHistorialSemanaActual()) {

                is HistoryResult.Success -> {

                    val agrupado = result.historial
                        .groupBy { it.fecha }
                        .map { (fecha, producciones) ->
                            ProductionByDay(
                                dayName = fecha.toDayName(),
                                fecha = fecha,
                                productions = producciones
                            )
                        }

                    _state.value = _state.value.copy(
                        isLoading = false,
                        historial = agrupado,
                        error = null
                    )
                }

                is HistoryResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
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
    fun retry() = cargarHistorial()
}