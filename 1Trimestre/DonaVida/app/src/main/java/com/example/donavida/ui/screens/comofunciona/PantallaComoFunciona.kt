package com.example.donavida.ui.screens.comofunciona

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.MetodoDonacion
import com.example.donavida.data.model.TipoDonacion
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.InfoChip
import com.example.donavida.ui.components.PrimaryPillButton
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.screens.sabermas.ListaViewModel
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing
import java.util.Locale

/** Versión "con estado": carga los métodos de donación y se los pasa a [ContenidoComoFunciona]. */
@Composable
fun PantallaComoFunciona(
    onBuscarCentros: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaViewModel<MetodoDonacion> = viewModel(factory = ListaViewModel.fabrica { repositorioMetodos.obtenerMetodos() }),
) {
    val metodos by viewModel.elementos.collectAsStateWithLifecycle()
    ContenidoComoFunciona(metodos = metodos.orEmpty(), onBuscarCentros = onBuscarCentros, modifier = modifier)
}

@Composable
fun ContenidoComoFunciona(
    metodos: List<MetodoDonacion>,
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
        item { MetodosDonacion(metodos) }
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
// 3. Métodos de donación (textos en assets/data/metodos.json)
// ---------------------------------------------------------------------------------------

/** Lo que no viene en el JSON: icono, colores del círculo y si lleva el chip "La más habitual". */
private data class EstiloMetodo(
    @param:DrawableRes val icono: Int,
    val fondo: Color,
    val tinte: Color,
    val masHabitual: Boolean = false,
)

@Composable
private fun estiloMetodo(tipo: TipoDonacion): EstiloMetodo {
    val e = DonaVidaTheme.extendedColors
    return when (tipo) {
        TipoDonacion.MEDULA_OSEA -> EstiloMetodo(R.drawable.ic_medical_services, e.primaryFixed, e.onPrimaryFixed)
        TipoDonacion.SANGRE_PERIFERICA -> EstiloMetodo(R.drawable.ic_bloodtype, e.secondaryFixed, e.onSecondaryFixed, masHabitual = true)
        TipoDonacion.CORDON_UMBILICAL -> EstiloMetodo(R.drawable.ic_child_care, e.tertiaryFixed, e.onTertiaryFixed)
    }
}

@Composable
private fun MetodosDonacion(metodos: List<MetodoDonacion>) {
    val colores = MaterialTheme.colorScheme
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.cf_metodos_titulo),
                style = MaterialTheme.typography.headlineSmall,
                color = colores.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() },
            )
            StatusBadge(
                text = stringResource(R.string.cf_metodos_numero),
                containerColor = colores.surfaceContainer,
                contentColor = colores.primary,
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
            metodos.forEach { TarjetaMetodo(it) }
        }
    }
}

@Composable
private fun TarjetaMetodo(metodo: MetodoDonacion) {
    val colores = MaterialTheme.colorScheme
    val estilo = estiloMetodo(metodo.tipo)
    val espanol = Locale.forLanguageTag("es-ES")
    SectionCard(shape = AppShapes.cardSmall, contentPadding = Spacing.md) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(icon = estilo.icono, size = 48.dp, background = estilo.fondo, tint = estilo.tinte, iconSize = 24.dp)
            Spacer(Modifier.width(Spacing.md))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(metodo.titulo, style = MaterialTheme.typography.headlineSmall, color = colores.onSurface)
                if (estilo.masHabitual) {
                    InfoChip(R.drawable.ic_check_circle, stringResource(R.string.cf_metodo_mas_habitual))
                }
            }
        }
        Spacer(Modifier.height(Spacing.md))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            metodo.apartados.forEach { apartado ->
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = apartado.etiqueta.uppercase(espanol),
                        style = MaterialTheme.typography.labelSmall,
                        color = colores.primary,
                    )
                    Text(apartado.texto, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
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
            ContenidoComoFunciona(metodos = emptyList(), onBuscarCentros = {})
        }
    }
}
