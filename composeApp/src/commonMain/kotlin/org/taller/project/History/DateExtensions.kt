package org.taller.project.History

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import org.taller.project.Models.ProductionHistory

fun String.toDayName(): String {
    return try {
        val date = LocalDate.parse(this)  // formato ISO: yyyy-MM-dd
        when (date.dayOfWeek) {
            DayOfWeek.MONDAY    -> "Lunes"
            DayOfWeek.TUESDAY   -> "Martes"
            DayOfWeek.WEDNESDAY -> "Miércoles"
            DayOfWeek.THURSDAY  -> "Jueves"
            DayOfWeek.FRIDAY    -> "Viernes"
            DayOfWeek.SATURDAY  -> "Sábado"
            DayOfWeek.SUNDAY    -> "Domingo"
            else -> "Desconocido"
        }
    } catch (e: Exception) {
        "Fecha inválida"
    }
}