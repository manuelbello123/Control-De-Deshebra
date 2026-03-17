package org.taller.project.Network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.taller.project.Login.SessionManager


object NetworkUtils {
    fun buildHttpClient(sessionManager: SessionManager): HttpClient {
        return HttpClient {

            install(ContentNegotiation) {
                json(
                    json = Json { ignoreUnknownKeys = true },
                    contentType = ContentType.Any
                )
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = sessionManager.getToken()
                        if (token != null) {
                            BearerTokens(
                                accessToken = token,
                                refreshToken = ""
                            )
                        } else {
                            null
                        }
                    }
                }
            }
        }
    }
}