package com.example.donavida.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Las dos partes de la pantalla "Qué problemas pueden solucionar". */
@Serializable
enum class BloqueProblema {
    /** Tratamientos que ya se usan con trasplante de células madre hematopoyéticas. */
    @SerialName("actual") ACTUAL,

    /** Líneas de investigación con otros tipos de células madre; no son tratamientos habituales. */
    @SerialName("investigacion") INVESTIGACION,
}

/**
 * Un punto de la pantalla "Qué problemas pueden solucionar".
 * [icono] es el nombre del icono de Material Symbols ("bloodtype"); la pantalla lo traduce a
 * su drawable y usa uno genérico si no lo conoce.
 */
@Serializable
data class Problema(
    val id: String,
    val bloque: BloqueProblema,
    val titulo: String,
    val texto: String,
    val icono: String,
)
