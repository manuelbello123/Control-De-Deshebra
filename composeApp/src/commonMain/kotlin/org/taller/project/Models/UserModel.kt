package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDto(
    @SerialName("id_usuario")    val idUsuario: Int,
    @SerialName("username")      val username: String,
    @SerialName("rol")           val rol: String,
    @SerialName("activo")        val activo: Boolean,
    @SerialName("id_trabajador") val idTrabajador: Int? = null
)

@Serializable
data class CreateUserRequest(
    val username: String,
    val password: String,
    val rol: String  // "ADMIN" o "CAPTURISTA"
)

@Serializable
data class UpdateUsuarioRequest(
    val username: String,
    val rol: String,
    val activo: Boolean,
    val id_trabajador: Int?
)