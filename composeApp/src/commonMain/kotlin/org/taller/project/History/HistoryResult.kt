package org.taller.project.History

import org.taller.project.Models.ProductionHistory

sealed class HistoryResult {
    data class Success(val historial: List<ProductionHistory>) : HistoryResult()
    data class Error(val message: String) : HistoryResult()
}