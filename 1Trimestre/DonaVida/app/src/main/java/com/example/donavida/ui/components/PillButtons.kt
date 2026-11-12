package com.example.donavida.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/**
 * Botón principal en forma de píldora (por defecto, fondo secondaryContainer). Al pulsarlo se encoge un
 * poco (escala 0,98) para que se note el toque.
 */
@Composable
fun PrimaryPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    iconAtEnd: Boolean = false,
    height: Dp = 56.dp,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    val interaccion = remember { MutableInteractionSource() }
    Button(
        onClick = onClick,
        modifier = modifier
            // heightIn y no height: si la persona agranda la letra, el botón crece.
            .heightIn(min = height)
            .escalaAlPulsar(interaccion),
        shape = AppShapes.pill,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.sm),
        interactionSource = interaccion,
    ) {
        ContenidoBoton(text, icon, iconAtEnd)
    }
}

/** Botón secundario "tonal": píldora surfaceContainer con texto en primary. */
@Composable
fun TonalPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    iconAtEnd: Boolean = false,
    height: Dp = 48.dp,
) {
    val interaccion = remember { MutableInteractionSource() }
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = height)
            .escalaAlPulsar(interaccion),
        shape = AppShapes.pill,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.sm),
        interactionSource = interaccion,
    ) {
        ContenidoBoton(text, icon, iconAtEnd)
    }
}

@Composable
private fun RowScope.ContenidoBoton(text: String, @DrawableRes icon: Int?, iconAtEnd: Boolean) {
    if (icon != null && !iconAtEnd) {
        IconoBoton(icon)
        Spacer(Modifier.width(Spacing.sm))
    }
    // weight(fill = false): si el texto ocupa dos líneas, el icono conserva su sitio.
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f, fill = false),
    )
    if (icon != null && iconAtEnd) {
        Spacer(Modifier.width(Spacing.sm))
        IconoBoton(icon)
    }
}

@Composable
private fun IconoBoton(@DrawableRes icon: Int) {
    // Decorativo: el texto del botón ya dice qué hace.
    Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(20.dp))
}

@Composable
private fun Modifier.escalaAlPulsar(interaccion: MutableInteractionSource): Modifier {
    val pulsado by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(if (pulsado) 0.98f else 1f, label = "escalaBoton")
    return graphicsLayer {
        scaleX = escala
        scaleY = escala
    }
}

@Preview
@Composable
private fun PillButtonsPreview() {
    DonaVidaTheme {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            PrimaryPillButton(
                text = "Buscar un centro cerca",
                icon = R.drawable.ic_pin_drop,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
            TonalPillButton(
                text = "Ver cómo funciona el proceso completo",
                icon = R.drawable.ic_arrow_forward,
                iconAtEnd = true,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
