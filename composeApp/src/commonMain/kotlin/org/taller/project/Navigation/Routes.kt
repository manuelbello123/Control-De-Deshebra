package org.taller.project.Navigation

import androidx.compose.material3.IconButton
import org.taller.project.Models.HeaderData

object Routes {
    const val LOGIN = "login"
    const val HOME_WORKER = "home_worker"
    const val ADD_WORKER = "add_worker"
    const val ADD_USER = "add_user"
    const val ADD_GARMENT = "add_garment"
    const val PRODUCTION_WORKER = "production_worker"
    const val HISTORY = "history"
    const val TOTAL_WEEKLY = "total_weekly"

    val headers = mapOf(

        LOGIN to HeaderData(
            title = "CONFEXA",
            subtitle = "Sistema avanzado de manufactura textil"
        ),

        HOME_WORKER to HeaderData(
            title = "Producción del día",
            subtitle = "Toca un trabajador para ver detalles"
        ),

        HISTORY to HeaderData(
            title = "Producción de la semana actual",
            subtitle = "Consulta registros anteriores"
        ),

        TOTAL_WEEKLY to HeaderData(
            title = "Totales semanales",
            subtitle = "Resumen de producción y pagos"
        ),

        ADD_WORKER to HeaderData(
            title = "Trabajadores",
            subtitle = "Gestiona tu equipo"
        ),

        ADD_USER to HeaderData(
            title = "Usuarios",
            subtitle = "Gestiona accesos al sistema"
        ),

        ADD_GARMENT to HeaderData(
            title = "Prendas",
            subtitle = "Gestiona tus prendas"
        ),
        PRODUCTION_WORKER to HeaderData(
            title = "Produccion",
            subtitle = "Asigna produccion"
        )
    )
}

