package org.taller.project.History

import org.taller.project.Models.ProductionByDay
import org.taller.project.Models.ProductionHistory


data class HistoryState(
    val isLoading: Boolean = false,
    val historial: List<ProductionByDay> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
)