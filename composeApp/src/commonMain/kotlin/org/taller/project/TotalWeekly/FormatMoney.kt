package org.taller.project.TotalWeekly

fun formatMoney(amount: Double): String {
    val rounded = (amount * 100).toLong() / 100.0

    val intPart = rounded.toLong()
    val decimalPart = ((rounded - intPart) * 100).toInt()

    val decimalStr = if (decimalPart < 10) "0$decimalPart" else "$decimalPart"

    return "$$intPart.$decimalStr"
}