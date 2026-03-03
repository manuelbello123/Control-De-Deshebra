package org.taller.project.AddGarment

import org.taller.project.Models.ColorDto
import org.taller.project.Models.ModeloDto
import org.taller.project.Models.PiezaDto
import org.taller.project.Models.PrecioDto
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.TallaDto
import org.taller.project.Models.TipoDto

data class GarmentState(
    val isLoading: Boolean = false,
    val prendas: List<PrendaDto> = emptyList(),
    val piezas: List<PiezaDto> = emptyList(),
    val colores: List<ColorDto> = emptyList(),
    val tallas: List<TallaDto> = emptyList(),
    val tipos: List<TipoDto> = emptyList(),
    val modelos: List<ModeloDto> = emptyList(),
    val precios: List<PrecioDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
    val isCreating: Boolean = false,
    val isDeleting: Boolean = false
) {
}