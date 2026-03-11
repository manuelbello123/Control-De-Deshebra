package org.taller.project.Components

import org.taller.project.Login.AuthRepository
import org.taller.project.Login.AuthViewModel
import org.taller.project.Login.InMemorySessionManager
import org.taller.project.Login.SessionManager

object AppModule {

    val sessionManager: SessionManager by lazy {
        InMemorySessionManager()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    fun getAuthViewModel(): AuthViewModel {
        return AuthViewModel(
            authRepository = authRepository,
            sessionManager = sessionManager
        )
    }
}