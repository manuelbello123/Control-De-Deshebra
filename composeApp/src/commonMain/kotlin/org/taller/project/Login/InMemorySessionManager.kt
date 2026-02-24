package org.taller.project.Login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.taller.project.Models.AuthUser

class InMemorySessionManager : SessionManager {

    private val _sessionState = MutableStateFlow(SessionState())
    override val sessionState: StateFlow<SessionState> = _sessionState

    override fun startSession(user: AuthUser) {
        _sessionState.value = SessionState(user)
    }

    override fun endSession() {
        _sessionState.value = SessionState()
    }

    override fun getToken(): String? {
        return _sessionState.value.user?.token
    }
}