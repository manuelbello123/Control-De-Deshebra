package org.taller.project.Models

data class ProductionHistory(
    val idProduccion: Int,
    val nombreTrabajador: String,
    val pieza: String,
    val color: String,
    val talla: String,
    val tipo: String,
    val modelo: String,
    val cantidad: Int,
    val fecha: String,
    val hora: String,
    val capturadoPor: String,
    val semanaIso: Int
)
data class ProductionByDay(
    val dayName: String,       // "Martes"
    val fecha: String,         // "2026-01-29" (usamos esta para ordenar)
    val productions: List<ProductionHistory>
)