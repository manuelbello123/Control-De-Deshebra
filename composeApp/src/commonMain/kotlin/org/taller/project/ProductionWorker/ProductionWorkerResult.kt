package org.taller.project.ProductionWorker

import org.taller.project.Models.ProduccionExpandida
import org.taller.project.Models.SueldoSemanalDto

sealed class SueldosSemanalesResult {
    data class Success(val sueldos: List<SueldoSemanalDto>) : SueldosSemanalesResult()
    data class Error(val message: String) : SueldosSemanalesResult()
}

sealed class ProduccionSemanalResult {
    data class Success(val producciones: List<ProduccionExpandida>) : ProduccionSemanalResult()
    data class Error(val message: String) : ProduccionSemanalResult()
}

sealed class CreateProduccionResult {
    object Success : CreateProduccionResult()
    data class Error(val message: String) : CreateProduccionResult()
}

sealed class UpdateProduccionResult {
    object Success : UpdateProduccionResult()
    data class Error(val message: String) : UpdateProduccionResult()
}

sealed class DeleteProduccionResult {
    object Success : DeleteProduccionResult()
    data class Error(val message: String) : DeleteProduccionResult()
}
