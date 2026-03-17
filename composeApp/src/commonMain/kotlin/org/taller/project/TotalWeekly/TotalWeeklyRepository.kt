package org.taller.project.TotalWeekly

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import kotlinx.io.IOException
import org.taller.project.Models.TotalSemanalDto

class TotalWeeklyRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.145.5.253"

    // ── GET: Obtener todos los totales semanales ───────────────────────
    suspend fun getTotalesSemanales(): TotalWeeklyResult {
        return try {
            val totales: List<TotalSemanalDto> = client.get("$baseUrl/sueldo/total").body()
            TotalWeeklyResult.Success(totales)

        } catch (e: ClientRequestException) {
            TotalWeeklyResult.Error("Error al obtener totales: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            TotalWeeklyResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            TotalWeeklyResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            TotalWeeklyResult.Error("Error inesperado: ${e.message}")
        }
    }
}