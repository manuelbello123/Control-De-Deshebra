package org.taller.project.Login

import kotlinx.serialization.Serializable
import org.taller.project.Models.AuthUser

@Serializable
data class AuthState(
    val isLoading: Boolean = false,
    val user: AuthUser? = null,
    val error: String? = null,
    val successMessage: String? = null
)

@Serializable
data class SessionState(
    val user: AuthUser? = null
) {
    val isLoggedIn: Boolean
        get() = user != null
}
