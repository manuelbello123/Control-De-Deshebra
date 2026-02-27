package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SueldoSemanalDto(
    @SerialName("id_trabajador")   val idTrabajador: Int,
    @SerialName("trabajador")      val trabajador: String,
    @SerialName("semana_iso")      val semanaIso: Int,
    @SerialName("inicio_semana")   val inicioSemana: String,
    @SerialName("fin_semana")      val finSemana: String,
    @SerialName("total_prendas")   val totalPrendas: Int,
    @SerialName("sueldo_semanal")  val sueldoSemanal: Double
)