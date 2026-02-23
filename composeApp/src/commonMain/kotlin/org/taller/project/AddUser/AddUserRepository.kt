package org.taller.project.AddUser

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
import org.taller.project.Models.CreateUserRequest
import org.taller.project.Models.UpdateUsuarioRequest
import org.taller.project.Models.UsuarioDto

class AddUserRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.131.91.29"

    // ── GET: Obtener todos los usuarios ────────────────────────────────
    suspend fun getUsuarios(): UsersListResult {
        return try {
            val usuarios: List<UsuarioDto> = client.get("$baseUrl/usuarios").body()
            UsersListResult.Success(usuarios)

        } catch (e: ClientRequestException) {
            UsersListResult.Error("Error al obtener usuarios: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            UsersListResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            UsersListResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            UsersListResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── POST: Crear nuevo usuario ──────────────────────────────────────
    suspend fun createUsuario(
        username: String,
        password: String,
        rol: String
    ): UserResult {
        return try {

            if (username.isBlank() || password.isBlank() || rol.isBlank()) {
                return UserResult.Error("Todos los campos son obligatorios")
            }

            if (rol != "ADMIN" && rol != "CAPTURISTA") {
                return UserResult.Error("Rol inválido. Debe ser ADMIN o CAPTURISTA")
            }

            val usuario: UsuarioDto = client.post("$baseUrl/usuarios") {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateUserRequest(
                        username = username,
                        password = password,
                        rol = rol
                    )
                )
            }.body()

            UserResult.Success(usuario)

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                409 -> UserResult.Error("El usuario ya existe")
                400 -> UserResult.Error("Datos inválidos. Verifica los campos.")
                403 -> UserResult.Error("No tienes permisos para crear usuarios")
                else -> UserResult.Error("Error al crear usuario: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            UserResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            UserResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            UserResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── PUT: Actualizar usuario ────────────────────────────────────────
    suspend fun updateUsuario(
        id: Int,
        username: String,
        rol: String,
        activo: Boolean,
        idTrabajador: Int?
    ): UpdateUserResult {
        return try {

            if (username.isBlank() || rol.isBlank()) {
                return UpdateUserResult.Error("Username y rol son obligatorios")
            }

            if (rol != "ADMIN" && rol != "CAPTURISTA") {
                return UpdateUserResult.Error("Rol inválido. Debe ser ADMIN o CAPTURISTA")
            }

            client.put("$baseUrl/usuarios/$id") {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateUsuarioRequest(
                        username = username,
                        rol = rol,
                        activo = activo,
                        id_trabajador = idTrabajador
                    )
                )
            }

            UpdateUserResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> UpdateUserResult.Error("No tienes permisos para actualizar usuarios")
                404 -> UpdateUserResult.Error("Usuario no encontrado")
                400 -> UpdateUserResult.Error("Datos inválidos")
                else -> UpdateUserResult.Error("Error al actualizar: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            UpdateUserResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            UpdateUserResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            UpdateUserResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── DELETE: Eliminar (desactivar) usuario ──────────────────────────
    suspend fun deleteUsuario(id: Int): DeleteUserResult {
        return try {

            client.delete("$baseUrl/usuarios/$id")

            DeleteUserResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> DeleteUserResult.Error("No tienes permisos para eliminar usuarios")
                404 -> DeleteUserResult.Error("Usuario no encontrado")
                else -> DeleteUserResult.Error("Error al eliminar: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            DeleteUserResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            DeleteUserResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            DeleteUserResult.Error("Error inesperado: ${e.message}")
        }
    }
}
