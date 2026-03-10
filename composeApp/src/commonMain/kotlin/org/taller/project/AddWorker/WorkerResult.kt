package org.taller.project.AddWorker

import org.taller.project.Models.TrabajadorDto

sealed class WorkerResult {
    data class Success(val trabajador: TrabajadorDto) : WorkerResult()
    data class Error(val message: String) : WorkerResult()
}

sealed class WorkersListResult {
    data class Success(val trabajadores: List<TrabajadorDto>) : WorkersListResult()
    data class Error(val message: String) : WorkersListResult()
}

sealed class UpdateWorkerResult {
    object Success : UpdateWorkerResult()
    data class Error(val message: String) : UpdateWorkerResult()
}

sealed class DeleteWorkerResult {
    object Success : DeleteWorkerResult()
    data class Error(val message: String) : DeleteWorkerResult()
}
