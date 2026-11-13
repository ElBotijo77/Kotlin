package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/**
 * Caso real de la pantalla "Casos reales". Son casos ya publicados en medios de
 * comunicación: llevan nombre, lugar y fecha porque así aparecen en la noticia, y
 * [fuenteUrl] enlaza a ella cuando la hay. No se añaden datos que no estén publicados.
 */
@Serializable
data class Historia(
    val id: String,
    /** Nombre de la persona o del caso ("Diego"). */
    val titulo: String,
    /** Lugar y fecha ("Berlín, 2007"). */
    val subtitulo: String? = null,
    val perfil: String = "",
    /** null si la noticia no dice por qué método se donó. */
    val tipoDonacion: TipoDonacion? = null,
    val resumen: String = "",
    val texto: String,
    /** Noticia o artículo del que sale el caso; null mientras no se haya añadido. */
    val fuenteUrl: String? = null,
    /** true si es un relato de ejemplo y no un caso real; la UI deberá indicarlo. */
    val esIlustrativa: Boolean = false,
)
