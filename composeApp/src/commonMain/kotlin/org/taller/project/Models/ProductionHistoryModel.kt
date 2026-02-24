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
    val dayName: String,
    val fecha: String,
    val productions: List<ProductionHistory>
)