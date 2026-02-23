package org.taller.project.Navigation

object Routes {
    const val LOGIN = "login"
    const val HOME_WORKER = "home_worker"
    const val ADD_WORKER = "add_worker"
    const val ADD_USER = "add_user"
    const val ADD_GARMENT = "add_garment"
    const val PRODUCTION_WORKER = "production_worker"
    const val HISTORY = "history"
    const val TOTAL_WEEKLY = "total_weekly"

    val titles = mapOf(
        LOGIN to "Login",
        HOME_WORKER to "Trabajadores",
        ADD_WORKER to "Agregar Trabajador",
        ADD_USER to "Agregar Usuario",
        ADD_GARMENT to "Agregar Prenda",
        PRODUCTION_WORKER to "Producción Trabajador",
        HISTORY to "Historial",
        TOTAL_WEEKLY to "Total Semanal"
    )
}