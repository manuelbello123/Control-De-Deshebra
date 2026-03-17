package org.taller.project.HomeWorker

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import org.taller.project.Models.SueldoDiarioDto
import org.taller.project.Models.TrabajadorConProduccion
import org.taller.project.Models.TrabajadorDto
import kotlin.time.Clock

class HomeWorkerRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.145.5.253"

    // ── GET: Obtener trabajadores activos con su producción del día ────
    suspend fun getTrabajadoresConProduccion(): HomeWorkerResult = supervisorScope  {
        return@supervisorScope  try {

            // Llamadas paralelas
            val trabajadoresDeferred = async {
                client.get("$baseUrl/trabajadores").body<List<TrabajadorDto>>()
            }
            val sueldosDeferred = async {
                client.get("$baseUrl/sueldo/diario").body<List<SueldoDiarioDto>>()
            }

            val trabajadores = trabajadoresDeferred.await()
            val sueldos = sueldosDeferred.await()

            // Obtener fecha actual en formato yyyy-MM-dd
            val fechaHoy = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
                .toString()

            // Filtrar solo trabajadores activos
            val trabajadoresActivos = trabajadores.filter { it.activo }

            // Mapa de sueldos por id_trabajador
            // Filtrar solo sueldos del día actual
            val sueldosHoy = sueldos.filter { it.fecha == fechaHoy }

            // Mapa solo del día actual
            val sueldosMap = sueldosHoy.associateBy { it.idTrabajador }

            // Combinar: para cada trabajador activo, buscar su sueldo del día
            val resultado = trabajadoresActivos.map { trabajador ->

                val sueldoDelDia = sueldosMap[trabajador.idTrabajador]

                TrabajadorConProduccion(
                    idTrabajador = trabajador.idTrabajador,
                    nombre = trabajador.nombre,
                    usuario = trabajador.usuario,
                    totalPrendas = sueldoDelDia?.totalPrendas ?: 0,
                    sueldoDiario = sueldoDelDia?.sueldoDiario ?: 0.0,
                    fecha = fechaHoy
                )
            }
                //.sortedBy { it.nombre } // Ordenar alfabéticamente

            HomeWorkerResult.Success(resultado)

        }
        catch (e: ClientRequestException) {
            HomeWorkerResult.Error("Error al obtener datos: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            HomeWorkerResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            HomeWorkerResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            HomeWorkerResult.Error("Error inesperado: ${e.message}")
        }
    }
}