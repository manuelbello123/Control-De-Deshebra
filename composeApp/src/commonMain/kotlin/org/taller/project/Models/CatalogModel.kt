package org.taller.project.Models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PiezaDto(
    @SerialName("id_pieza") val idPieza: Int,
    @SerialName("nombre") val nombre: String
)

@Serializable
data class ColorDto(
    @SerialName("id_color") val idColor: Int,
    @SerialName("nombre") val nombre: String
)

@Serializable
data class TallaDto(
    @SerialName("id_talla") val idTalla: Int,
    @SerialName("nombre") val nombre: String
)

@Serializable
data class TipoDto(
    @SerialName("id_tipo") val idTipo: Int,
    @SerialName("nombre") val nombre: String
)

@Serializable
data class ModeloDto(
    @SerialName("id_modelo") val idModelo: Int,
    @SerialName("nombre") val nombre: String
)
@Serializable
data class PrecioDto(
    @SerialName("id_precio") val idPrecio: Int,
    @SerialName("precio") val precio: Double
)

@Serializable
data class CreateCatalogoRequest(val nombre: String)

@Serializable
data class UpdateCatalogoRequest(val nombre: String)

@Serializable
data class CreatePrecioRequest(val precio: Double)

@Serializable
data class UpdatePrecioRequest(val precio: Double)