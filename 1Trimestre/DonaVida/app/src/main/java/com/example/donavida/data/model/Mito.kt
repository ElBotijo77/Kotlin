package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/**
 * Entrada de la pantalla «Mitos y preguntas frecuentes». Las dos cosas tienen la misma forma
 * (un enunciado y su respuesta), así que comparten modelo:
 * - [esMito] = true: [enunciado] es la creencia falsa y [respuesta], la realidad.
 * - [esMito] = false: [enunciado] es una pregunta frecuente.
 */
@Serializable
data class Mito(
    val id: String,
    val esMito: Boolean,
    val enunciado: String,
    val respuesta: String,
    val urlFuente: String,
)
