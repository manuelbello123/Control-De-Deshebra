package org.taller.project.ProductionWorker

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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import org.taller.project.Models.CreateProduccionRequest
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.ProduccionDetalleDto
import org.taller.project.Models.ProduccionDto
import org.taller.project.Models.ProduccionExpandida
import org.taller.project.Models.SueldoSemanalDto
import org.taller.project.Models.UpdateProduccionRequest
import org.taller.project.Models.UsuarioDto
import kotlin.time.Clock

class ProductionWorkerRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.131.91.29"

    // Calcular semana ISO actual
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

    // ── GET: Sueldos semanales del trabajador ──────────────────────────
    suspend fun getSueldosSemanales(idTrabajador: Int): SueldosSemanalesResult {
        return try {
            val sueldos: List<SueldoSemanalDto> = client
                .get("$baseUrl/sueldo/semanal/$idTrabajador")
                .body()

            // Ordenar por semana ISO descendente (más reciente primero)
            val sueldosOrdenados = sueldos.sortedByDescending { it.semanaIso }

            SueldosSemanalesResult.Success(sueldosOrdenados)

        } catch (e: ClientRequestException) {
            SueldosSemanalesResult.Error("Error al obtener sueldos: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            SueldosSemanalesResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            SueldosSemanalesResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            SueldosSemanalesResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── GET: Producción de la semana actual (expandida) ────────────────
    suspend fun getProduccionSemanaActual(idTrabajador: Int): ProduccionSemanalResult = supervisorScope  {
        return@supervisorScope  try {

            val semanaIso = semanaIsoActual()

            // Llamadas paralelas
            val produccionDeferred = async {
                client.get("$baseUrl/produccion/semana/$semanaIso/trabajador/$idTrabajador")
                    .body<List<ProduccionDetalleDto>>()
            }
            val prendasDeferred = async {
                client.get("$baseUrl/prendas").body<List<PrendaDto>>()
            }
            val usuariosDeferred = async {
                client.get("$baseUrl/usuarios").body<List<UsuarioDto>>()
            }

            val producciones = produccionDeferred.await()
            val prendas      = prendasDeferred.await()
            val usuarios     = usuariosDeferred.await()

            // Mapas para lookup O(1)
            val prendasMap  = prendas.associateBy { it.idPrenda }
            val usuariosMap = usuarios.associateBy { it.idUsuario }

            // Expandir datos
            val produccionExpandida = producciones.mapNotNull { prod ->
                val prenda  = prendasMap[prod.idPrenda]     ?: return@mapNotNull null
                val usuario = usuariosMap[prod.capturadoPor] ?: return@mapNotNull null

                ProduccionExpandida(
                    idProduccion  = prod.idProduccion,
                    idTrabajador  = prod.idTrabajador,
                    cantidad      = prod.cantidad,
                    fecha         = prod.fecha,
                    hora          = prod.hora,
                    semanaIso     = prod.semanaIso,
                    pieza         = prenda.pieza,
                    color         = prenda.color,
                    talla         = prenda.talla,
                    tipo          = prenda.tipo,
                    modelo        = prenda.modelo,
                    precio        = prenda.precio,
                    capturadoPor  = usuario.username
                )
            }

            ProduccionSemanalResult.Success(produccionExpandida)

        } catch (e: ClientRequestException) {
            ProduccionSemanalResult.Error("Error al obtener producción: ${e.response.status.description}")

        } catch (e: ServerResponseException) {
            ProduccionSemanalResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            ProduccionSemanalResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            ProduccionSemanalResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── POST: Crear nueva producción ───────────────────────────────────
    suspend fun createProduccion(
        idTrabajador: Int,
        idPrenda: Int,
        cantidad: Int
    ): CreateProduccionResult {
        return try {

            if (cantidad <= 0) {
                return CreateProduccionResult.Error("La cantidad debe ser mayor a 0")
            }

            client.post("$baseUrl/produccion") {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateProduccionRequest(
                        id_trabajador = idTrabajador,
                        id_prenda = idPrenda,
                        cantidad = cantidad
                    )
                )
            }

            CreateProduccionResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                401 -> CreateProduccionResult.Error("No autorizado. Inicia sesión nuevamente.")
                400 -> CreateProduccionResult.Error("Datos inválidos. Verifica los campos.")
                else -> CreateProduccionResult.Error("Error al crear producción: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            CreateProduccionResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            CreateProduccionResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            CreateProduccionResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── PUT: Actualizar producción ─────────────────────────────────────
    suspend fun updateProduccion(
        idProduccion: Int,
        idPrenda: Int,
        cantidad: Int
    ): UpdateProduccionResult {
        return try {

            if (cantidad <= 0) {
                return UpdateProduccionResult.Error("La cantidad debe ser mayor a 0")
            }

            client.put("$baseUrl/produccion/$idProduccion") {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProduccionRequest(
                        id_prenda = idPrenda,
                        cantidad = cantidad
                    )
                )
            }

            UpdateProduccionResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> UpdateProduccionResult.Error("No tienes permisos para modificar esta producción")
                404 -> UpdateProduccionResult.Error("Producción no encontrada")
                401 -> UpdateProduccionResult.Error("No autorizado. Inicia sesión nuevamente.")
                400 -> UpdateProduccionResult.Error("Datos inválidos")
                else -> UpdateProduccionResult.Error("Error al actualizar: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            UpdateProduccionResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            UpdateProduccionResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            UpdateProduccionResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── DELETE: Eliminar producción ────────────────────────────────────
    suspend fun deleteProduccion(idProduccion: Int): DeleteProduccionResult {
        return try {

            client.delete("$baseUrl/produccion/$idProduccion")

            DeleteProduccionResult.Success

        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                403 -> DeleteProduccionResult.Error("No tienes permisos para eliminar esta producción")
                404 -> DeleteProduccionResult.Error("Producción no encontrada")
                401 -> DeleteProduccionResult.Error("No autorizado. Inicia sesión nuevamente.")
                else -> DeleteProduccionResult.Error("Error al eliminar: ${e.response.status.description}")
            }

        } catch (e: ServerResponseException) {
            DeleteProduccionResult.Error("Error del servidor. Intenta más tarde.")

        } catch (e: IOException) {
            DeleteProduccionResult.Error("Sin conexión a internet. Verifica tu red.")

        } catch (e: Exception) {
            DeleteProduccionResult.Error("Error inesperado: ${e.message}")
        }
    }

    // ── GET: Obtener prendas disponibles para dropdown ─────────────────
    suspend fun getPrendasDisponibles(): List<PrendaDto> {
        return try {
            client.get("$baseUrl/prendas").body()
        } catch (e: Exception) {
            emptyList()
        }
    }
}