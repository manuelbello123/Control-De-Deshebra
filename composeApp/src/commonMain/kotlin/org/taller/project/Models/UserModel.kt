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