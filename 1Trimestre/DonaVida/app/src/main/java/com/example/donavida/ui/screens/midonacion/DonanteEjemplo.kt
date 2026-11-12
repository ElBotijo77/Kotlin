package com.example.donavida.ui.screens.midonacion

import androidx.annotation.DrawableRes
import com.example.donavida.R

/** Un hito de la línea de tiempo de la donación. */
data class PasoDonacion(
    @param:DrawableRes val icono: Int,
    val titulo: String,
    val fecha: String,
    val descripcion: String,
    /** Si no es null, el paso va destacado y este texto acompaña a "Completado". */
    val detalleDestacado: String? = null,
)

/**
 * PROTOTIPO: donante ficticio para la demo de "Mi donación". No existe ninguna integración
 * real con el REDMO; en una versión real estos datos vendrían del registro oficial y la app
 * no los guardaría.
 */
object DonanteEjemplo {
    const val ID = "884-ES-BMA"
    const val ESTADO_RECEPTOR = "Estable"
    const val PERIODO_SEGUIMIENTO = "Semestre 1"

    // TODO: sustituir las descripciones por las del diseño.
    val pasos = listOf(
        PasoDonacion(
            icono = R.drawable.ic_how_to_reg,
            titulo = "Inscrito",
            fecha = "Mayo 2021",
            descripcion = "Te inscribiste en el REDMO y se analizó tu tipaje HLA.",
        ),
        PasoDonacion(
            icono = R.drawable.ic_diversity_1,
            titulo = "Compatible",
            fecha = "Octubre 2024",
            descripcion = "Se confirmó tu compatibilidad con un paciente y aceptaste donar.",
        ),
        PasoDonacion(
            icono = R.drawable.ic_favorite,
            titulo = "Donación realizada",
            fecha = "Enero 2025",
            descripcion = "Donaste progenitores de la sangre tras el reconocimiento médico.",
            detalleDestacado = "Procedimiento por aféresis",
        ),
        PasoDonacion(
            icono = R.drawable.ic_local_shipping,
            titulo = "Células entregadas al centro de trasplante",
            fecha = "Febrero 2025",
            descripcion = "Tus células llegaron al hospital del receptor en perfectas condiciones.",
        ),
        PasoDonacion(
            icono = R.drawable.ic_workspace_premium_filled,
            titulo = "Trasplante realizado",
            fecha = "Febrero 2025",
            descripcion = "El equipo médico realizó el trasplante con éxito.",
        ),
    )

    const val GRUPO_EDAD = "Adulto de 30 a 50 años"
    const val UBICACION_MEDICA = "Tratado en España"
    const val DIAGNOSTICO = "Enfermedad de la sangre"
    const val ESTADO_SALUD = "Evolución favorable a los 6 meses"
}
