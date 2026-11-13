package com.example.donavida.ui.screens.sabermas

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/** Pestaña "Saber más": cabecera y cuatro tarjetas que abren las pantallas de contenido. */
@Composable
fun PantallaSaberMas(
    onProblemas: () -> Unit,
    onCasos: () -> Unit,
    onPreguntas: () -> Unit,
    onMitos: () -> Unit,
    onPrivacidad: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = Spacing.margin, end = Spacing.margin, top = Spacing.md, bottom = Spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        item { Cabecera() }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaSeccion(R.drawable.ic_healing, extendidos.primaryFixed, extendidos.onPrimaryFixed,
                    R.string.sm_problemas_titulo, R.string.sm_problemas_texto, onProblemas)
                TarjetaSeccion(R.drawable.ic_favorite, extendidos.secondaryFixed, extendidos.onSecondaryFixed,
                    R.string.sm_casos_titulo, R.string.sm_casos_texto, onCasos)
                TarjetaSeccion(R.drawable.ic_help_outline, extendidos.tertiaryFixed, extendidos.onTertiaryFixed,
                    R.string.sm_preguntas_titulo, R.string.sm_preguntas_texto, onPreguntas)
                TarjetaSeccion(R.drawable.ic_lightbulb, colores.surfaceContainerHigh, colores.primary,
                    R.string.sm_mitos_titulo, R.string.sm_mitos_texto, onMitos)
            }
        }
        item {
            Text(
                text = stringResource(R.string.titulo_privacidad),
                style = MaterialTheme.typography.labelMedium,
                color = colores.primary,
                modifier = Modifier
                    .clip(AppShapes.inner)
                    .clickable(role = Role.Button, onClick = onPrivacidad)
                    .padding(vertical = Spacing.sm, horizontal = Spacing.xs),
            )
        }
    }
}

@Composable
private fun Cabecera() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        StatusBadge(
            text = stringResource(R.string.sm_insignia),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.sm_titulo),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(R.string.sm_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TarjetaSeccion(
    @DrawableRes icono: Int,
    fondoIcono: Color,
    colorIcono: Color,
    titulo: Int,
    texto: Int,
    onClick: () -> Unit,
) {
    SectionCard(
        shape = AppShapes.card,
        contentPadding = Spacing.md,
        modifier = Modifier
            .clip(AppShapes.card)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            IconCircle(icon = icono, size = 44.dp, background = fondoIcono, tint = colorIcono)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(titulo),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(texto),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PantallaSaberMasPreview() {
    DonaVidaTheme {
        PantallaSaberMas(onProblemas = {}, onCasos = {}, onPreguntas = {}, onMitos = {}, onPrivacidad = {})
    }
}
