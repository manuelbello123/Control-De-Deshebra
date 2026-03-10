package org.taller.project.AddWorker

import org.taller.project.Models.TrabajadorDto

data class WorkerState(
    val isLoading: Boolean = false,
    val trabajadores: List<TrabajadorDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false
)