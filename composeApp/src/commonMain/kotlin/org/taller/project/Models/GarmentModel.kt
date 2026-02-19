package org.taller.project.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrendaDto(
    @SerialName("id_prenda")  val idPrenda: Int,
    @SerialName("pieza")      val pieza: String,
    @SerialName("color")      val color: String,
    @SerialName("talla")      val talla: String,
    @SerialName("tipo")       val tipo: String,
    @SerialName("modelo")     val modelo: String,
    @SerialName("precio")     val precio: Double
)