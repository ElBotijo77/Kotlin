package com.example.donavida.util

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val RADIO_TIERRA_KM = 6371.0

/**
 * Distancia en línea recta (km) entre dos puntos, con la fórmula del semiverseno (haversine).
 * Servirá para ordenar los centros por cercanía. El cálculo se hace en el propio móvil:
 * la ubicación nunca sale del dispositivo.
 */
fun distanciaKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
        cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
    return 2 * RADIO_TIERRA_KM * asin(sqrt(a))
}

private val Espanol: java.util.Locale = java.util.Locale.forLanguageTag("es-ES")

/**
 * Distancia legible en español: "A 2,4 km" (un decimal, coma decimal) o, por debajo de 1 km,
 * "A 850 m" (redondeado a decenas de metro).
 */
fun formatearDistancia(km: Double): String {
    val metros = Math.round(km * 100) * 10 // decenas de metro
    return if (metros < 1000) {
        "A $metros m"
    } else {
        "A ${String.format(Espanol, "%.1f", km)} km"
    }
}
