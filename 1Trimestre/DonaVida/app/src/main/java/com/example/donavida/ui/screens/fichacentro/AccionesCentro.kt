package com.example.donavida.ui.screens.fichacentro

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.example.donavida.data.model.Centro

// Acciones que abren otras apps. Se intenta abrir directamente y se captura el error si no
// hay ninguna app que lo resuelva: así no hace falta declarar <queries> en el manifiesto.

/** Abre el marcador con el número escrito. ACTION_DIAL no llama: no requiere permiso. */
fun abrirMarcador(context: Context, telefono: String) {
    val numero = telefono.filter { it.isDigit() || it == '+' }
    abrirSinFallar(context, Intent(Intent.ACTION_DIAL, "tel:$numero".toUri()))
}

/** Abre la app de mapas en el centro; si no hay ninguna, Google Maps en el navegador. */
fun abrirRuta(context: Context, centro: Centro) {
    val coordenadas = "${centro.latitud},${centro.longitud}"
    val geo = "geo:$coordenadas?q=$coordenadas(${Uri.encode(centro.nombre)})".toUri()
    if (!abrirSinFallar(context, Intent(Intent.ACTION_VIEW, geo))) {
        val web = "https://www.google.com/maps/search/?api=1&query=$coordenadas".toUri()
        abrirSinFallar(context, Intent(Intent.ACTION_VIEW, web))
    }
}

fun abrirWeb(context: Context, url: String) {
    abrirSinFallar(context, Intent(Intent.ACTION_VIEW, url.toUri()))
}

/** true si alguna app ha abierto el intent. */
private fun abrirSinFallar(context: Context, intent: Intent): Boolean = try {
    context.startActivity(intent)
    true
} catch (_: ActivityNotFoundException) {
    false
}
