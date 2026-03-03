package org.taller.project.AddGarment

sealed class CatalogosResult<T> {
    data class Success<T>(val items: List<T>) : CatalogosResult<T>()
    data class Error<T>(val message: String) : CatalogosResult<T>()
}

sealed class CrudCatalogoResult {
    object Success : CrudCatalogoResult()
    data class Error(val message: String) : CrudCatalogoResult()
}