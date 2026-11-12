package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/**
 * Testimonio para concienciar. Por privacidad no lleva nombres, ciudades ni fechas:
 * [perfil] es una descripción genérica ("Donante de sangre periférica").
 */
@Serializable
data class Historia(
    val id: String,
    val titulo: String,
    val perfil: String,
    val tipoDonacion: TipoDonacion,
    val resumen: String,
    val texto: String,
    /** true si es un relato de ejemplo y no un testimonio real; la UI deberá indicarlo. */
    val esIlustrativa: Boolean = true,
)
