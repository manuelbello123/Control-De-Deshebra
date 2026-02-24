package org.taller.project.TotalWeekly

fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("-")
        val day = parts[2].toInt()
        val month = when (parts[1].toInt()) {
            1 -> "Ene"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Abr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Ago"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dic"
            else -> ""
        }
        "$day $month"
    } catch (e: Exception) {
        dateString
    }
}