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
@Serializable
data class ProduccionTrabajadorDetalle(val id: Int, val nombre: String, val usuario: String)
@Serializable
data class ProduccionDetalleDto(
    @SerialName("id_produccion")   val idProduccion: Int,
    @SerialName("id_trabajador")   val idTrabajador: Int,
    @SerialName("id_prenda")       val idPrenda: Int,
    @SerialName("cantidad")        val cantidad: Int,
    @SerialName("fecha")           val fecha: String,
    @SerialName("hora")            val hora: String,
    @SerialName("semana_iso")      val semanaIso: Int,
    @SerialName("capturado_por")   val capturadoPor: Int
)

// ── Request para POST /produccion ──────────────────────────────────────
@Serializable
data class CreateProduccionRequest(
    val id_trabajador: Int,
    val id_prenda: Int,
    val cantidad: Int
)

// ── Request para PUT /produccion/{id} ──────────────────────────────────
@Serializable
data class UpdateProduccionRequest(
    val id_prenda: Int,
    val cantidad: Int
)

// ── Modelo de presentación con datos expandidos ────────────────────────
data class ProduccionExpandida(
    val idProduccion: Int,
    val idTrabajador: Int,
    val cantidad: Int,
    val fecha: String,
    val hora: String,
    val semanaIso: Int,
    // Prenda expandida
    val pieza: String,
    val color: String,
    val talla: String,
    val tipo: String,
    val modelo: String,
    val precio: Double,
    // Usuario que capturó
    val capturadoPor: String  // username
)

data class ProductionDay(
    val dayName: String,
    val fecha: String,
    val productions: List<ProduccionExpandida>
)