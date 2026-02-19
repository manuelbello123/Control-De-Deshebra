package org.taller.project.Login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.taller.project.Models.AuthState

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(username: String, password: String) {

        if (username.isBlank() || password.isBlank()) {
            _state.value = AuthState(error = "Usuario y contraseña obligatorios")
            return
        }

        _state.value = AuthState(isLoading = true)

        CoroutineScope(Dispatchers.Default).launch {

            when (val result = authRepository.login(username, password)) {

                is AuthResult.Success -> {
                    sessionManager.startSession(result.user)
                    _state.value = AuthState(user = result.user)
                }

                is AuthResult.Error -> {
                    _state.value = AuthState(error = result.message)
                }
            }
        }
    }

    fun logout() {
        sessionManager.endSession()
        _state.value = AuthState()
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}