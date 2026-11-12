package com.example.donavida.util

/**
 * Agrupa un teléfono español de 9 cifras para leerlo mejor:
 * fijos "91 301 72 00", móviles "612 34 56 78" y gratuitos/especiales "900 100 200".
 * Si no son 9 cifras, lo devuelve tal cual.
 */
fun formatearTelefono(telefono: String): String {
    val cifras = telefono.filter { it.isDigit() }
    if (cifras.length != 9) return telefono.trim()
    val grupos = when {
        cifras[0] == '6' || cifras[0] == '7' -> listOf(3, 2, 2, 2)
        cifras.startsWith("80") || cifras.startsWith("90") -> listOf(3, 3, 3)
        else -> listOf(2, 3, 2, 2)
    }
    var inicio = 0
    return grupos.joinToString(" ") { largo ->
        cifras.substring(inicio, inicio + largo).also { inicio += largo }
    }
}
