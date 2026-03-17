package org.taller.project.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(username: String, password: String) {

        // Validación
        if (username.isBlank() || password.isBlank()) {
            _state.value = AuthState(error = "Usuario y contraseña obligatorios")
            return
        }

        _state.value = AuthState(isLoading = true)

        viewModelScope.launch {
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

    fun clearSuccessMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }
}