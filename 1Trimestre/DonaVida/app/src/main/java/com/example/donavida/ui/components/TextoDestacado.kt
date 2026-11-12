package com.example.donavida.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/**
 * Devuelve [textoCompleto] con la primera aparición de [destacado] en [estilo] (por ejemplo,
 * negrita). El destacado va dentro del texto traducible (con %1$s en strings.xml), así que
 * aquí solo se busca; si no aparece, se devuelve el texto sin estilo.
 */
fun textoConDestacado(textoCompleto: String, destacado: String, estilo: SpanStyle): AnnotatedString =
    buildAnnotatedString {
        val inicio = textoCompleto.indexOf(destacado)
        if (destacado.isEmpty() || inicio < 0) {
            append(textoCompleto)
            return@buildAnnotatedString
        }
        append(textoCompleto.substring(0, inicio))
        withStyle(estilo) { append(destacado) }
        append(textoCompleto.substring(inicio + destacado.length))
    }
