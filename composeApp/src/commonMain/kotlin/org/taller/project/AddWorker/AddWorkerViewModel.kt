package org.taller.project.AddWorker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.taller.project.Models.CreateWorkerRequest
import org.taller.project.Models.TrabajadorDto


class AddWorkerViewModel(
    private val repository: AddWorkerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WorkerState())
    val state: StateFlow<WorkerState> = _state.asStateFlow()

    // ── Obtener lista de trabajadores ──────────────────────────────────
    fun cargarTrabajadores() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getTrabajadores()) {
                is WorkersListResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        trabajadores = result.trabajadores,
                        error = null
                    )
                }

                is WorkersListResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Crear nuevo trabajador ─────────────────────────────────────────
    fun crearTrabajador(nombre: String, usuario: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isCreating = true,
                error = null,
                successMessage = null
            )

            when (val result = repository.createTrabajador(nombre, usuario)) {
                is WorkerResult.Success -> {
                    val nuevaLista = _state.value.trabajadores + result.trabajador

                    _state.value = _state.value.copy(
                        isCreating = false,
                        trabajadores = nuevaLista,
                        successMessage = "Trabajador creado exitosamente",
                        error = null
                    )
                }

                is WorkerResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Actualizar trabajador (toggle activo o edición completa) ───────
    fun actualizarTrabajador(trabajador: TrabajadorDto) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, error = null)

            when (val result = repository.updateTrabajador(
                id = trabajador.idTrabajador,
                nombre = trabajador.nombre,
                usuario = trabajador.usuario,
                activo = trabajador.activo
            )) {
                is UpdateWorkerResult.Success -> {
                    // Actualizar el trabajador en la lista local
                    val nuevaLista = _state.value.trabajadores.map {
                        if (it.idTrabajador == trabajador.idTrabajador) trabajador else it
                    }

                    _state.value = _state.value.copy(
                        isUpdating = false,
                        trabajadores = nuevaLista,
                        successMessage = "Trabajador actualizado",
                        error = null
                    )
                }

                is UpdateWorkerResult.Error -> {
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Toggle estado activo de un trabajador ──────────────────────────
    fun toggleActivo(trabajador: TrabajadorDto) {
        val trabajadorActualizado = trabajador.copy(activo = !trabajador.activo)
        actualizarTrabajador(trabajadorActualizado)
    }

    // ── Eliminar trabajador (soft delete) ──────────────────────────────
    fun eliminarTrabajador(idTrabajador: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deleteTrabajador(idTrabajador)) {
                is DeleteWorkerResult.Success -> {
                    // Eliminar el trabajador de la lista local
                    val nuevaLista = _state.value.trabajadores.filter {
                        it.idTrabajador != idTrabajador
                    }

                    _state.value = _state.value.copy(
                        isDeleting = false,
                        trabajadores = nuevaLista,
                        successMessage = "Trabajador eliminado correctamente",
                        error = null
                    )
                }

                is DeleteWorkerResult.Error -> {
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
    fun retry() = cargarTrabajadores()
}