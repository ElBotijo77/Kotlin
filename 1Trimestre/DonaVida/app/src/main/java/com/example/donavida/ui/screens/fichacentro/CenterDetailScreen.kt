package com.example.donavida.ui.screens.fichacentro

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.FranjaHorario
import com.example.donavida.data.model.TipoCentro
import com.example.donavida.ui.components.CardHeader
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.SecondaryTopBar
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.components.TonalPillButton
import com.example.donavida.ui.components.textoConDestacado
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing
import com.example.donavida.util.EstadoApertura
import com.example.donavida.util.formatearTelefono
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val Espanol: Locale = Locale.forLanguageTag("es-ES")
private val FormatoHora: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val FormatoFecha: DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")

/** Espacio entre las tarjetas de información. */
private val SeparacionTarjetas = 14.dp

/** Versión "con estado": obtiene el ViewModel y convierte las acciones en intents. */
@Composable
fun CenterDetailRoute(
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: CenterDetailViewModel = viewModel(factory = CenterDetailViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    CenterDetailScreen(
        uiState = uiState,
        onBack = onBack,
        onProfileClick = onProfileClick,
        onCall = { telefono -> abrirMarcador(context, telefono) },
        onDirections = { centro -> abrirRuta(context, centro) },
        onOpenWeb = { url -> abrirWeb(context, url) },
    )
}

/** Ficha de un centro. Solo pinta [uiState] y avisa con callbacks. */
@Composable
fun CenterDetailScreen(
    uiState: CenterDetailUiState,
    onBack: () -> Unit,
    onProfileClick: () -> Unit,
    onCall: (String) -> Unit,
    onDirections: (Centro) -> Unit,
    onOpenWeb: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SecondaryTopBar(
                title = stringResource(R.string.titulo_ficha_centro),
                onBack = onBack,
                onProfileClick = onProfileClick,
            )
        },
    ) { margenInterior ->
        when (uiState) {
            CenterDetailUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(margenInterior),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }

            CenterDetailUiState.NotFound -> CentroNoEncontrado(
                onBack = onBack,
                modifier = Modifier.padding(margenInterior),
            )

            is CenterDetailUiState.Success -> ContenidoFicha(
                estado = uiState,
                contentPadding = margenInterior,
                onBack = onBack,
                onCall = onCall,
                onDirections = onDirections,
                onOpenWeb = onOpenWeb,
            )
        }
    }
}

@Composable
private fun ContenidoFicha(
    estado: CenterDetailUiState.Success,
    contentPadding: PaddingValues,
    onBack: () -> Unit,
    onCall: (String) -> Unit,
    onDirections: (Centro) -> Unit,
    onOpenWeb: (String) -> Unit,
) {
    val centro = estado.centro
    val direccion = LocalLayoutDirection.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // El contenido pasa por debajo de la barra superior (semitransparente).
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(direccion) + Spacing.margin,
            end = contentPadding.calculateEndPadding(direccion) + Spacing.margin,
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + 40.dp,
        ),
    ) {
        item { BackLink(onBack = onBack, modifier = Modifier.padding(vertical = Spacing.sm)) }
        item {
            CenterMapPreview(
                distanciaKm = estado.distanciaKm,
                onClick = { onDirections(centro) },
            )
        }
        item {
            CenterHeader(
                centro = centro,
                estadoApertura = estado.estadoApertura,
                modifier = Modifier.padding(top = 12.dp, bottom = Spacing.md),
            )
        }
        item {
            QuickActions(
                centro = centro,
                onCall = onCall,
                onDirections = onDirections,
                onOpenWeb = onOpenWeb,
                modifier = Modifier.padding(bottom = SeparacionTarjetas),
            )
        }
        if (centro.direccion.isNotBlank() || centro.transportes.isNotEmpty()) {
            item { LocationCard(centro, Modifier.padding(bottom = SeparacionTarjetas)) }
        }
        if (centro.horarios.isNotEmpty()) {
            item { ScheduleCard(centro, Modifier.padding(bottom = SeparacionTarjetas)) }
        }
        item { AppointmentCard(centro.requiereCita, Modifier.padding(bottom = SeparacionTarjetas)) }
        item { WhatToBringCard() }
        item { SourceNotice(centro.fechaVerificacion, Modifier.padding(top = Spacing.sm + SeparacionTarjetas)) }
    }
}

// ---------------------------------------------------------------------------------------
// 1. Volver
// ---------------------------------------------------------------------------------------

@Composable
private fun BackLink(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            // Zona táctil de al menos 48 dp aunque la píldora sea más baja.
            .minimumInteractiveComponentSize()
            .clip(AppShapes.pill)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(role = Role.Button, onClick = onBack)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = stringResource(R.string.ficha_volver_centros),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

// ---------------------------------------------------------------------------------------
// 2. Mapa
// ---------------------------------------------------------------------------------------

/**
 * Vista previa del mapa con el pin del centro. [mapContent] es un hueco para poder cambiar
 * la imagen estática por un mapa real (maps-compose) sin tocar el resto.
 */
@Composable
fun CenterMapPreview(
    distanciaKm: Double?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mapContent: @Composable () -> Unit = {
        Image(
            painter = painterResource(R.drawable.map_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    },
) {
    val descripcion = stringResource(R.string.cd_mapa)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp)
            .shadow(2.dp, AppShapes.cardSmall)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, AppShapes.cardSmall)
            .clip(AppShapes.cardSmall)
            .clickable(
                onClickLabel = stringResource(R.string.cd_abrir_ruta),
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { contentDescription = descripcion },
    ) {
        mapContent()
        MapPin(modifier = Modifier.align(Alignment.Center))
        if (distanciaKm != null) {
            DistancePill(
                distanciaKm = distanciaKm,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
            )
        }
        // TODO: píldora "Plano satélite" (icono ic_layers) cuando haya mapa real; sobre la
        //  imagen estática no haría nada.
    }
}

@Composable
private fun MapPin(modifier: Modifier = Modifier) {
    val transicion = rememberInfiniteTransition(label = "pin")
    val salto by transicion.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 900), RepeatMode.Reverse),
        label = "saltoPin",
    )
    val colorSombra = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        IconCircle(
            icon = R.drawable.ic_local_hospital_filled,
            size = 40.dp,
            background = MaterialTheme.colorScheme.primaryContainer,
            tint = MaterialTheme.colorScheme.onPrimary,
            iconSize = 22.dp,
            modifier = Modifier.graphicsLayer { translationY = salto * density },
        )
        Spacer(Modifier.height(2.dp))
        Canvas(Modifier.size(width = 12.dp, height = 6.dp)) { drawOval(colorSombra) }
    }
}

@Composable
private fun DistancePill(distanciaKm: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f), AppShapes.pill)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_near_me),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(16.dp),
        )
        Text(
            // Formato español: coma decimal ("3,8").
            text = stringResource(R.string.ficha_distancia, String.format(Espanol, "%.1f", distanciaKm)),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ---------------------------------------------------------------------------------------
// 3. Cabecera
// ---------------------------------------------------------------------------------------

@Composable
private fun CenterHeader(centro: Centro, estadoApertura: EstadoApertura?, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (estadoApertura != null && textoEstado(estadoApertura) != null) {
            OpeningStatusBadge(estadoApertura)
            Spacer(Modifier.height(12.dp))
        }
        Text(
            text = centro.nombre,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        if (centro.esReferenciaRedmo) {
            Spacer(Modifier.height(Spacing.sm))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_verified_filled),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.ficha_referencia_redmo),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Texto del estado, o null si el centro no tiene ninguna apertura próxima. */
@Composable
private fun textoEstado(estado: EstadoApertura): String? = when (estado) {
    is EstadoApertura.Abierto -> stringResource(R.string.ficha_abierto_hasta, estado.cierre.format(FormatoHora))
    is EstadoApertura.Cerrado -> estado.proximaApertura?.let { apertura ->
        val dia = when (estado.diasHastaApertura) {
            0 -> stringResource(R.string.ficha_dia_hoy)
            1 -> stringResource(R.string.ficha_dia_manana)
            else -> stringResource(
                R.string.ficha_dia_semana,
                apertura.dayOfWeek.getDisplayName(TextStyle.FULL, Espanol),
            )
        }
        stringResource(R.string.ficha_cerrado_abre, dia, apertura.format(FormatoHora))
    }
}

@Composable
private fun OpeningStatusBadge(estado: EstadoApertura) {
    val texto = textoEstado(estado) ?: return
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    when (estado) {
        is EstadoApertura.Abierto -> StatusBadge(
            text = texto,
            containerColor = extendidos.tertiaryFixed,
            contentColor = extendidos.onTertiaryFixed,
            dotColor = colores.tertiary,
        )
        is EstadoApertura.Cerrado -> StatusBadge(
            text = texto,
            containerColor = colores.surfaceContainerHigh,
            contentColor = colores.onSurfaceVariant,
            dotColor = colores.outline,
        )
    }
}

// ---------------------------------------------------------------------------------------
// 4. Acciones rápidas
// ---------------------------------------------------------------------------------------

@Composable
private fun QuickActions(
    centro: Centro,
    onCall: (String) -> Unit,
    onDirections: (Centro) -> Unit,
    onOpenWeb: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme
    val web = centro.web?.takeIf { it.isNotBlank() }
    // IntrinsicSize.Min + fillMaxHeight: las tres baldosas miden lo que la más alta.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val baldosa = Modifier
            .weight(1f)
            .fillMaxHeight()
        if (centro.telefono.isNotBlank()) {
            QuickActionTile(
                icon = R.drawable.ic_call_filled,
                title = stringResource(R.string.ficha_accion_llamar),
                subtitle = formatearTelefono(centro.telefono),
                containerColor = colores.surfaceContainerLow,
                circleColor = colores.primary.copy(alpha = 0.10f),
                iconTint = colores.primary,
                textColor = colores.onSurface,
                subtitleColor = colores.onSurfaceVariant,
                onClick = { onCall(centro.telefono) },
                modifier = baldosa,
            )
        }
        QuickActionTile(
            icon = R.drawable.ic_directions_filled,
            title = stringResource(R.string.ficha_accion_como_llegar),
            subtitle = stringResource(R.string.ficha_accion_ruta),
            containerColor = colores.secondaryContainer,
            circleColor = colores.onSecondaryContainer.copy(alpha = 0.15f),
            iconTint = colores.onSecondaryContainer,
            textColor = colores.onSecondaryContainer,
            subtitleColor = colores.onSecondaryContainer.copy(alpha = 0.8f),
            onClick = { onDirections(centro) },
            modifier = baldosa,
        )
        if (web != null) {
            QuickActionTile(
                icon = R.drawable.ic_public,
                title = stringResource(R.string.ficha_accion_web),
                subtitle = stringResource(R.string.ficha_accion_enlace),
                containerColor = colores.surfaceContainerLow,
                circleColor = colores.primary.copy(alpha = 0.10f),
                iconTint = colores.primary,
                textColor = colores.onSurface,
                subtitleColor = colores.onSurfaceVariant,
                onClick = { onOpenWeb(web) },
                modifier = baldosa,
            )
        }
    }
}

@Composable
private fun QuickActionTile(
    @DrawableRes icon: Int,
    title: String,
    subtitle: String,
    containerColor: Color,
    circleColor: Color,
    iconTint: Color,
    textColor: Color,
    subtitleColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interaccion = remember { MutableInteractionSource() }
    val pulsado by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(if (pulsado) 0.98f else 1f, label = "escalaAccion")
    Surface(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = escala
            scaleY = escala
        },
        shape = AppShapes.cardSmall,
        color = containerColor,
        shadowElevation = 2.dp,
        interactionSource = interaccion,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            IconCircle(icon = icon, size = 40.dp, background = circleColor, tint = iconTint, iconSize = 20.dp)
            Spacer(Modifier.height(Spacing.sm))
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = textColor)
            Text(
                text = subtitle,
                style = AppTypography.caption,
                color = subtitleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ---------------------------------------------------------------------------------------
// 5. Tarjetas de información
// ---------------------------------------------------------------------------------------

/** Tarjeta de la ficha: esquinas de 12 dp y relleno de 16 dp. */
@Composable
private fun InfoCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    SectionCard(
        modifier = modifier,
        shape = AppShapes.cardSmall,
        contentPadding = Spacing.md,
    ) { content() }
}

@Composable
private fun LocationCard(centro: Centro, modifier: Modifier = Modifier) {
    InfoCard(modifier) {
        CardHeader(icon = R.drawable.ic_pin_drop_filled, title = stringResource(R.string.ficha_ubicacion_titulo))
        if (centro.direccion.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = centro.direccion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        if (centro.transportes.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                centro.transportes.forEach { transporte ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainerLow, AppShapes.inner)
                            .padding(horizontal = 12.dp, vertical = Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_subway),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(Spacing.sm))
                        Text(
                            text = transporte,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleCard(centro: Centro, modifier: Modifier = Modifier) {
    val extendidos = DonaVidaTheme.extendedColors
    InfoCard(modifier) {
        CardHeader(
            icon = R.drawable.ic_schedule_filled,
            title = stringResource(
                if (centro.atencionContinua) R.string.ficha_horario_continuo_titulo else R.string.ficha_horario_titulo,
            ),
            trailing = if (centro.atencionContinua) {
                {
                    StatusBadge(
                        text = stringResource(R.string.ficha_atencion_continua),
                        containerColor = extendidos.tertiaryFixed,
                        contentColor = extendidos.onTertiaryFixed,
                    )
                }
            } else {
                null
            },
        )
        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            centro.horarios.forEach { franja ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow, AppShapes.inner)
                        .padding(horizontal = Spacing.sm, vertical = 6.dp)
                        .semantics(mergeDescendants = true) {},
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = franja.dias,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(Spacing.sm))
                    Text(
                        text = stringResource(
                            R.string.ficha_franja_horas,
                            franja.apertura.format(FormatoHora),
                            franja.cierre.format(FormatoHora),
                        ),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppointmentCard(requiereCita: Boolean, modifier: Modifier = Modifier) {
    val destacado = stringResource(
        if (requiereCita) R.string.ficha_cita_obligatoria_destacado else R.string.ficha_cita_no_obligatoria_destacado,
    )
    val texto = stringResource(
        if (requiereCita) R.string.ficha_cita_obligatoria else R.string.ficha_cita_no_obligatoria,
        destacado,
    )
    InfoCard(modifier) {
        CardHeader(icon = R.drawable.ic_event_available_filled, title = stringResource(R.string.ficha_cita_titulo))
        Spacer(Modifier.height(12.dp))
        Text(
            text = textoConDestacado(texto, destacado, negrita()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WhatToBringCard(modifier: Modifier = Modifier) {
    InfoCard(modifier) {
        CardHeader(icon = R.drawable.ic_checklist_filled, title = stringResource(R.string.ficha_llevar_titulo))
        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ItemParaLlevar(R.drawable.ic_badge, R.string.ficha_llevar_documento, R.string.ficha_llevar_documento_destacado)
            ItemParaLlevar(R.drawable.ic_restaurant, R.string.ficha_llevar_ayunas, R.string.ficha_llevar_ayunas_destacado)
            ItemParaLlevar(R.drawable.ic_edit_note, R.string.ficha_llevar_consentimiento, R.string.ficha_llevar_consentimiento_destacado)
        }
    }
}

@Composable
private fun ItemParaLlevar(@DrawableRes icon: Int, texto: Int, destacado: Int) {
    val parteDestacada = stringResource(destacado)
    Row(verticalAlignment = Alignment.Top) {
        IconCircle(
            icon = icon,
            size = 24.dp,
            background = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
            tint = MaterialTheme.colorScheme.primary,
            iconSize = 16.dp,
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = textoConDestacado(stringResource(texto, parteDestacada), parteDestacada, negrita()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun negrita() = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

// ---------------------------------------------------------------------------------------
// 6. Fuente
// ---------------------------------------------------------------------------------------

@Composable
private fun SourceNotice(fechaVerificacion: LocalDate?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, AppShapes.cardSmall)
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_info),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = if (fechaVerificacion != null) {
                stringResource(R.string.ficha_fuente_verificada, fechaVerificacion.format(FormatoFecha))
            } else {
                stringResource(R.string.ficha_fuente)
            },
            style = AppTypography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------------------
// Estado: no encontrado
// ---------------------------------------------------------------------------------------

@Composable
private fun CentroNoEncontrado(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.md, Alignment.CenterVertically),
    ) {
        Text(
            text = stringResource(R.string.ficha_no_encontrado),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        TonalPillButton(
            text = stringResource(R.string.ficha_volver_centros),
            icon = R.drawable.ic_arrow_back,
            onClick = onBack,
        )
    }
}

// ---------------------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------------------

private val CentroDeEjemplo = Centro(
    id = "ejemplo-madrid",
    nombre = "Centro de Transfusión de la Comunidad de Madrid",
    tipo = TipoCentro.CENTRO_TRANSFUSION,
    comunidad = "Comunidad de Madrid",
    direccion = "Avda. de la Democracia, s/n, 28032 Madrid",
    latitud = 40.3990,
    longitud = -3.6190,
    telefono = "913017200",
    web = "https://www.comunidad.madrid/servicios/salud/donacion-medula-osea",
    horario = "",
    requiereCita = false,
    urlFuente = "https://www.comunidad.madrid/servicios/salud/donacion-medula-osea",
    fechaVerificacion = LocalDate.of(2026, 11, 3),
    esReferenciaRedmo = true,
    transportes = listOf("Metro Valdebernardo (Línea 9) a 350 m"),
    horarios = listOf(
        FranjaHorario(
            dias = "Lunes a sábado",
            apertura = LocalTime.of(8, 30),
            cierre = LocalTime.of(21, 0),
            diasSemana = setOf(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY,
            ),
        ),
        FranjaHorario(
            dias = "Domingos y festivos",
            apertura = LocalTime.of(9, 0),
            cierre = LocalTime.of(20, 0),
            diasSemana = setOf(DayOfWeek.SUNDAY),
        ),
    ),
    atencionContinua = true,
)

private val Abierto = EstadoApertura.Abierto(LocalTime.of(21, 0))
private val Cerrado = EstadoApertura.Cerrado(LocalDateTime.of(2026, 11, 16, 8, 30), diasHastaApertura = 1)

@Composable
private fun FichaPreview(estado: CenterDetailUiState) {
    DonaVidaTheme {
        CenterDetailScreen(
            uiState = estado,
            onBack = {},
            onProfileClick = {},
            onCall = {},
            onDirections = {},
            onOpenWeb = {},
        )
    }
}

@Preview(heightDp = 1700)
@Composable
private fun FichaAbiertaPreview() {
    FichaPreview(CenterDetailUiState.Success(CentroDeEjemplo, Abierto, distanciaKm = 3.8))
}

@Preview(heightDp = 1700)
@Composable
private fun FichaCerradaSinDistanciaPreview() {
    FichaPreview(CenterDetailUiState.Success(CentroDeEjemplo, Cerrado, distanciaKm = null))
}

@Preview
@Composable
private fun FichaCargandoPreview() {
    FichaPreview(CenterDetailUiState.Loading)
}

@Preview
@Composable
private fun FichaNoEncontradaPreview() {
    FichaPreview(CenterDetailUiState.NotFound)
}

@Composable
private fun BloquePreview(content: @Composable () -> Unit) {
    DonaVidaTheme {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(Spacing.margin),
        ) { content() }
    }
}

@Preview
@Composable
private fun BackLinkPreview() {
    BloquePreview { BackLink(onBack = {}) }
}

@Preview
@Composable
private fun CenterMapPreviewPreview() {
    BloquePreview { CenterMapPreview(distanciaKm = 3.8, onClick = {}) }
}

@Preview
@Composable
private fun CenterHeaderAbiertoPreview() {
    BloquePreview { CenterHeader(CentroDeEjemplo, Abierto) }
}

@Preview
@Composable
private fun CenterHeaderCerradoPreview() {
    BloquePreview { CenterHeader(CentroDeEjemplo, Cerrado) }
}

@Preview
@Composable
private fun QuickActionsPreview() {
    BloquePreview { QuickActions(CentroDeEjemplo, onCall = {}, onDirections = {}, onOpenWeb = {}) }
}

@Preview
@Composable
private fun QuickActionsSinWebPreview() {
    BloquePreview { QuickActions(CentroDeEjemplo.copy(web = null), onCall = {}, onDirections = {}, onOpenWeb = {}) }
}

@Preview
@Composable
private fun LocationCardPreview() {
    BloquePreview { LocationCard(CentroDeEjemplo) }
}

@Preview
@Composable
private fun ScheduleCardPreview() {
    BloquePreview { ScheduleCard(CentroDeEjemplo) }
}

@Preview
@Composable
private fun AppointmentCardPreview() {
    BloquePreview { AppointmentCard(requiereCita = false) }
}

@Preview
@Composable
private fun WhatToBringCardPreview() {
    BloquePreview { WhatToBringCard() }
}

@Preview
@Composable
private fun SourceNoticePreview() {
    BloquePreview { SourceNotice(LocalDate.of(2026, 11, 3)) }
}
