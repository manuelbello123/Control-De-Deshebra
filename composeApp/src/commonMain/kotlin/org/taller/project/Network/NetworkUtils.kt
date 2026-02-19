package org.taller.project.Network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.taller.project.Login.InMemorySessionManager


object NetworkUtils {

    // El cliente ahora necesita el SessionManager para leer el token
    fun buildHttpClient(sessionManager: InMemorySessionManager): HttpClient {
        return HttpClient {

            install(ContentNegotiation) {
                json(
                    json = Json { ignoreUnknownKeys = true },
                    contentType = ContentType.Any
                )
            }

            install(Auth) {
                bearer {
                    // Ktor llama a loadTokens antes de cada request protegido
                    loadTokens {
                        val token = sessionManager.getToken()
                        if (token != null) {
                            BearerTokens(
                                accessToken = token,
                                refreshToken = ""   // no manejas refresh por ahora
                            )
                        } else {
                            null    // sin token → Ktor no agrega el header
                        }
                    }
                }
            }
        }
    }
}