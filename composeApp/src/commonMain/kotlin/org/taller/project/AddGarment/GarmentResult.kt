package org.taller.project.AddGarment

import org.taller.project.Models.PrendaDto

sealed class PrendasResult {
    data class Success(val prendas: List<PrendaDto>) : PrendasResult()
    data class Error(val message: String) : PrendasResult()
}

sealed class CreatePrendaResult {
    object Success : CreatePrendaResult()
    data class Error(val message: String) : CreatePrendaResult()
}

sealed class DeletePrendaResult {
    object Success : DeletePrendaResult()
    data class Error(val message: String) : DeletePrendaResult()
}