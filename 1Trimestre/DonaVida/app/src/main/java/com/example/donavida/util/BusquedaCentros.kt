package com.example.donavida.util

import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.ServicioCentro
import java.text.Normalizer
import java.util.Locale

/**
 * Filtra los centros por lo que escribe la persona y por el servicio elegido. Función pura.
 *
 * - Solo cifras (2 o más): compara las dos primeras con las del código postal, que indican
 *   la provincia. "28006" encuentra todos los centros de Madrid.
 * - Texto: busca en ciudad, provincia y nombre, sin distinguir mayúsculas ni tildes
 *   ("malaga" encuentra "Málaga").
 * - Consulta vacía: no filtra por texto.
 * - [servicio] distinto de null: deja solo los centros que lo ofrecen.
 */
fun filtrarCentros(centros: List<Centro>, consulta: String, servicio: ServicioCentro?): List<Centro> {
    val texto = consulta.trim()
    val esCodigoPostal = texto.length >= 2 && texto.all { it.isDigit() }
    val buscado = normalizar(texto)

    return centros.filter { centro ->
        val coincideTexto = when {
            texto.isEmpty() -> true
            esCodigoPostal -> centro.codigoPostal.take(2) == texto.take(2)
            else -> listOf(centro.ciudad, centro.provincia, centro.nombre)
                .any { campo -> normalizar(campo).contains(buscado) }
        }
        coincideTexto && (servicio == null || servicio in centro.servicios)
    }
}

/** "Málaga" -> "malaga": quita tildes y diéresis y pasa a minúsculas. */
private fun normalizar(texto: String): String =
    Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("""\p{Mn}+"""), "")
        .lowercase(Locale.ROOT)
