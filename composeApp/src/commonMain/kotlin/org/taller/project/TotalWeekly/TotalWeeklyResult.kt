package org.taller.project.TotalWeekly

import org.taller.project.Models.TotalSemanalDto

sealed class TotalWeeklyResult {
    data class Success(val totales: List<TotalSemanalDto>) : TotalWeeklyResult()
    data class Error(val message: String) : TotalWeeklyResult()
}