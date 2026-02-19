package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProduccionDto(
    @SerialName("id_produccion")   val idProduccion: Int,
    @SerialName("id_trabajador")   val idTrabajador: Int,
    @SerialName("id_prenda")       val idPrenda: Int,
    @SerialName("cantidad")        val cantidad: Int,
    @SerialName("fecha")           val fecha: String,
    @SerialName("hora")            val hora: String,
    @SerialName("semana_iso")      val semanaIso: Int,
    @SerialName("capturado_por")   val capturadoPor: Int
)