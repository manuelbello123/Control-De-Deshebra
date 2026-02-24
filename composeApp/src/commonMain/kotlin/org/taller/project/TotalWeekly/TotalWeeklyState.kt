package org.taller.project.TotalWeekly

import org.taller.project.Models.TotalSemanalDto

data class TotalWeeklyState(
    val isLoading: Boolean = false,
    val totales: List<TotalSemanalDto> = emptyList(),
    val error: String? = null
)