package org.taller.project.AddGarment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddGarmentViewModel(
    private val repository: AddGarmentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GarmentState())
    val state: StateFlow<GarmentState> = _state.asStateFlow()

    fun cargarTodosLosDatos() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            cargarPrendas()
            cargarCatalogos()
        }
    }

    private fun cargarPrendas() {
        viewModelScope.launch {
            when (val result = repository.getPrendas()) {
                is PrendasResult.Success -> {
                    _state.value = _state.value.copy(
                        prendas = result.prendas,
                        isLoading = false
                    )
                }
                is PrendasResult.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun cargarCatalogos() {
        viewModelScope.launch {
            launch { cargarPiezas() }
            launch { cargarColores() }
            launch { cargarTallas() }
            launch { cargarTipos() }
            launch { cargarModelos() }
            launch { cargarPrecios() }
        }
    }

    private fun cargarPiezas() {
        viewModelScope.launch {
            when (val result = repository.getPiezas()) {
                is CatalogosResult.Success -> {
                    _state.value = _state.value.copy(piezas = result.items)
                }
                is CatalogosResult.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
            }
        }
    }

    private fun cargarColores() {
        viewModelScope.launch {
            when (val result = repository.getColores()) {
                is CatalogosResult.Success -> {
                    _state.value = _state.value.copy(colores = result.items)
                }
                is CatalogosResult.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
            }
        }
    }

    private fun cargarTallas() {
        viewModelScope.launch {
            when (val result = repository.getTallas()) {
                is CatalogosResult.Success -> {
                    _state.value = _state.value.copy(tallas = result.items)
                }
                is CatalogosResult.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
            }
        }
    }

    private fun cargarTipos() {
        viewModelScope.launch {
            when (val result = repository.getTipos()) {
                is CatalogosResult.Success -> {
                    _state.value = _state.value.copy(tipos = result.items)
                }
                is CatalogosResult.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
            }
        }
    }

    private fun cargarModelos() {
        viewModelScope.launch {
            when (val result = repository.getModelos()) {
                is CatalogosResult.Success -> {
                    _state.value = _state.value.copy(modelos = result.items)
                }
                is CatalogosResult.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
            }
        }
    }

    private fun cargarPrecios() {
        viewModelScope.launch {
            when (val result = repository.getPrecios()) {
                is CatalogosResult.Success -> {
                    _state.value = _state.value.copy(precios = result.items)
                }
                is CatalogosResult.Error -> {
                    _state.value = _state.value.copy(error = result.message)
                }
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // OPERACIONES DE PRENDAS
    // ═════════════════════════════════════════════════════════════════════

    fun crearPrendas(
        idPieza: Int, idColor: Int, idTalla: Int,
        idTipo: Int, idModelo: Int, idPrecio: Int
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createPrendas(idPieza, idColor, idTalla, idTipo, idModelo, idPrecio)) {
                is CreatePrendaResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Prenda creada exitosamente"
                    )
                    cargarPrendas()
                }
                is CreatePrendaResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarPrendas(idPrenda: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deletePrendas(idPrenda)) {
                is DeletePrendaResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Prenda eliminada exitosamente"
                    )
                    cargarPrendas()
                }
                is DeletePrendaResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun crearPiezas(nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createPiezas(nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Pieza creada exitosamente"
                    )
                    cargarPiezas()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun actualizarPiezas(id: Int, nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.updatePiezas(id, nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Pieza actualizada exitosamente"
                    )
                    cargarPiezas()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarPiezas(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deletePiezas(id)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Pieza eliminada exitosamente"
                    )
                    cargarPiezas()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun crearColores(nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createColores(nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Color creado exitosamente"
                    )
                    cargarColores()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun actualizarColores(id: Int, nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.updateColores(id, nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Color actualizado exitosamente"
                    )
                    cargarColores()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarColores(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deleteColores(id)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Color eliminado exitosamente"
                    )
                    cargarColores()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun crearTallas(nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createTallas(nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Talla creada exitosamente"
                    )
                    cargarTallas()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun actualizarTallas(id: Int, nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.updateTallas(id, nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Talla actualizada exitosamente"
                    )
                    cargarTallas()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarTallas(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deleteTallas(id)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Talla eliminada exitosamente"
                    )
                    cargarTallas()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun crearTipos(nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createTipos(nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Tipo creado exitosamente"
                    )
                    cargarTipos()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun actualizarTipos(id: Int, nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.updateTipos(id, nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Tipo actualizado exitosamente"
                    )
                    cargarTipos()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarTipos(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deleteTipos(id)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Tipo eliminado exitosamente"
                    )
                    cargarTipos()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun crearModelos(nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createModelos(nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Modelo creado exitosamente"
                    )
                    cargarModelos()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun actualizarModelos(id: Int, nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.updateModelos(id, nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Modelo actualizado exitosamente"
                    )
                    cargarModelos()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarModelos(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deleteModelos(id)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Modelo eliminado exitosamente"
                    )
                    cargarModelos()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun crearPrecios(nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.createPrecios(nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Precio creado exitosamente"
                    )
                    cargarPrecios()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun actualizarPrecios(id: Int, nombre: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)

            when (val result = repository.updatePrecios(id, nombre)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        successMessage = "Precio actualizado exitosamente"
                    )
                    cargarPrecios()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isCreating = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun eliminarPrecios(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isDeleting = true, error = null)

            when (val result = repository.deletePrecios(id)) {
                is CrudCatalogoResult.Success -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        successMessage = "Precio eliminado exitosamente"
                    )
                    cargarPrecios()
                }
                is CrudCatalogoResult.Error -> {
                    _state.value = _state.value.copy(
                        isDeleting = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }

    fun retry() = cargarTodosLosDatos()
}