package org.taller.project.AddWorker

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
import org.taller.project.Models.CreateWorkerRequest
import org.taller.project.Models.TrabajadorDto
import org.taller.project.Models.UpdateTrabajadorRequest

class AddWorkerRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.131.91.29"

    // ── GET: Obtener todos los trabajadores ────────────────────────────
    suspend fun getTrabajadores(): WorkersListResult {
        return try {
            val trabajadores: List<TrabajadorDto> = client.get("$baseUrl/trabajadores").body()
            WorkersListResult.Success(trabajadores)

        } catch (e: ClientRequestException) {
            WorkersListResult.Error("Error al obtener trabajadores: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            WorkersListResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            WorkersListResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            WorkersListResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── POST: Crear nuevo trabajador ───────────────────────────────────
    suspend fun createTrabajador(nombre: String, usuario: String): WorkerResult {
        return try {

            if (nombre.isBlank() || usuario.isBlank()) {
                return WorkerResult.Error("Nombre y apellidos son obligatorios")
            }

            val trabajador: TrabajadorDto = client.post("$baseUrl/trabajadores") {
                contentType(ContentType.Application.Json)
                setBody(CreateWorkerRequest(nombre = nombre, usuario = usuario))
            }.body()

            WorkerResult.Success(trabajador)

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                400 -> WorkerResult.Error("Datos inválidos. Verifica el nombre y los apellidos.")
                else -> WorkerResult.Error("Error al crear trabajador: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            WorkerResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            WorkerResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            WorkerResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── PUT: Actualizar trabajador ─────────────────────────────────────
    suspend fun updateTrabajador(
        id: Int,
        nombre: String,
        usuario: String,
        activo: Boolean
    ): UpdateWorkerResult {
        return try {

            if (nombre.isBlank() || usuario.isBlank()) {
                return UpdateWorkerResult.Error("Nombre y apellidos son obligatorios")
            }

            client.put("$baseUrl/trabajadores/$id") {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateTrabajadorRequest(
                        nombre = nombre,
                        usuario = usuario,
                        activo = activo
                    )
                )
            }

            UpdateWorkerResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> UpdateWorkerResult.Error("No tienes permisos para actualizar trabajadores")
                404 -> UpdateWorkerResult.Error("Trabajador no encontrado")
                400 -> UpdateWorkerResult.Error("Datos inválidos")
                else -> UpdateWorkerResult.Error("Error al actualizar: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            UpdateWorkerResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            UpdateWorkerResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            UpdateWorkerResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── DELETE: Eliminar (desactivar) trabajador ───────────────────────
    suspend fun deleteTrabajador(id: Int): DeleteWorkerResult {
        return try {

            client.delete("$baseUrl/trabajadores/$id")

            DeleteWorkerResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> DeleteWorkerResult.Error("No tienes permisos para eliminar trabajadores")
                404 -> DeleteWorkerResult.Error("Trabajador no encontrado o ya eliminado")
                401 -> DeleteWorkerResult.Error("No autorizado")
                else -> DeleteWorkerResult.Error("Error al eliminar: ${e.response.status.description}")
            }
        } catch (e: ServerResponseException) {
            DeleteWorkerResult.Error("Error del servidor. Intenta más tarde.")
        } catch (e: IOException) {
            DeleteWorkerResult.Error("Sin conexión a internet. Verifica tu red.")
        } catch (e: Exception) {
            DeleteWorkerResult.Error("Error inesperado: ${e.message}")
        }
    }
}