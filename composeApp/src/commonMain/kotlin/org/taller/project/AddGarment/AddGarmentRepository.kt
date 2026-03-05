package org.taller.project.AddGarment

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.io.IOException
import org.taller.project.AddUser.UpdateUserResult
import org.taller.project.Models.CatalogoSimpleDto
import org.taller.project.Models.CreateCatalogoRequest
import org.taller.project.Models.CreatePrecioRequest
import org.taller.project.Models.CreatePrendaRequest
import org.taller.project.Models.PrecioDto
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.UpdateCatalogoRequest
import org.taller.project.Models.UpdatePrecioRequest

class AddGarmentRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.131.91.29"

    suspend fun getPrendas(): PrendasResult {
        return try {
            val prendas: List<PrendaDto> = client.get("$baseUrl/prendas").body()
            PrendasResult.Success(prendas)
        } catch (e: ClientRequestException) {
            PrendasResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            PrendasResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            PrendasResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            PrendasResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createPrendas(
        idPieza: Int, idColor: Int, idTalla: Int,
        idTipo: Int, idModelo: Int, idPrecio: Int
    ): CreatePrendaResult {
        return try {
            client.post("$baseUrl/prendas") {
                contentType(ContentType.Application.Json)
                setBody(CreatePrendaRequest(idPieza, idColor, idTalla, idTipo, idModelo, idPrecio))
            }
            CreatePrendaResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                401 -> CreatePrendaResult.Error("No autorizado")
                409 -> CreatePrendaResult.Error("Prenda ya existe")
                else -> CreatePrendaResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CreatePrendaResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CreatePrendaResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CreatePrendaResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updatePrendas(
        idPrenda: Int,
        idPieza: Int,
        idColor: Int,
        idTalla: Int,
        idTipo: Int,
        idModelo: Int,
        idPrecio: Int
    ): UpdatePrendaResult {
        return try {

            client.put("$baseUrl/prendas/$idPrenda") {
                contentType(ContentType.Application.Json)
                setBody(
                    CreatePrendaRequest(
                        id_pieza = idPieza,
                        id_color = idColor,
                        id_talla = idTalla,
                        id_tipo = idTipo,
                        id_modelo = idModelo,
                        id_precio = idPrecio
                    )
                )
            }

            UpdatePrendaResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                404 -> UpdatePrendaResult.Error("Prenda no encontrada")
                401 -> UpdatePrendaResult.Error("No autorizado")
                400 -> UpdatePrendaResult.Error("Datos inválidos")
                else -> UpdatePrendaResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            UpdatePrendaResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            UpdatePrendaResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            UpdatePrendaResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deletePrendas(idPrenda: Int): DeletePrendaResult {
        return try {
            client.delete("$baseUrl/prendas/$idPrenda")
            DeletePrendaResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                404 -> DeletePrendaResult.Error("Prenda no encontrada")
                401 -> DeletePrendaResult.Error("No autorizado")
                else -> DeletePrendaResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            DeletePrendaResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            DeletePrendaResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            DeletePrendaResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getPiezas(): CatalogosResult<CatalogoSimpleDto> {
        return try {
            val items: List<CatalogoSimpleDto> = client.get("$baseUrl/piezas").body()
            CatalogosResult.Success(items)
        } catch (e: ClientRequestException) {
            CatalogosResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            CatalogosResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CatalogosResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CatalogosResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createPiezas(nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.post("$baseUrl/piezas") {
                contentType(ContentType.Application.Json)
                setBody(CreateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede crear")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updatePiezas(id: Int, nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.put("$baseUrl/piezas/$id") {
                contentType(ContentType.Application.Json)
                setBody(UpdateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede actualizar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deletePiezas(id: Int): CrudCatalogoResult {
        return try {
            client.delete("$baseUrl/piezas/$id")
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede eliminar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getColores(): CatalogosResult<CatalogoSimpleDto> {
        return try {
            val items: List<CatalogoSimpleDto> = client.get("$baseUrl/colores").body()
            CatalogosResult.Success(items)
        } catch (e: ClientRequestException) {
            CatalogosResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            CatalogosResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CatalogosResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CatalogosResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createColores(nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.post("$baseUrl/colores") {
                contentType(ContentType.Application.Json)
                setBody(CreateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede crear")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updateColores(id: Int, nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.put("$baseUrl/colores/$id") {
                contentType(ContentType.Application.Json)
                setBody(UpdateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede actualizar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteColores(id: Int): CrudCatalogoResult {
        return try {
            client.delete("$baseUrl/colores/$id")
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede eliminar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getTallas(): CatalogosResult<CatalogoSimpleDto> {
        return try {
            val items: List<CatalogoSimpleDto> = client.get("$baseUrl/tallas").body()
            CatalogosResult.Success(items)
        } catch (e: ClientRequestException) {
            CatalogosResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            CatalogosResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CatalogosResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CatalogosResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createTallas(nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.post("$baseUrl/tallas") {
                contentType(ContentType.Application.Json)
                setBody(CreateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede crear")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updateTallas(id: Int, nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.put("$baseUrl/tallas/$id") {
                contentType(ContentType.Application.Json)
                setBody(UpdateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede actualizar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteTallas(id: Int): CrudCatalogoResult {
        return try {
            client.delete("$baseUrl/tallas/$id")
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede eliminar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getTipos(): CatalogosResult<CatalogoSimpleDto> {
        return try {
            val items: List<CatalogoSimpleDto> = client.get("$baseUrl/tipos").body()
            CatalogosResult.Success(items)
        } catch (e: ClientRequestException) {
            CatalogosResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            CatalogosResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CatalogosResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CatalogosResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createTipos(nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.post("$baseUrl/tipos") {
                contentType(ContentType.Application.Json)
                setBody(CreateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede crear")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updateTipos(id: Int, nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.put("$baseUrl/tipos/$id") {
                contentType(ContentType.Application.Json)
                setBody(UpdateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede actualizar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteTipos(id: Int): CrudCatalogoResult {
        return try {
            client.delete("$baseUrl/tipos/$id")
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede eliminar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getModelos(): CatalogosResult<CatalogoSimpleDto> {
        return try {
            val items: List<CatalogoSimpleDto> = client.get("$baseUrl/modelos").body()
            CatalogosResult.Success(items)
        } catch (e: ClientRequestException) {
            CatalogosResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            CatalogosResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CatalogosResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CatalogosResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createModelos(nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.post("$baseUrl/modelos") {
                contentType(ContentType.Application.Json)
                setBody(CreateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede crear")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updateModelos(id: Int, nombre: String): CrudCatalogoResult {
        return try {
            if (nombre.isBlank()) return CrudCatalogoResult.Error("El nombre no puede estar vacío")
            client.put("$baseUrl/modelos/$id") {
                contentType(ContentType.Application.Json)
                setBody(UpdateCatalogoRequest(nombre))
            }
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede actualizar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteModelos(id: Int): CrudCatalogoResult {
        return try {
            client.delete("$baseUrl/modelos/$id")
            CrudCatalogoResult.Success
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede eliminar")
                404 -> CrudCatalogoResult.Error("No encontrado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getPrecios(): CatalogosResult<PrecioDto> {
        return try {
            val items: List<PrecioDto> = client.get("$baseUrl/precios").body()
            CatalogosResult.Success(items)
        } catch (e: ClientRequestException) {
            CatalogosResult.Error("Error: ${e.response.status.description}")
        } catch (e: ServerResponseException) {
            CatalogosResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CatalogosResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CatalogosResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun createPrecios(precio: Double): CrudCatalogoResult {
        return try {
            if (precio <= 0) return CrudCatalogoResult.Error("El precio debe ser mayor a 0")

            client.post("$baseUrl/precios") {
                contentType(ContentType.Application.Json)
                setBody(CreatePrecioRequest(precio = precio))
            }

            CrudCatalogoResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede crear precios")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun updatePrecios(id: Int, precio: Double): CrudCatalogoResult {
        return try {
            if (precio <= 0) return CrudCatalogoResult.Error("El precio debe ser mayor a 0")

            client.put("$baseUrl/precios/$id") {
                contentType(ContentType.Application.Json)
                setBody(UpdatePrecioRequest(precio = precio))
            }

            CrudCatalogoResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede actualizar precios")
                404 -> CrudCatalogoResult.Error("Precio no encontrado")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deletePrecios(id: Int): CrudCatalogoResult {
        return try {
            client.delete("$baseUrl/precios/$id")

            CrudCatalogoResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> CrudCatalogoResult.Error("Solo ADMIN puede eliminar precios")
                404 -> CrudCatalogoResult.Error("Precio no encontrado")
                401 -> CrudCatalogoResult.Error("No autorizado")
                else -> CrudCatalogoResult.Error("Error: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            CrudCatalogoResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CrudCatalogoResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CrudCatalogoResult.Error("Error inesperado: ${e.message}")
        }
    }
}