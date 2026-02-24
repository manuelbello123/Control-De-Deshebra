package org.taller.project.Models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TotalSemanalDto(
    @SerialName("semana_iso")      val semanaIso: Int,
    @SerialName("inicio_semana")   val inicioSemana: String,
    @SerialName("fin_semana")      val finSemana: String,
    @SerialName("total_piezas")    val totalPiezas: Int,
    @SerialName("total_sueldo")    val totalSueldo: Double
)