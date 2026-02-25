package org.taller.project.HomeWorker

import org.taller.project.Models.TrabajadorConProduccion

sealed class HomeWorkerResult {
    data class Success(val trabajadores: List<TrabajadorConProduccion>) : HomeWorkerResult()
    data class Error(val message: String) : HomeWorkerResult()
}