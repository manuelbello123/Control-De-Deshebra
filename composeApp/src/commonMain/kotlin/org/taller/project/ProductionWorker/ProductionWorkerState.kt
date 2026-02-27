package org.taller.project.ProductionWorker

import org.taller.project.Models.PrendaDto
import org.taller.project.Models.ProduccionExpandida
import org.taller.project.Models.ProductionByDay
import org.taller.project.Models.ProductionDay
import org.taller.project.Models.SueldoSemanalDto

data class ProductionWorkerState(
    val isLoading: Boolean = false,
    val sueldosSemanales: List<SueldoSemanalDto> = emptyList(),
    val produccionSemanal: List<ProduccionExpandida> = emptyList(),
    val produciton: List<ProductionDay> = emptyList(),
    val prendasDisponibles: List<PrendaDto> = emptyList(),  // Para el dropdown
    val error: String? = null,
    val successMessage: String? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false
)