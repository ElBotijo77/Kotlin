package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/** Término técnico explicado en lenguaje sencillo (se mostrará en «Cómo funciona»). */
@Serializable
data class TerminoGlosario(
    val id: String,
    val termino: String,
    val definicion: String,
)
