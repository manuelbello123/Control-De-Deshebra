package org.taller.project.History

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.ProduccionDto
import org.taller.project.Models.ProductionHistory
import org.taller.project.Models.TrabajadorDto
import org.taller.project.Models.UsuarioDto
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock


class HistoryRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.145.5.253"
    private fun semanaIsoActual(): Int {
        val hoy = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val diaSemana = hoy.dayOfWeek.isoDayNumber
        val semanaNum = (hoy.dayOfYear - diaSemana + 10) / 7
        val semana = when {
            semanaNum < 1  -> 52
            semanaNum > 52 -> 1
            else           -> semanaNum
        }
        return hoy.year * 100 + semana
    }

    suspend fun getHistorialSemanaActual(): HistoryResult {
        return try {

            coroutineScope {

                val semana = semanaIsoActual()

                val produccionDeferred = async {
                    client.get("$baseUrl/produccion/semana/$semana")
                        .body<List<ProduccionDto>>()
                }

                val trabajadoresDeferred = async {
                    client.get("$baseUrl/trabajadores")
                        .body<List<TrabajadorDto>>()
                }

                val prendasDeferred = async {
                    client.get("$baseUrl/prendas")
                        .body<List<PrendaDto>>()
                }

                val usuariosDeferred = async {
                    client.get("$baseUrl/usuarios")
                        .body<List<UsuarioDto>>()
                }

                val producciones  = produccionDeferred.await()
                val trabajadores  = trabajadoresDeferred.await()
                val prendas       = prendasDeferred.await()
                val usuarios      = usuariosDeferred.await()

                val trabajadoresMap = trabajadores.associateBy { it.idTrabajador }
                val prendasMap      = prendas.associateBy { it.idPrenda }
                val usuariosMap     = usuarios.associateBy { it.idUsuario }

                val historial = producciones.mapNotNull { prod ->
                    val trabajador = trabajadoresMap[prod.idTrabajador] ?: return@mapNotNull null
                    val prenda     = prendasMap[prod.idPrenda] ?: return@mapNotNull null
                    val usuario    = usuariosMap[prod.capturadoPor] ?: return@mapNotNull null

                    ProductionHistory(
                        idProduccion     = prod.idProduccion,
                        nombreTrabajador = trabajador.nombre,
                        usuarioTrabajador= trabajador.usuario,
                        pieza            = prenda.pieza,
                        color            = prenda.color,
                        talla            = prenda.talla,
                        tipo             = prenda.tipo,
                        modelo           = prenda.modelo,
                        cantidad         = prod.cantidad,
                        fecha            = prod.fecha,
                        hora             = prod.hora,
                        capturadoPor     = usuario.username,
                        semanaIso        = prod.semanaIso
                    )
                }

                HistoryResult.Success(historial)
            }

        } catch (e: ClientRequestException) {
            HistoryResult.Error("Error al obtener historial: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            HistoryResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            HistoryResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            HistoryResult.Error("Error inesperado: ${e.message}")
        }
    }
}