package org.taller.project.TotalWeekly

fun formatMoney(amount: Double): String {
    // Redondear a 2 decimales
    val rounded = (amount * 100).toLong() / 100.0

    // Separar parte entera y decimal
    val intPart = rounded.toLong()
    val decimalPart = ((rounded - intPart) * 100).toInt()

    // Formatear con ceros a la izquierda si es necesario
    val decimalStr = if (decimalPart < 10) "0$decimalPart" else "$decimalPart"

    return "$$intPart.$decimalStr"
}