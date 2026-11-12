package com.example.donavida.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Seguimiento de ejemplo para la pestaña «Mi donación». Es totalmente genérico: solo etapas
 * del proceso, nunca datos del receptor (ni nombre, ni ciudad, ni fechas).
 */
@Serializable
data class MiDonacion(
    /** Siempre true por ahora: la app no guarda datos reales de ninguna persona. */
    val esEjemplo: Boolean = true,
    val tipoDonacion: TipoDonacion,
    val etapas: List<EtapaDonacion>,
)

@Serializable
data class EtapaDonacion(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val estado: EstadoEtapa,
)

@Serializable
enum class EstadoEtapa {
    @SerialName("completada") COMPLETADA,
    @SerialName("en_curso") EN_CURSO,
    @SerialName("pendiente") PENDIENTE,
}
