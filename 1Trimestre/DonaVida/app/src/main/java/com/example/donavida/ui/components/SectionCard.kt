package com.example.donavida.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/**
 * Tarjeta base de la app: fondo, esquinas de 16 dp y relleno interior.
 *
 * @param shape esquinas; por defecto las de Inicio (16 dp). La ficha de centro usa AppShapes.cardSmall.
 * @param elevated sombra ligera; las tarjetas "de fondo" (surfaceContainerLow) no la llevan.
 * @param decoration dibujo opcional detrás del contenido. Queda recortado por la forma de
 *   la tarjeta, así que puede salirse por los bordes sin verse fuera.
 */
@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    shape: Shape = AppShapes.card,
    contentPadding: Dp = Spacing.lg,
    elevated: Boolean = true,
    decoration: (DrawScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = containerColor,
        shadowElevation = if (elevated) 2.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier
                .then(if (decoration != null) Modifier.drawBehind(decoration) else Modifier)
                .padding(contentPadding),
            content = content,
        )
    }
}

@Preview
@Composable
private fun SectionCardPreview() {
    DonaVidaTheme {
        SectionCard(modifier = Modifier.padding(Spacing.md)) {
            Text("Título de la tarjeta", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Contenido de ejemplo dentro de la tarjeta base.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
