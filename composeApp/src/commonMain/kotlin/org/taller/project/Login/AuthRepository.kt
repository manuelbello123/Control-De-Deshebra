package org.taller.project.Login

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.taller.project.Models.AuthUser
import org.taller.project.Models.LoginRequest
import org.taller.project.Models.LoginResponse
import org.taller.project.Network.NetworkUtils

class AuthRepository : AuthRepo {

    // Cliente sin Bearer — /auth/login es ruta pública
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                json = Json { ignoreUnknownKeys = true },
                contentType = ContentType.Any
            )
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): AuthResult {
        return try {

            val response: LoginResponse = client.post(
                "http://3.131.91.29/auth/login"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginRequest(
                        username = username,
                        password = password
                    )
                )
            }.body()

            val user = AuthUser(
                username = response.username,
                rol = UserRole.valueOf(response.rol),
                token = response.token
            )

            AuthResult.Success(user)

        } catch (e: ClientRequestException) {
            AuthResult.Error("Usuario o contraseña incorrectos: ${e.response.status.description} ")

        } catch (e: ServerResponseException) {
            AuthResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: SerializationException) {
            AuthResult.Error("Respuesta inválida del servidor")

        } catch (e: IOException) {
            AuthResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            AuthResult.Error("Error inesperado: ${e.message}")
        }
    }
}