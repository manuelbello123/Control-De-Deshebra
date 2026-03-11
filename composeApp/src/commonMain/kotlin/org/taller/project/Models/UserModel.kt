package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDto(
    @SerialName("id_usuario")    val idUsuario: Int,
    @SerialName("nombre")        val nombre: String,
    @SerialName("username")      val username: String,
    @SerialName("rol")           val rol: String,
    @SerialName("activo")        val activo: Boolean,
    @SerialName("is_deleted")    val isDeleted: Boolean = false,
    @SerialName("id_trabajador") val idTrabajador: Int? = null
)

@Serializable
data class CreateUserRequest(
    val nombre: String,
    val username: String,
    val password: String,
    val rol: String
)

@Serializable
data class UpdateUsuarioRequest(
    val nombre: String,
    val username: String,
    val rol: String,
    val activo: Boolean,
    val id_trabajador: Int?
)