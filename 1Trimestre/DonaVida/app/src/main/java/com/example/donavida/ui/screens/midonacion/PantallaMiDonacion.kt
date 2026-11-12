package com.example.donavida.ui.screens.midonacion

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.PrimaryPillButton
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/** Título de paso y valores de la cuadrícula: 16 sp SemiBold. */
private val TextoDestacado16
    @Composable get() = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold)

/** PROTOTIPO: los datos del donante son de ejemplo y el inicio de sesión es simulado. */
@Composable
fun PantallaMiDonacion(
    modifier: Modifier = Modifier,
    viewModel: MiDonacionViewModel = viewModel(),
) {
    val sesionIniciada by viewModel.sesionIniciada.collectAsStateWithLifecycle()
    PantallaMiDonacionContenido(
        sesionIniciada = sesionIniciada,
        onCambiarSesion = viewModel::cambiarSesion,
        modifier = modifier,
    )
}

@Composable
fun PantallaMiDonacionContenido(
    sesionIniciada: Boolean,
    onCambiarSesion: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = Spacing.margin, end = Spacing.margin, top = Spacing.md, bottom = Spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        // DEMO: selector para enseñar los dos estados. Quitar cuando haya acceso real.
        item { SelectorDemo(sesionIniciada = sesionIniciada, onCambiar = onCambiarSesion) }

        if (sesionIniciada) {
            item { TarjetaDonante() }
            item { LineaTiempo() }
            item { InformeImpacto() }
        } else {
            item { TarjetaAcceso(onAcceder = { onCambiarSesion(true) }) }
            item { FilaGarantias() }
        }
        item { NotaRegistro() }
    }
}

// ---------------------------------------------------------------------------------------
// Selector de demo
// ---------------------------------------------------------------------------------------

// DEMO: quitar junto con el selector.
@Composable
private fun SelectorDemo(sesionIniciada: Boolean, onCambiar: (Boolean) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh, AppShapes.pill)
                .padding(Spacing.xs)
                .selectableGroup(),
        ) {
            OpcionDemo(stringResource(R.string.mi_demo_sesion_activa), activa = sesionIniciada, conPunto = true) { onCambiar(true) }
            OpcionDemo(stringResource(R.string.mi_demo_sin_sesion), activa = !sesionIniciada, conPunto = false) { onCambiar(false) }
        }
    }
}

@Composable
private fun OpcionDemo(texto: String, activa: Boolean, conPunto: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .clip(AppShapes.pill)
            .background(if (activa) colores.surfaceContainerLowest else Color.Transparent)
            .selectable(selected = activa, role = Role.Tab, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (conPunto) {
            PuntoParpadeante(colores.primary)
            Spacer(Modifier.width(6.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelMedium, color = if (activa) colores.primary else colores.onSurfaceVariant)
    }
}

/** Copiado de RedmoStatusChip (Inicio): punto de 8 dp con pulso de opacidad. */
@Composable
private fun PuntoParpadeante(color: Color) {
    val opacidad by rememberInfiniteTransition(label = "puntoDemo").animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "opacidad",
    )
    Box(
        Modifier
            .size(8.dp)
            .graphicsLayer { alpha = opacidad }
            .background(color, CircleShape),
    )
}

// ---------------------------------------------------------------------------------------
// Estado A: sin sesión
// ---------------------------------------------------------------------------------------

@Composable
private fun TarjetaAcceso(onAcceder: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    SectionCard {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            IconCircle(
                icon = R.drawable.ic_badge_filled,
                size = 80.dp,
                background = colores.surfaceContainerHigh,
                tint = colores.primary,
                iconSize = 40.dp,
            )
            Spacer(Modifier.height(Spacing.md))
            StatusBadge(
                text = stringResource(R.string.mi_acceso_registro),
                containerColor = extendidos.tertiaryFixed,
                contentColor = extendidos.onTertiaryFixed,
            )
            Spacer(Modifier.height(Spacing.md))
            Text(
                text = stringResource(R.string.mi_acceso_titulo),
                style = MaterialTheme.typography.headlineSmall,
                color = colores.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { heading() },
            )
            Spacer(Modifier.height(Spacing.sm))
            Text(
                text = stringResource(R.string.mi_acceso_texto),
                style = MaterialTheme.typography.bodyMedium,
                color = colores.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(Spacing.lg))
            // Acceso simulado: pasa directamente a la sesión iniciada.
            PrimaryPillButton(
                text = stringResource(R.string.mi_acceso_boton),
                icon = R.drawable.ic_verified_user,
                onClick = onAcceder,
                containerColor = colores.secondary,
                contentColor = colores.onSecondary,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.md))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colores.surfaceContainerLow, AppShapes.cardSmall)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painterResource(R.drawable.ic_lock), null, tint = colores.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(Spacing.sm))
                Text(stringResource(R.string.mi_acceso_seguridad), style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun FilaGarantias() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        TarjetaGarantia(R.drawable.ic_visibility_off, R.string.mi_anonimato_titulo, R.string.mi_anonimato_texto, Modifier.weight(1f).fillMaxHeight())
        TarjetaGarantia(R.drawable.ic_history_edu, R.string.mi_historial_titulo, R.string.mi_historial_texto, Modifier.weight(1f).fillMaxHeight())
    }
}

@Composable
private fun TarjetaGarantia(@DrawableRes icono: Int, titulo: Int, texto: Int, modifier: Modifier) {
    SectionCard(modifier = modifier, contentPadding = Spacing.md) {
        IconCircle(icon = icono, size = 40.dp, background = MaterialTheme.colorScheme.surfaceContainer, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))
        Text(stringResource(titulo), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(Spacing.xs))
        Text(stringResource(texto), style = AppTypography.caption, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ---------------------------------------------------------------------------------------
// Estado B: sesión iniciada
// ---------------------------------------------------------------------------------------

@Composable
private fun TarjetaDonante() {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppShapes.card)
            .background(Brush.linearGradient(listOf(colores.primary, colores.primaryContainer, colores.primary)))
            // Copiado de DonorCtaCard (Inicio): círculo que asoma por la esquina superior derecha.
            .drawBehind {
                val radio = 80.dp.toPx()
                val desplazamiento = 48.dp.toPx()
                drawCircle(
                    color = Color.White.copy(alpha = 0.10f),
                    radius = radio,
                    center = Offset(size.width - radio + desplazamiento, radio - desplazamiento),
                )
            }
            .padding(Spacing.lg),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.15f), AppShapes.pill)
                    .padding(horizontal = 12.dp, vertical = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(8.dp).background(colores.secondaryContainer, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.mi_donante_efectivo), style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.mi_id_donante, DonanteEjemplo.ID),
                style = AppTypography.caption,
                color = colores.onPrimaryContainer,
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Text(
            text = stringResource(R.string.mi_principal_titulo),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(Spacing.xs))
        Text(stringResource(R.string.mi_principal_subtitulo), style = MaterialTheme.typography.bodyMedium, color = colores.onPrimaryContainer)
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.10f), AppShapes.cardSmall)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_volunteer_activism_filled), null, tint = extendidos.secondaryFixed, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.sm))
            Text(
                text = stringResource(R.string.mi_estado_receptor, DonanteEjemplo.ESTADO_RECEPTOR),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            Text(DonanteEjemplo.PERIODO_SEGUIMIENTO, style = AppTypography.caption, color = extendidos.secondaryFixed)
        }
    }
}

@Composable
private fun LineaTiempo() {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    val pasos = DonanteEjemplo.pasos
    SectionCard {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.mi_linea_titulo),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colores.onSurface,
                    modifier = Modifier.semantics { heading() },
                )
                Text(stringResource(R.string.mi_linea_subtitulo), style = AppTypography.caption, color = colores.onSurfaceVariant)
            }
            Spacer(Modifier.width(Spacing.sm))
            InsigniaConIcono(
                icono = R.drawable.ic_check_circle,
                texto = stringResource(R.string.mi_linea_etapas, pasos.size, pasos.size),
                fondo = extendidos.tertiaryFixed,
                contenido = extendidos.onTertiaryFixed,
            )
        }
        Spacer(Modifier.height(20.dp))
        pasos.forEachIndexed { indice, paso ->
            PasoLineaTiempo(paso = paso, esUltimo = indice == pasos.lastIndex)
        }
    }
}

/** Como StatusBadge pero con icono en vez de punto (StatusBadge no lo admite). */
@Composable
private fun InsigniaConIcono(@DrawableRes icono: Int, texto: String, fondo: Color, contenido: Color) {
    Row(
        modifier = Modifier
            .background(fondo, AppShapes.pill)
            .padding(horizontal = 10.dp, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painterResource(icono), null, tint = contenido, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(Spacing.xs))
        Text(texto, style = MaterialTheme.typography.labelSmall, color = contenido)
    }
}

/** Mismo método que PasoDelProceso (Inicio): la línea se dibuja por tramos bajo cada nodo. */
@Composable
private fun PasoLineaTiempo(paso: PasoDonacion, esUltimo: Boolean) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    val destacado = paso.detalleDestacado != null
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        // Columna de 48 dp para que el nodo destacado (40 + anillo de 4) no descuadre la línea.
        Column(modifier = Modifier.width(48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(if (destacado) colores.secondary.copy(alpha = 0.15f) else Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                IconCircle(
                    icon = paso.icono,
                    size = 40.dp,
                    background = if (destacado) colores.secondary else colores.primary,
                    tint = Color.White,
                    iconSize = 20.dp,
                )
            }
            if (!esUltimo) {
                Box(
                    Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(colores.primary.copy(alpha = 0.2f)),
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (esUltimo) 0.dp else 20.dp)
                .then(
                    if (destacado) {
                        Modifier
                            .background(colores.surfaceContainerLow, AppShapes.cardSmall)
                            .padding(12.dp)
                    } else {
                        Modifier.padding(top = 12.dp)
                    },
                ),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(paso.titulo, style = TextoDestacado16, color = colores.onSurface, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(Spacing.sm))
                Text(paso.fecha, style = MaterialTheme.typography.labelSmall, color = if (destacado) colores.secondary else colores.primary)
            }
            Spacer(Modifier.height(Spacing.xs))
            Text(paso.descripcion, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            if (paso.detalleDestacado != null) {
                StatusBadge(
                    text = stringResource(R.string.mi_paso_completado_detalle, paso.detalleDestacado),
                    containerColor = extendidos.secondaryFixed,
                    contentColor = extendidos.onSecondaryFixed,
                )
            } else {
                Text(stringResource(R.string.mi_paso_completado), style = AppTypography.caption, color = colores.tertiary)
            }
        }
    }
}

@Composable
private fun InformeImpacto() {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(icon = R.drawable.ic_person_heart, size = 40.dp, background = extendidos.secondaryFixed, tint = colores.secondary)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.mi_impacto_etiqueta).uppercase(),
                    style = AppTypography.caption.copy(letterSpacing = 0.08.em),
                    color = colores.secondary,
                )
                Text(
                    text = stringResource(R.string.mi_impacto_titulo),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colores.onSurface,
                    modifier = Modifier.semantics { heading() },
                )
            }
        }
        Spacer(Modifier.height(Spacing.md))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                CasillaImpacto(R.drawable.ic_person, R.string.mi_impacto_edad, DonanteEjemplo.GRUPO_EDAD, false, Modifier.weight(1f).fillMaxHeight())
                CasillaImpacto(R.drawable.ic_location_on, R.string.mi_impacto_ubicacion, DonanteEjemplo.UBICACION_MEDICA, false, Modifier.weight(1f).fillMaxHeight())
            }
            Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                CasillaImpacto(R.drawable.ic_healing, R.string.mi_impacto_diagnostico, DonanteEjemplo.DIAGNOSTICO, false, Modifier.weight(1f).fillMaxHeight())
                CasillaImpacto(R.drawable.ic_trending_up, R.string.mi_impacto_salud, DonanteEjemplo.ESTADO_SALUD, true, Modifier.weight(1f).fillMaxHeight())
            }
        }
        Spacer(Modifier.height(Spacing.md))
        DesplegablePorQue()
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(extendidos.secondaryFixed.copy(alpha = 0.3f), AppShapes.cardSmall)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_sentiment_satisfied), null, tint = colores.secondary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.sm))
            Text(stringResource(R.string.mi_agradecimiento), style = AppTypography.caption, color = extendidos.onSecondaryFixed)
        }
    }
}

@Composable
private fun CasillaImpacto(@DrawableRes icono: Int, etiqueta: Int, valor: String, destacada: Boolean, modifier: Modifier) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    val colorEtiqueta = if (destacada) colores.tertiary else colores.primary
    Column(
        modifier = modifier
            .background(if (destacada) extendidos.tertiaryFixed.copy(alpha = 0.3f) else colores.surfaceContainerLow, AppShapes.cardSmall)
            .padding(Spacing.md),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(icono), null, tint = colorEtiqueta, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(stringResource(etiqueta), style = AppTypography.caption, color = colorEtiqueta)
        }
        Spacer(Modifier.height(6.dp))
        Text(valor, style = TextoDestacado16, color = if (destacada) extendidos.onTertiaryFixed else colores.onSurface)
    }
}

@Composable
private fun DesplegablePorQue() {
    val colores = MaterialTheme.colorScheme
    var abierto by rememberSaveable { mutableStateOf(false) }
    val giro by animateFloatAsState(if (abierto) 180f else 0f, label = "giroFlecha")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppShapes.cardSmall)
            .background(colores.surfaceContainerHigh.copy(alpha = 0.6f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button) { abierto = !abierto }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_help_outline), null, tint = colores.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(Spacing.sm))
            Text(stringResource(R.string.mi_por_que_titulo), style = MaterialTheme.typography.labelMedium, color = colores.primary, modifier = Modifier.weight(1f))
            Icon(painterResource(R.drawable.ic_expand_more), null, tint = colores.primary, modifier = Modifier.rotate(giro))
        }
        AnimatedVisibility(visible = abierto) {
            Text(
                text = stringResource(R.string.mi_por_que_texto),
                style = MaterialTheme.typography.bodySmall,
                color = colores.onSurfaceVariant,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            )
        }
    }
}

// ---------------------------------------------------------------------------------------
// Nota final (los dos estados)
// ---------------------------------------------------------------------------------------

@Composable
private fun NotaRegistro() {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.surfaceContainerHigh.copy(alpha = 0.7f), AppShapes.card)
            .padding(Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircle(icon = R.drawable.ic_shield, size = 32.dp, background = colores.primary.copy(alpha = 0.10f), tint = colores.primary, iconSize = 18.dp)
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.mi_nota_registro),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = colores.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------------------

@Preview(heightDp = 1100)
@Composable
private fun MiDonacionSinSesionPreview() {
    DonaVidaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PantallaMiDonacionContenido(sesionIniciada = false, onCambiarSesion = {})
        }
    }
}

@Preview(heightDp = 2400)
@Composable
private fun MiDonacionConSesionPreview() {
    DonaVidaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PantallaMiDonacionContenido(sesionIniciada = true, onCambiarSesion = {})
        }
    }
}
