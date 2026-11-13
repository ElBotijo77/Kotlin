package com.example.donavida.ui.screens.sabermas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.donavida.ui.components.SecondaryTopBar
import com.example.donavida.ui.theme.Spacing

/**
 * Armazón de las pantallas de contenido de "Saber más": barra con flecha de volver y una
 * lista con el margen lateral de la app.
 */
@Composable
fun PantallaSecundaria(
    titulo: String,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    separacion: Dp = Spacing.lg,
    contenido: LazyListScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SecondaryTopBar(title = titulo, onBack = onVolver)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = Spacing.margin, end = Spacing.margin, top = Spacing.md, bottom = Spacing.xxl),
            verticalArrangement = Arrangement.spacedBy(separacion),
            content = contenido,
        )
    }
}
