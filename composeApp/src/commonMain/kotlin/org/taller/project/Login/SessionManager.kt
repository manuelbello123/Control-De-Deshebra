package org.taller.project.Login

import kotlinx.coroutines.flow.StateFlow
import org.taller.project.Models.AuthUser
import org.taller.project.Models.SessionState

interface SessionManager {

    val sessionState: StateFlow<SessionState>

    fun startSession(user: AuthUser)

    fun endSession()

    fun getToken(): String?
}