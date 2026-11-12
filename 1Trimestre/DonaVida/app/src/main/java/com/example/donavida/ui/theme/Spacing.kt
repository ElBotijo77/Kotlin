package com.example.donavida.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** Escala de espaciado del diseño. Mejor usar estos valores que números sueltos. */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp

    /** Margen lateral de las pantallas. */
    val margin = 20.dp
    val gutter = 16.dp
}

object AppShapes {
    /** Tarjetas: el "rounded-2xl" del diseño. */
    val card = RoundedCornerShape(16.dp)

    /** Tarjetas más compactas (ficha de centro). */
    val cardSmall = RoundedCornerShape(12.dp)

    /** Filas interiores con fondo dentro de una tarjeta. */
    val inner = RoundedCornerShape(8.dp)

    /** Botones y chips. */
    val pill = CircleShape
}
