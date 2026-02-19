package org.taller.project.History

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.taller.project.Login.InMemorySessionManager
import org.taller.project.Models.ProductionByDay
import org.taller.project.Network.NetworkUtils


class HistoryViewModel(
    private val sessionManager: InMemorySessionManager,
    private val repository: HistoryRepository = HistoryRepository(
        client = NetworkUtils.buildHttpClient(sessionManager)
    )
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        viewModelScope.launch {
            _state.value = HistoryState(isLoading = true)

            try {
                val historial = repository.getHistorialSemanaActual()

                // Agrupar por fecha y convertir a ProductionByDay
                val agrupado = historial
                    .groupBy { it.fecha }  // agrupa por "2026-01-29"
                    .map { (fecha, producciones) ->
                        ProductionByDay(
                            dayName = fecha.toDayName(),
                            fecha = fecha,
                            productions = producciones
                        )
                    }
                    //.sortedByDescending { it.fecha }  // ⬅️ Más reciente primero

                _state.value = HistoryState(
                    isLoading = false,
                    data = agrupado
                )

            } catch (e: ResponseException) {
                val codigo = e.response.status.value
                _state.value = HistoryState(
                    isLoading = false,
                    error = "Error del servidor ($codigo): ${e.message}"
                )

            } catch (e: IOException) {
                _state.value = HistoryState(
                    isLoading = false,
                    error = "Sin conexión a internet. Verifica tu red."
                )

            } catch (e: Exception) {
                _state.value = HistoryState(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    fun retry() = cargarHistorial()
}