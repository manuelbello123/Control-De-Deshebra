package org.taller.project.History

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import org.taller.project.Models.PrendaDto
import org.taller.project.Models.ProduccionDto
import org.taller.project.Models.ProductionHistory
import org.taller.project.Models.TrabajadorDto
import org.taller.project.Models.UsuarioDto
import kotlin.time.Clock


class HistoryRepository(private val client: HttpClient) {

    private val baseUrl = "http://3.131.91.29"

    private fun semanaIsoActual(): Int {
        val hoy = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val diaSemana = hoy.dayOfWeek.isoDayNumber   // 1=Lun … 7=Dom
        val semanaNum = (hoy.dayOfYear - diaSemana + 10) / 7
        val semana = when {
            semanaNum < 1  -> 52
            semanaNum > 52 -> 1
            else           -> semanaNum
        }
        return hoy.year * 100 + semana
    }

    suspend fun getHistorialSemanaActual(): List<ProductionHistory> = coroutineScope {

        val semana = semanaIsoActual()

        // Las 4 llamadas van en paralelo, todas con Bearer automático
        val produccionDeferred   = async { client.get("$baseUrl/produccion/semana/$semana").body<List<ProduccionDto>>() }
        val trabajadoresDeferred = async { client.get("$baseUrl/trabajadores").body<List<TrabajadorDto>>() }
        val prendasDeferred      = async { client.get("$baseUrl/prendas").body<List<PrendaDto>>() }
        val usuariosDeferred     = async { client.get("$baseUrl/usuarios").body<List<UsuarioDto>>() }

        val producciones  = produccionDeferred.await()
        val trabajadores  = trabajadoresDeferred.await()
        val prendas       = prendasDeferred.await()
        val usuarios      = usuariosDeferred.await()

        val trabajadoresMap = trabajadores.associateBy { it.idTrabajador }
        val prendasMap      = prendas.associateBy { it.idPrenda }
        val usuariosMap     = usuarios.associateBy { it.idUsuario }

        producciones.mapNotNull { prod ->
            val trabajador = trabajadoresMap[prod.idTrabajador] ?: return@mapNotNull null
            val prenda     = prendasMap[prod.idPrenda]         ?: return@mapNotNull null
            val usuario    = usuariosMap[prod.capturadoPor]    ?: return@mapNotNull null

            ProductionHistory(
                idProduccion     = prod.idProduccion,
                nombreTrabajador = trabajador.nombre,
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
    }
}