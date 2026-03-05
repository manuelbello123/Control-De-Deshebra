package org.taller.project.Models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CatalogoSimpleDto(
    @SerialName("id") val id: Int,
    @SerialName("nombre") val nombre: String
)
@Serializable
data class PrecioDto(
    @SerialName("id") val id: Int,
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