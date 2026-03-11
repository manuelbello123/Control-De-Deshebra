package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SueldoDiarioDto(
    @SerialName("id_trabajador")   val idTrabajador: Int,
    @SerialName("trabajador")      val trabajador: String,
    @SerialName("fecha")           val fecha: String,
    @SerialName("total_prendas")   val totalPrendas: Int,
    @SerialName("sueldo_diario")   val sueldoDiario: Double
)

data class TrabajadorConProduccion(
    val idTrabajador: Int,
    val nombre: String,
    val usuario: String,
    val totalPrendas: Int,
    val sueldoDiario: Double,
    val fecha: String
)