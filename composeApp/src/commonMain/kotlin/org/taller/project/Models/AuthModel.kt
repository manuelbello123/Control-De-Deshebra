package org.taller.project.Models

import kotlinx.serialization.Serializable
import org.taller.project.Login.UserRole

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val rol: String,
    val username: String
)

@Serializable
data class AuthUser(
    val username: String,
    val rol: UserRole,
    val token: String
)



@Serializable
data class Users(
    val id_usuario: Int,
    val username: String,
    val rol: String,
    val activo: Boolean,
    val id_trabajador: Int? = null,
    val created_at: String? = null
)
