package com.example.donavida.data.model

import kotlinx.serialization.Serializable

/**
 * Datos mínimos de la persona que usa la app, para el saludo de Inicio.
 * Por ahora es un perfil de ejemplo: la app no guarda datos reales de nadie.
 */
@Serializable
data class Perfil(
    val esEjemplo: Boolean = true,
    /** Solo el nombre de pila; vacío si la persona no ha querido darlo. */
    val nombre: String = "",
    /** true si la persona ya está inscrita en REDMO. */
    val redmoActivo: Boolean = false,
)
