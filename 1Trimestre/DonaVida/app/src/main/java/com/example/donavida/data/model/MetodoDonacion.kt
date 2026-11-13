package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/**
 * Un método de donación de la pantalla "Cómo funciona". El icono y los colores los decide
 * la pantalla según [tipo].
 */
@Serializable
data class MetodoDonacion(
    val tipo: TipoDonacion,
    val titulo: String,
    /** "Procedimiento", "Recuperación", "Preparación"… en el orden en que se muestran. */
    val apartados: List<ApartadoMetodo>,
)

@Serializable
data class ApartadoMetodo(
    val etiqueta: String,
    val texto: String,
)
