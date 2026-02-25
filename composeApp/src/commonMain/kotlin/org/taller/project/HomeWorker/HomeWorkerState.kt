package org.taller.project.HomeWorker

import org.taller.project.Models.TrabajadorConProduccion

data class HomeWorkerState(
    val isLoading: Boolean = false,
    val trabajadores: List<TrabajadorConProduccion> = emptyList(),
    val error: String? = null
)