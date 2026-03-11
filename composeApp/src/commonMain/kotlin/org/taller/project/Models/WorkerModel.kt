package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrabajadorDto(
    @SerialName("id_trabajador")   val idTrabajador: Int,
    @SerialName("nombre")          val nombre: String,
    @SerialName("usuario")         val usuario: String,
    @SerialName("activo")          val activo: Boolean,
    @SerialName("is_deleted")      val isDeleted: Boolean = false
)
@Serializable
data class CreateWorkerRequest(
    val nombre: String,
    val usuario: String? = null
)
@Serializable
data class UpdateTrabajadorRequest(
    val nombre: String,
    val usuario: String? = null,
    val activo: Boolean
)
