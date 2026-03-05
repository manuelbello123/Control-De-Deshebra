package org.taller.project.AddGarment

import org.taller.project.Models.CatalogoSimpleDto
import org.taller.project.Models.PrecioDto
import org.taller.project.Models.PrendaDto

data class GarmentState(
    val isLoading: Boolean = false,
    val prendas: List<PrendaDto> = emptyList(),

    val piezas: List<CatalogoSimpleDto> = emptyList(),
    val colores: List<CatalogoSimpleDto> = emptyList(),
    val tallas: List<CatalogoSimpleDto> = emptyList(),
    val tipos: List<CatalogoSimpleDto> = emptyList(),
    val modelos: List<CatalogoSimpleDto> = emptyList(),

    val precios: List<PrecioDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false
)