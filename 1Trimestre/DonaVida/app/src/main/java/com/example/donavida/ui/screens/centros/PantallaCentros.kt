package com.example.donavida.ui.screens.centros

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.FranjaHorario
import com.example.donavida.data.model.ServicioCentro
import com.example.donavida.data.model.TipoCentro
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.PrimaryPillButton
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.components.TonalPillButton
import com.example.donavida.ui.screens.fichacentro.abrirRuta
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing
import com.example.donavida.util.EstadoApertura
import com.example.donavida.util.formatearDistancia
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private const val PermisoUbicacion = Manifest.permission.ACCESS_COARSE_LOCATION
private val Espanol: Locale = Locale.forLanguageTag("es-ES")
private val FormatoHora: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")
private val FormatoFecha: DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")

/** Margen entre los bloques grandes de la pantalla. */
private val SeparacionBloques = 24.dp

/**
 * Versión "con estado": obtiene el ViewModel y se encarga del permiso de ubicación, que
 * necesita una Activity.
 *
 * @param contentPadding hueco de las barras del AppScaffold: la lista pasa por debajo.
 */
@Composable
fun PantallaCentros(
    contentPadding: PaddingValues,
    onVerDetalles: (String) -> Unit,
    onDudas: () -> Unit,
    viewModel: CentrosViewModel = viewModel(factory = CentrosViewModel.Factory),
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current

    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) {
            viewModel.alConcederPermiso()
        } else {
            // Si el sistema ya no muestra la explicación es que la persona marcó "no volver a
            // preguntar": solo se puede conceder desde los ajustes.
            val permanente = activity != null &&
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, PermisoUbicacion)
            viewModel.alDenegarPermiso(permanente)
        }
    }

    // Al volver de los ajustes, si se ha concedido el permiso, se busca la ubicación.
    LifecycleResumeEffect(estado.estadoUbicacion) {
        if (estado.estadoUbicacion == EstadoUbicacion.DENEGADA && tienePermisoUbicacion(context)) {
            viewModel.alRecuperarPermiso()
        }
        onPauseOrDispose { }
    }

    PantallaCentrosContenido(
        estado = estado,
        contentPadding = contentPadding,
        onCambiarConsulta = viewModel::alCambiarConsulta,
        onUsarUbicacion = {
            if (estado.estadoUbicacion == EstadoUbicacion.DENEGADA) {
                abrirAjustesDeLaApp(context)
            } else {
                pedirPermiso.launch(PermisoUbicacion)
            }
        },
        onCambiarVista = viewModel::alCambiarVista,
        onPulsarServicio = viewModel::alPulsarServicio,
        onBorrarBusqueda = viewModel::alBorrarBusqueda,
        onVerDetalles = onVerDetalles,
        onComoLlegar = { centro -> abrirRuta(context, centro) },
        onDudas = onDudas,
    )
}

private fun tienePermisoUbicacion(context: Context) =
    ContextCompat.checkSelfPermission(context, PermisoUbicacion) == PackageManager.PERMISSION_GRANTED

private fun abrirAjustesDeLaApp(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:${context.packageName}".toUri())
    context.startActivity(intent)
}

/** Lista de centros. Solo pinta [estado] y avisa con callbacks. */
@Composable
fun PantallaCentrosContenido(
    estado: CentrosUiState,
    contentPadding: PaddingValues,
    onCambiarConsulta: (String) -> Unit,
    onUsarUbicacion: () -> Unit,
    onCambiarVista: (VistaCentros) -> Unit,
    onPulsarServicio: (ServicioCentro) -> Unit,
    onBorrarBusqueda: () -> Unit,
    onVerDetalles: (String) -> Unit,
    onComoLlegar: (Centro) -> Unit,
    onDudas: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (estado.cargando) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) { CircularProgressIndicator() }
        return
    }

    val direccion = LocalLayoutDirection.current
    val alphaLista = if (estado.vista == VistaCentros.MAPA) 0.9f else 1f
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(direccion) + Spacing.margin,
            end = contentPadding.calculateEndPadding(direccion) + Spacing.margin,
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + Spacing.xl,
        ),
    ) {
        item { CabeceraCentros(Modifier.padding(top = Spacing.md, bottom = Spacing.sm)) }
        item {
            CajaBusqueda(
                consulta = estado.consulta,
                estadoUbicacion = estado.estadoUbicacion,
                onCambiarConsulta = onCambiarConsulta,
                onUsarUbicacion = onUsarUbicacion,
                modifier = Modifier.padding(top = Spacing.sm),
            )
        }
        item {
            Column(
                modifier = Modifier.padding(top = SeparacionBloques),
                verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            ) {
                SelectorVista(vista = estado.vista, onCambiarVista = onCambiarVista)
                MapaCentrosPreview(
                    numeroCentros = estado.centros.size,
                    hayUbicacion = estado.hayUbicacion,
                    vista = estado.vista,
                )
            }
        }
        item {
            FiltroEspecialidades(
                servicioSeleccionado = estado.servicioSeleccionado,
                onPulsarServicio = onPulsarServicio,
                modifier = Modifier.padding(top = SeparacionBloques),
            )
        }
        if (estado.centros.isEmpty()) {
            item {
                SinResultados(
                    consulta = estado.consulta,
                    onBorrarBusqueda = onBorrarBusqueda,
                    modifier = Modifier.padding(top = SeparacionBloques),
                )
            }
        } else {
            itemsIndexed(estado.centros, key = { _, item -> item.centro.id }) { indice, item ->
                TarjetaCentro(
                    item = item,
                    // La lista va ordenada por distancia: el primero es el más cercano.
                    esElMasCercano = indice == 0 && item.distanciaKm != null,
                    onVerDetalles = onVerDetalles,
                    onComoLlegar = onComoLlegar,
                    modifier = Modifier
                        .padding(top = if (indice == 0) SeparacionBloques else Spacing.md)
                        .alpha(alphaLista),
                )
            }
        }
        item { NotaDudas(onClick = onDudas, modifier = Modifier.padding(top = SeparacionBloques)) }
    }
}

// ---------------------------------------------------------------------------------------
// 1. Cabecera
// ---------------------------------------------------------------------------------------

@Composable
private fun CabeceraCentros(modifier: Modifier = Modifier) {
    val extendidos = DonaVidaTheme.extendedColors
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(
                icon = R.drawable.ic_verified,
                size = 28.dp,
                background = extendidos.tertiaryFixed,
                tint = extendidos.onTertiaryFixed,
                iconSize = 18.dp,
            )
            Spacer(Modifier.width(Spacing.sm))
            Text(
                text = stringResource(R.string.centros_red_oficial),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.em),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        Text(
            text = stringResource(R.string.centros_titulo),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Text(
            text = stringResource(R.string.centros_subtitulo),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------------------
// 2. Búsqueda y ubicación
// ---------------------------------------------------------------------------------------

@Composable
private fun CajaBusqueda(
    consulta: String,
    estadoUbicacion: EstadoUbicacion,
    onCambiarConsulta: (String) -> Unit,
    onUsarUbicacion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = AppShapes.card,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            CampoBusqueda(consulta = consulta, onCambiarConsulta = onCambiarConsulta)
            BotonUbicacion(estado = estadoUbicacion, onClick = onUsarUbicacion)
            NotaPrivacidad()
        }
    }
}

@Composable
private fun CampoBusqueda(consulta: String, onCambiarConsulta: (String) -> Unit) {
    val colores = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = consulta,
        onValueChange = onCambiarConsulta,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall.copy(color = colores.onSurface),
        cursorBrush = SolidColor(colores.primary),
        // La lista ya se filtra al escribir: la tecla de buscar solo cierra el teclado.
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        decorationBox = { campo ->
            Row(
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .background(colores.surfaceContainerLowest, AppShapes.cardSmall)
                    .padding(start = 12.dp, end = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = colores.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(Spacing.sm))
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (consulta.isEmpty()) {
                        Text(
                            text = stringResource(R.string.centros_busqueda_placeholder),
                            style = MaterialTheme.typography.bodySmall,
                            color = colores.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    campo()
                }
                if (consulta.isNotEmpty()) {
                    IconButton(onClick = { onCambiarConsulta("") }, modifier = Modifier.size(40.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.centros_busqueda_borrar),
                            tint = colores.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                } else {
                    Spacer(Modifier.width(Spacing.sm))
                }
            }
        },
    )
}

@Composable
private fun BotonUbicacion(estado: EstadoUbicacion, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        enabled = estado != EstadoUbicacion.BUSCANDO,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp),
        shape = AppShapes.pill,
        color = colores.surfaceContainerLowest,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (estado) {
                EstadoUbicacion.BUSCANDO -> CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = colores.primary,
                )
                EstadoUbicacion.DETECTADA -> Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = colores.tertiary,
                    modifier = Modifier.size(19.dp),
                )
                else -> Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    contentDescription = null,
                    tint = colores.primary,
                    modifier = Modifier.size(19.dp),
                )
            }
            Spacer(Modifier.width(Spacing.sm))
            Text(
                text = stringResource(
                    when (estado) {
                        EstadoUbicacion.INACTIVA -> R.string.centros_ubicacion_usar
                        EstadoUbicacion.BUSCANDO -> R.string.centros_ubicacion_buscando
                        EstadoUbicacion.DETECTADA -> R.string.centros_ubicacion_detectada
                        EstadoUbicacion.DENEGADA -> R.string.centros_ubicacion_denegada
                        EstadoUbicacion.ERROR -> R.string.centros_ubicacion_error
                    },
                ),
                style = MaterialTheme.typography.labelMedium,
                color = colores.primary,
            )
        }
    }
}

@Composable
private fun NotaPrivacidad() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_lock),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(15.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = stringResource(R.string.centros_privacidad),
            style = AppTypography.caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f, fill = false),
        )
    }
}

// ---------------------------------------------------------------------------------------
// 3. Selector de vista y mapa
// ---------------------------------------------------------------------------------------

@Composable
private fun SelectorVista(vista: VistaCentros, onCambiarVista: (VistaCentros) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, AppShapes.pill)
            .padding(Spacing.xs)
            .selectableGroup(),
    ) {
        OpcionVista(
            texto = stringResource(R.string.centros_vista_lista),
            icono = R.drawable.ic_format_list_bulleted,
            activa = vista == VistaCentros.LISTA,
            onClick = { onCambiarVista(VistaCentros.LISTA) },
            modifier = Modifier.weight(1f),
        )
        OpcionVista(
            texto = stringResource(R.string.centros_vista_mapa),
            icono = R.drawable.ic_map,
            activa = vista == VistaCentros.MAPA,
            onClick = { onCambiarVista(VistaCentros.MAPA) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun OpcionVista(
    texto: String,
    @DrawableRes icono: Int,
    activa: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme
    val colorContenido = if (activa) colores.primary else colores.onSurfaceVariant
    Row(
        modifier = modifier
            .then(if (activa) Modifier.shadow(1.dp, AppShapes.pill) else Modifier)
            .clip(AppShapes.pill)
            .background(if (activa) colores.surfaceContainerLowest else Color.Transparent)
            .selectable(selected = activa, role = Role.Tab, onClick = onClick)
            .padding(vertical = Spacing.sm),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = colorContenido,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(text = texto, style = MaterialTheme.typography.labelMedium, color = colorContenido)
    }
}

/**
 * Vista previa del mapa de centros. [mapContent] es el hueco para cambiar la imagen estática
 * por un mapa real (maps-compose) sin tocar el resto. Los marcadores no se pintan: sobre una
 * imagen estática estarían en posiciones falsas.
 */
@Composable
fun MapaCentrosPreview(
    numeroCentros: Int,
    hayUbicacion: Boolean,
    vista: VistaCentros,
    modifier: Modifier = Modifier,
    mapContent: @Composable () -> Unit = {
        Image(
            painter = painterResource(R.drawable.map_centros_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    },
) {
    val colores = MaterialTheme.colorScheme
    val alto by animateDpAsState(
        targetValue = if (vista == VistaCentros.MAPA) 320.dp else 176.dp,
        label = "altoMapa",
    )
    val descripcion = stringResource(R.string.cd_mapa_centros)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(alto)
            .clip(AppShapes.card)
            .background(colores.surfaceContainerHigh)
            .semantics(mergeDescendants = true) { contentDescription = descripcion },
    ) {
        mapContent()
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, colores.onSurface.copy(alpha = 0.4f)))),
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .background(colores.surfaceContainerLowest.copy(alpha = 0.9f), AppShapes.pill)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_pin_drop),
                contentDescription = null,
                tint = colores.primary,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (hayUbicacion) {
                    pluralStringResource(R.plurals.centros_en_radio, numeroCentros, numeroCentros)
                } else {
                    pluralStringResource(R.plurals.centros_total, numeroCentros, numeroCentros)
                },
                style = MaterialTheme.typography.labelSmall,
                color = colores.primary,
            )
        }
    }
}

// ---------------------------------------------------------------------------------------
// 4. Filtro de especialidades
// ---------------------------------------------------------------------------------------

@Composable
private fun FiltroEspecialidades(
    servicioSeleccionado: ServicioCentro?,
    onPulsarServicio: (ServicioCentro) -> Unit,
    modifier: Modifier = Modifier,
) {
    val servicios = ServicioCentro.entries
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.centros_especialidades),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() },
            )
            Text(
                text = pluralStringResource(R.plurals.centros_especialidades_disponibles, servicios.size, servicios.size),
                style = AppTypography.caption,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            contentPadding = PaddingValues(vertical = 6.dp),
        ) {
            items(servicios) { servicio ->
                ChipFiltro(
                    servicio = servicio,
                    activo = servicio == servicioSeleccionado,
                    onClick = { onPulsarServicio(servicio) },
                )
            }
        }
    }
}

@Composable
private fun ChipFiltro(servicio: ServicioCentro, activo: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val (icono, texto) = when (servicio) {
        ServicioCentro.INSCRIPCION_MEDULA -> R.drawable.ic_how_to_reg to R.string.servicio_inscripcion_medula
        ServicioCentro.SANGRE_CORDON -> R.drawable.ic_child_care to R.string.servicio_sangre_cordon
        ServicioCentro.PUNTOS_TEMPORALES -> R.drawable.ic_event_repeat to R.string.servicio_puntos_temporales
    }
    val colorContenido = if (activo) colores.onPrimary else colores.onSurfaceVariant
    Row(
        modifier = Modifier
            // Zona táctil de 48 dp aunque el chip sea más bajo.
            .minimumInteractiveComponentSize()
            .clip(AppShapes.pill)
            .background(if (activo) colores.primary else colores.surfaceContainerHigh)
            .toggleable(value = activo, role = Role.Checkbox, onValueChange = { onClick() })
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = colorContenido,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(text = stringResource(texto), style = MaterialTheme.typography.labelSmall, color = colorContenido)
    }
}

// ---------------------------------------------------------------------------------------
// 5. Tarjeta de centro
// ---------------------------------------------------------------------------------------

/** Icono, colores y categoría de cada tipo de centro. */
private data class AparienciaTipo(
    @param:DrawableRes val icono: Int,
    val fondo: Color,
    val tinte: Color,
    val colorCategoria: Color,
    @param:StringRes val categoria: Int,
)

@Composable
private fun aparienciaDe(tipo: TipoCentro): AparienciaTipo {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    return when (tipo) {
        TipoCentro.CENTRO_TRANSFUSION -> AparienciaTipo(
            R.drawable.ic_medical_services, extendidos.primaryFixed, colores.primary, colores.primary,
            R.string.tipo_centro_transfusion,
        )
        TipoCentro.HOSPITAL_PUBLICO -> AparienciaTipo(
            R.drawable.ic_domain, extendidos.tertiaryFixed, extendidos.onTertiaryFixed, colores.tertiary,
            R.string.tipo_hospital_publico,
        )
        TipoCentro.PUNTO_INSCRIPCION -> AparienciaTipo(
            R.drawable.ic_diversity_1, extendidos.secondaryFixed, colores.secondary, colores.secondary,
            R.string.tipo_punto_inscripcion,
        )
        TipoCentro.MATERNIDAD -> AparienciaTipo(
            R.drawable.ic_child_care, extendidos.secondaryFixed, colores.secondary, colores.secondary,
            R.string.tipo_maternidad,
        )
    }
}

@Composable
private fun TarjetaCentro(
    item: CentroConDistancia,
    esElMasCercano: Boolean,
    onVerDetalles: (String) -> Unit,
    onComoLlegar: (Centro) -> Unit,
    modifier: Modifier = Modifier,
) {
    val centro = item.centro
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    val apariencia = aparienciaDe(centro.tipo)

    // Toda la tarjeta abre la ficha. Sin ripple (se saldría de las esquinas redondeadas):
    // la respuesta al toque es el mismo encogimiento que el de los botones.
    val interaccion = remember { MutableInteractionSource() }
    val pulsada by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(if (pulsada) 0.98f else 1f, label = "escalaTarjeta")
    SectionCard(
        modifier = modifier
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
            .clickable(
                interactionSource = interaccion,
                indication = null,
                onClickLabel = stringResource(R.string.centros_ver_detalles),
                role = Role.Button,
                onClick = { onVerDetalles(centro.id) },
            ),
        contentPadding = Spacing.md,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            // Cabecera
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconCircle(
                    icon = apariencia.icono,
                    size = 44.dp,
                    background = apariencia.fondo,
                    tint = apariencia.tinte,
                    iconSize = 22.dp,
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(apariencia.categoria).uppercase(Espanol),
                        style = MaterialTheme.typography.labelSmall,
                        color = apariencia.colorCategoria,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = centro.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        color = colores.onSurface,
                        modifier = Modifier.semantics { heading() },
                    )
                }
                if (item.distanciaKm != null) {
                    Spacer(Modifier.width(Spacing.sm))
                    StatusBadge(
                        text = formatearDistancia(item.distanciaKm),
                        containerColor = if (esElMasCercano) extendidos.secondaryFixed else colores.surfaceContainerHigh,
                        contentColor = if (esElMasCercano) extendidos.onSecondaryFixed else colores.onSurface,
                    )
                }
            }

            // Información
            Column(
                modifier = Modifier.padding(top = Spacing.xs),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                val apertura = textoApertura(item)
                if (centro.requiereCita) {
                    FilaInfo(
                        icono = R.drawable.ic_calendar_month,
                        colorIcono = colores.secondary,
                        texto = apertura?.let { stringResource(R.string.centros_requiere_cita, it) }
                            ?: stringResource(R.string.centros_requiere_cita_solo),
                    )
                } else if (apertura != null) {
                    FilaInfo(icono = R.drawable.ic_schedule, colorIcono = colores.tertiary, texto = apertura)
                }
                centro.fechaVerificacion?.let { fecha ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = colores.tertiary,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(Spacing.sm))
                        Text(
                            text = stringResource(R.string.centros_datos_verificados, fecha.format(FormatoFecha)),
                            style = AppTypography.caption,
                            color = colores.onSurfaceVariant,
                        )
                    }
                }
            }

            // Acciones
            Row(
                modifier = Modifier.padding(top = Spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PrimaryPillButton(
                    text = stringResource(R.string.centros_ver_detalles),
                    icon = R.drawable.ic_arrow_forward,
                    iconAtEnd = true,
                    onClick = { onVerDetalles(centro.id) },
                    height = 44.dp,
                    containerColor = colores.primary,
                    contentColor = colores.onPrimary,
                    modifier = Modifier.weight(1f),
                )
                val descripcionRuta = stringResource(R.string.ficha_accion_como_llegar)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colores.surfaceContainer)
                        .clickable(role = Role.Button, onClick = { onComoLlegar(centro) })
                        .semantics { contentDescription = descripcionRuta },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_directions),
                        contentDescription = null,
                        tint = colores.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaInfo(@DrawableRes icono: Int, colorIcono: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,
            tint = colorIcono,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(Spacing.sm))
        Text(text = texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

/**
 * "Abierto hoy de 8:30 a 21:00", el texto de cerrado de la ficha ("Cerrado · abre mañana a
 * las 8:30") o, si el centro no tiene horario detallado, su horario en texto libre.
 */
@Composable
private fun textoApertura(item: CentroConDistancia): String? = when (val estado = item.estadoApertura) {
    is EstadoApertura.Abierto -> item.franjaActual
        ?.let { stringResource(R.string.centros_abierto_de_a, it.apertura.format(FormatoHora), it.cierre.format(FormatoHora)) }
        ?: stringResource(R.string.ficha_abierto_hasta, estado.cierre.format(FormatoHora))
    is EstadoApertura.Cerrado -> estado.proximaApertura?.let { apertura ->
        val dia = when (estado.diasHastaApertura) {
            0 -> stringResource(R.string.ficha_dia_hoy)
            1 -> stringResource(R.string.ficha_dia_manana)
            else -> stringResource(R.string.ficha_dia_semana, apertura.dayOfWeek.getDisplayName(TextStyle.FULL, Espanol))
        }
        stringResource(R.string.ficha_cerrado_abre, dia, apertura.format(FormatoHora))
    }
    null -> item.centro.horario.takeIf { it.isNotBlank() }
}

// ---------------------------------------------------------------------------------------
// 6. Sin resultados y nota final
// ---------------------------------------------------------------------------------------

@Composable
private fun SinResultados(consulta: String, onBorrarBusqueda: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (consulta.isNotBlank()) {
                stringResource(R.string.centros_sin_resultados, consulta.trim())
            } else {
                stringResource(R.string.centros_sin_resultados_filtro)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        TonalPillButton(text = stringResource(R.string.centros_borrar_busqueda), onClick = onBorrarBusqueda)
    }
}

@Composable
private fun NotaDudas(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppShapes.card)
            .background(colores.surfaceContainer)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircle(
            icon = R.drawable.ic_contact_support,
            size = 40.dp,
            background = colores.surfaceContainerLowest,
            tint = colores.primary,
            iconSize = 22.dp,
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.centros_dudas_titulo),
                style = MaterialTheme.typography.labelMedium,
                color = colores.onSurface,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.centros_dudas_texto),
                style = AppTypography.caption,
                color = colores.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(Spacing.sm))
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = colores.onSurfaceVariant,
        )
    }
}

// ---------------------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------------------

private val LunesAViernes = setOf(
    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
)

private fun centroDeEjemplo(
    id: String,
    nombre: String,
    tipo: TipoCentro,
    requiereCita: Boolean,
    franja: FranjaHorario,
) = Centro(
    id = id,
    nombre = nombre,
    tipo = tipo,
    comunidad = "Comunidad de Madrid",
    direccion = "",
    ciudad = "Madrid",
    provincia = "Madrid",
    codigoPostal = "28001",
    servicios = setOf(ServicioCentro.INSCRIPCION_MEDULA),
    latitud = 40.4,
    longitud = -3.7,
    telefono = "",
    horario = "",
    requiereCita = requiereCita,
    urlFuente = "https://ejemplo.es",
    fechaVerificacion = LocalDate.of(2026, 11, 3),
    horarios = listOf(franja),
)

private val FranjaMadrid = FranjaHorario("Lunes a sábado", LocalTime.of(8, 30), LocalTime.of(21, 0), LunesAViernes + DayOfWeek.SATURDAY)
private val FranjaLaPaz = FranjaHorario("Lunes a viernes", LocalTime.of(8, 0), LocalTime.of(20, 30), LunesAViernes)
private val FranjaClinico = FranjaHorario("Lunes a viernes", LocalTime.of(9, 0), LocalTime.of(19, 0), LunesAViernes)

private fun ejemplos(conDistancia: Boolean) = listOf(
    CentroConDistancia(
        centro = centroDeEjemplo("madrid", "Centro de Transfusión de la Comunidad de Madrid", TipoCentro.CENTRO_TRANSFUSION, false, FranjaMadrid),
        estadoApertura = EstadoApertura.Abierto(LocalTime.of(21, 0)),
        franjaActual = FranjaMadrid,
        distanciaKm = if (conDistancia) 2.4 else null,
    ),
    CentroConDistancia(
        centro = centroDeEjemplo("la-paz", "Hospital Universitario La Paz (Banco de Sangre)", TipoCentro.HOSPITAL_PUBLICO, false, FranjaLaPaz),
        estadoApertura = EstadoApertura.Abierto(LocalTime.of(20, 30)),
        franjaActual = FranjaLaPaz,
        distanciaKm = if (conDistancia) 5.1 else null,
    ),
    CentroConDistancia(
        centro = centroDeEjemplo("clinico", "Hospital Clínico San Carlos (Extracciones)", TipoCentro.PUNTO_INSCRIPCION, true, FranjaClinico),
        estadoApertura = EstadoApertura.Cerrado(LocalDateTime.of(2026, 11, 16, 9, 0), diasHastaApertura = 1),
        franjaActual = null,
        distanciaKm = if (conDistancia) 0.85 else null,
    ),
).let { lista -> if (conDistancia) lista.sortedBy { it.distanciaKm } else lista.sortedBy { it.centro.nombre } }

@Composable
private fun PantallaPreview(estado: CentrosUiState) {
    DonaVidaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PantallaCentrosContenido(
                estado = estado,
                contentPadding = PaddingValues(),
                onCambiarConsulta = {},
                onUsarUbicacion = {},
                onCambiarVista = {},
                onPulsarServicio = {},
                onBorrarBusqueda = {},
                onVerDetalles = {},
                onComoLlegar = {},
                onDudas = {},
            )
        }
    }
}

@Preview(heightDp = 2200)
@Composable
private fun CentrosSinUbicacionPreview() {
    PantallaPreview(CentrosUiState(cargando = false, centros = ejemplos(conDistancia = false)))
}

@Preview(heightDp = 2200)
@Composable
private fun CentrosConUbicacionPreview() {
    PantallaPreview(
        CentrosUiState(
            cargando = false,
            estadoUbicacion = EstadoUbicacion.DETECTADA,
            centros = ejemplos(conDistancia = true),
            hayUbicacion = true,
        ),
    )
}

@Preview(heightDp = 2300)
@Composable
private fun CentrosVistaMapaPreview() {
    PantallaPreview(
        CentrosUiState(
            cargando = false,
            vista = VistaCentros.MAPA,
            estadoUbicacion = EstadoUbicacion.DETECTADA,
            centros = ejemplos(conDistancia = true),
            hayUbicacion = true,
        ),
    )
}

@Preview(heightDp = 1200)
@Composable
private fun CentrosSinResultadosPreview() {
    PantallaPreview(CentrosUiState(cargando = false, consulta = "Zaragoza", centros = emptyList()))
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
private fun CabeceraCentrosPreview() {
    BloquePreview { CabeceraCentros() }
}

@Preview
@Composable
private fun CajaBusquedaPreview() {
    BloquePreview {
        CajaBusqueda(consulta = "", estadoUbicacion = EstadoUbicacion.INACTIVA, onCambiarConsulta = {}, onUsarUbicacion = {})
    }
}

@Preview
@Composable
private fun CajaBusquedaBuscandoPreview() {
    BloquePreview {
        CajaBusqueda(consulta = "28006", estadoUbicacion = EstadoUbicacion.BUSCANDO, onCambiarConsulta = {}, onUsarUbicacion = {})
    }
}

@Preview
@Composable
private fun CajaBusquedaDenegadaPreview() {
    BloquePreview {
        CajaBusqueda(consulta = "", estadoUbicacion = EstadoUbicacion.DENEGADA, onCambiarConsulta = {}, onUsarUbicacion = {})
    }
}

@Preview
@Composable
private fun SelectorVistaPreview() {
    BloquePreview { SelectorVista(vista = VistaCentros.LISTA, onCambiarVista = {}) }
}

@Preview
@Composable
private fun MapaCentrosPreviewPreview() {
    BloquePreview { MapaCentrosPreview(numeroCentros = 3, hayUbicacion = true, vista = VistaCentros.LISTA) }
}

@Preview
@Composable
private fun FiltroEspecialidadesPreview() {
    BloquePreview {
        FiltroEspecialidades(servicioSeleccionado = ServicioCentro.INSCRIPCION_MEDULA, onPulsarServicio = {})
    }
}

@Preview
@Composable
private fun TarjetaCentroPreview() {
    BloquePreview {
        TarjetaCentro(item = ejemplos(conDistancia = true).first(), esElMasCercano = true, onVerDetalles = {}, onComoLlegar = {})
    }
}

@Preview
@Composable
private fun TarjetaCentroConCitaPreview() {
    BloquePreview {
        TarjetaCentro(
            item = ejemplos(conDistancia = false).first { it.centro.requiereCita },
            esElMasCercano = false,
            onVerDetalles = {},
            onComoLlegar = {},
        )
    }
}

@Preview
@Composable
private fun SinResultadosPreview() {
    BloquePreview { SinResultados(consulta = "Zaragoza", onBorrarBusqueda = {}) }
}

@Preview
@Composable
private fun NotaDudasPreview() {
    BloquePreview { NotaDudas(onClick = {}) }
}
