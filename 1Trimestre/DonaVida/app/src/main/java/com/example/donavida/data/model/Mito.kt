package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/**
 * Entrada de las pantallas «Mitos» y «Preguntas frecuentes». Las dos cosas tienen la misma
 * forma (un enunciado y su respuesta), así que comparten modelo:
 * - [esMito] = true: [enunciado] es la creencia falsa y [respuesta], la realidad.
 * - [esMito] = false: [enunciado] es una pregunta frecuente.
 */
@Serializable
data class Mito(
    val id: String,
    val esMito: Boolean,
    val enunciado: String,
    val respuesta: String,
    /** Lista opcional que va debajo de la respuesta (por ejemplo, un punto por método). */
    val puntos: List<PuntoDestacado> = emptyList(),
    val urlFuente: String? = null,
)

/** Un punto de lista con una parte en negrita: [destacado] tiene que aparecer dentro de [texto]. */
@Serializable
data class PuntoDestacado(
    val texto: String,
    val destacado: String,
)
