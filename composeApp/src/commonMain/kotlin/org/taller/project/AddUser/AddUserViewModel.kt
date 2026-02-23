package org.taller.project.AddUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.taller.project.Models.UsuarioDto

class AddUserViewModel(
    private val repository: AddUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        cargarUsuarios()
    }

    // ── Obtener lista de usuarios ──────────────────────────────────────
    fun cargarUsuarios() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = repository.getUsuarios()) {
                is UsersListResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        usuarios = result.usuarios,
                        error = null
                    )
                }

                is UsersListResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Crear nuevo usuario ────────────────────────────────────────────
    fun crearUsuario(username: String, password: String, rol: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isCreating = true,
                error = null,
                successMessage = null
            )

            when (val result = repository.createUsuario(username, password, rol)) {
                is UserResult.Success -> {
                    val nuevaLista = _state.value.usuarios + result.usuario

                    _state.value = _state.value.copy(
                        isCreating = false,
                        usuarios = nuevaLista,
                        successMessage = "Usuario creado exitosamente",
                        error = null
                    )
                }

                is UserResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Actualizar usuario (toggle activo o edición completa) ──────────
    fun actualizarUsuario(usuario: UsuarioDto) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, error = null)

            when (val result = repository.updateUsuario(
                id = usuario.idUsuario,
                username = usuario.username,
                rol = usuario.rol,
                activo = usuario.activo,
                idTrabajador = usuario.idTrabajador
            )) {
                is UpdateUserResult.Success -> {
                    // Actualizar el usuario en la lista local
                    val nuevaLista = _state.value.usuarios.map {
                        if (it.idUsuario == usuario.idUsuario) usuario else it
                    }

                    _state.value = _state.value.copy(
                        isUpdating = false,
                        usuarios = nuevaLista,
                        successMessage = "Usuario actualizado",
                        error = null
                    )
                }

                is UpdateUserResult.Error -> {
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    // ── Toggle estado activo de un usuario ─────────────────────────────
    fun toggleActivo(usuario: UsuarioDto) {
        val usuarioActualizado = usuario.copy(activo = !usuario.activo)
        actualizarUsuario(usuarioActualizado)
    }

    // ── Eliminar usuario (soft delete) ─────────────────────────────────
    fun eliminarUsuario(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(error = null)

            when (val result = repository.deleteUsuario(id)) {
                is DeleteUserResult.Success -> {
                    // Actualizar el usuario a inactivo en la lista local
                    val nuevaLista = _state.value.usuarios.map {
                        if (it.idUsuario == id) it.copy(activo = false) else it
                    }

                    _state.value = _state.value.copy(
                        usuarios = nuevaLista,
                        successMessage = "Usuario desactivado",
                        error = null
                    )
                }

                is DeleteUserResult.Error -> {
                    _state.value = _state.value.copy(
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
    fun retry() = cargarUsuarios()
}