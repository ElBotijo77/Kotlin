package com.example.donavida.ui.screens.comofunciona

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.InfoChip
import com.example.donavida.ui.components.PrimaryPillButton
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing
import java.util.Locale

/** Padding de la tarjeta (16) + mitad del círculo (24): ahí va la línea que une los pasos. */
private val CentroCirculo = 40.dp

@Composable
fun PantallaComoFunciona(
    onBuscarCentros: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = Spacing.margin, end = Spacing.margin, top = Spacing.md, bottom = Spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        item { Cabecera() }
        item { ImagenPrincipal() }
        item { CaminoDonacion() }
        item { ImagenManos() }
        item { Requisitos() }
        item {
            PrimaryPillButton(
                text = stringResource(R.string.cf_boton_centros),
                icon = R.drawable.ic_pin_drop,
                onClick = onBuscarCentros,
                height = 54.dp,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// ---------------------------------------------------------------------------------------
// 1-2. Cabecera e imagen
// ---------------------------------------------------------------------------------------

@Composable
private fun Cabecera() {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        InsigniaConIcono(
            icono = R.drawable.ic_info,
            texto = stringResource(R.string.cf_guia),
            fondo = MaterialTheme.colorScheme.surfaceContainerHigh,
            contenido = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.cf_titulo),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(R.string.cf_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Copiado de Mi donación: como StatusBadge pero con icono (StatusBadge no lo admite). */
@Composable
private fun InsigniaConIcono(@DrawableRes icono: Int, texto: String, fondo: Color, contenido: Color) {
    Row(
        modifier = Modifier
            .background(fondo, AppShapes.pill)
            .padding(horizontal = 12.dp, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painterResource(icono), null, tint = contenido, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(texto, style = MaterialTheme.typography.labelSmall, color = contenido)
    }
}

@Composable
private fun ImagenPrincipal() {
    val colores = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
            .clip(AppShapes.cardSmall),
    ) {
        Image(
            painter = painterResource(R.drawable.como_funciona_hero),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, colores.primary.copy(alpha = 0.8f)))),
        )
        Text(
            text = stringResource(R.string.cf_hero),
            style = MaterialTheme.typography.headlineSmall,
            color = colores.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacing.md),
        )
    }
}

// ---------------------------------------------------------------------------------------
// 3. El camino de la donación
// ---------------------------------------------------------------------------------------

private data class PasoCamino(
    val numero: Int,
    @param:DrawableRes val icono: Int,
    val fondoCirculo: Color,
    val tinteCirculo: Color,
    val colorNumero: Color,
    @param:StringRes val etiqueta: Int,
    val colorEtiqueta: Color,
    @param:StringRes val titulo: Int,
    @param:StringRes val texto: Int,
    val conChipsDonacion: Boolean = false,
)

@Composable
private fun pasosDelCamino(): List<PasoCamino> {
    val c = MaterialTheme.colorScheme
    val e = DonaVidaTheme.extendedColors
    return listOf(
        PasoCamino(1, R.drawable.ic_lightbulb, c.surfaceContainerHigh, c.primary, c.primary,
            R.string.cf_paso1_etiqueta, c.onSurfaceVariant, R.string.cf_paso1_titulo, R.string.cf_paso1_texto),
        PasoCamino(2, R.drawable.ic_assignment_turned_in, e.primaryFixed, e.onPrimaryFixed, c.primary,
            R.string.cf_paso2_etiqueta, c.onSurfaceVariant, R.string.cf_paso2_titulo, R.string.cf_paso2_texto),
        PasoCamino(3, R.drawable.ic_hourglass_empty, c.surfaceContainer, c.primary, c.primary,
            R.string.cf_paso3_etiqueta, c.onSurfaceVariant, R.string.cf_paso3_titulo, R.string.cf_paso3_texto),
        PasoCamino(4, R.drawable.ic_ring_volume, e.secondaryFixed, e.onSecondaryFixed, c.secondary,
            R.string.cf_paso4_etiqueta, c.secondary, R.string.cf_paso4_titulo, R.string.cf_paso4_texto),
        PasoCamino(5, R.drawable.ic_diversity_1, e.tertiaryFixed, e.onTertiaryFixed, c.tertiary,
            R.string.cf_paso5_etiqueta, c.onSurfaceVariant, R.string.cf_paso5_titulo, R.string.cf_paso5_texto,
            conChipsDonacion = true),
        PasoCamino(6, R.drawable.ic_spa, e.primaryFixedDim, e.onPrimaryFixed, c.primary,
            R.string.cf_paso6_etiqueta, c.tertiary, R.string.cf_paso6_titulo, R.string.cf_paso6_texto),
    )
}

@Composable
private fun CaminoDonacion() {
    val colores = MaterialTheme.colorScheme
    val colorLinea = colores.outlineVariant.copy(alpha = 0.5f)
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.cf_camino_titulo),
                style = MaterialTheme.typography.headlineSmall,
                color = colores.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() },
            )
            StatusBadge(
                text = stringResource(R.string.cf_camino_pasos),
                containerColor = colores.surfaceContainer,
                contentColor = colores.primary,
            )
        }
        Spacer(Modifier.height(Spacing.md))
        // La línea se dibuja detrás de las tarjetas: solo asoma en los huecos entre ellas.
        Column(
            modifier = Modifier.drawBehind {
                val x = CentroCirculo.toPx()
                drawLine(colorLinea, Offset(x, 0f), Offset(x, size.height), strokeWidth = 2.dp.toPx())
            },
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            pasosDelCamino().forEach { TarjetaPaso(it) }
        }
    }
}

@Composable
private fun TarjetaPaso(paso: PasoCamino) {
    // Se encoge un poco al tocarla (sin acción: es solo respuesta visual).
    var pulsada by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(if (pulsada) 0.99f else 1f, label = "escalaPaso")
    SectionCard(
        modifier = Modifier
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    pulsada = true
                    tryAwaitRelease()
                    pulsada = false
                })
            },
        shape = AppShapes.cardSmall,
        contentPadding = Spacing.md,
    ) {
        Row(verticalAlignment = Alignment.Top) {
            IconCircle(
                icon = paso.icono,
                size = 48.dp,
                background = paso.fondoCirculo,
                tint = paso.tinteCirculo,
                iconSize = 24.dp,
            )
            Spacer(Modifier.width(Spacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.cf_paso_numero, paso.numero).uppercase(Locale.forLanguageTag("es-ES")),
                        style = MaterialTheme.typography.labelSmall,
                        color = paso.colorNumero,
                        modifier = Modifier.weight(1f),
                    )
                    Text(stringResource(paso.etiqueta), style = AppTypography.caption, color = paso.colorEtiqueta)
                }
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = stringResource(paso.titulo),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = stringResource(paso.texto),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (paso.conChipsDonacion) {
                    Spacer(Modifier.height(Spacing.sm))
                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        InfoChip(R.drawable.ic_check_circle, stringResource(R.string.cf_paso5_chip_aferesis))
                        InfoChip(R.drawable.ic_check_circle, stringResource(R.string.cf_paso5_chip_aspiracion))
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------
// 4-5. Imagen secundaria y requisitos
// ---------------------------------------------------------------------------------------

@Composable
private fun ImagenManos() {
    Image(
        painter = painterResource(R.drawable.como_funciona_manos),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .clip(AppShapes.cardSmall),
    )
}

@Composable
private fun Requisitos() {
    val colores = MaterialTheme.colorScheme
    SectionCard(shape = AppShapes.cardSmall, contentPadding = Spacing.lg) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(icon = R.drawable.ic_fact_check, size = 40.dp, background = colores.surfaceContainer, tint = colores.primary)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.cf_requisitos_titulo),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colores.onSurface,
                    modifier = Modifier.semantics { heading() },
                )
                Text(stringResource(R.string.cf_requisitos_subtitulo), style = AppTypography.caption, color = colores.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(Spacing.md))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            FilaRequisito(R.string.cf_requisito_edad)
            FilaRequisito(R.string.cf_requisito_salud)
            FilaRequisito(R.string.cf_requisito_residencia)
        }
        Spacer(Modifier.height(Spacing.md))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colores.surfaceContainerHigh, AppShapes.cardSmall)
                .padding(Spacing.md),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_verified_user), null, tint = colores.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(Spacing.sm))
                Text(stringResource(R.string.cf_aviso_titulo), style = MaterialTheme.typography.labelLarge, color = colores.primary)
            }
            Spacer(Modifier.height(Spacing.sm))
            Text(stringResource(R.string.cf_aviso_texto), style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
        }
    }
}

@Composable
private fun FilaRequisito(@StringRes texto: Int) {
    val extendidos = DonaVidaTheme.extendedColors
    Row(verticalAlignment = Alignment.Top) {
        IconCircle(
            icon = R.drawable.ic_done,
            size = 20.dp,
            background = extendidos.primaryFixed,
            tint = extendidos.onPrimaryFixed,
            iconSize = 15.dp,
        )
        Spacer(Modifier.width(10.dp))
        Text(stringResource(texto), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(heightDp = 2600)
@Composable
private fun PantallaComoFuncionaPreview() {
    DonaVidaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PantallaComoFunciona(onBuscarCentros = {})
        }
    }
}
