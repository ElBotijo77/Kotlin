package com.example.donavida.ui.screens.sabermas

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.BloqueProblema
import com.example.donavida.data.model.Problema
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/** "Qué problemas pueden solucionar": tratamientos actuales y líneas de investigación. */
@Composable
fun PantallaProblemas(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaViewModel<Problema> = viewModel(factory = ListaViewModel.fabrica { repositorioProblemas.obtenerProblemas() }),
) {
    val problemas by viewModel.elementos.collectAsStateWithLifecycle()
    val extendidos = DonaVidaTheme.extendedColors
    // Cada bloque: título, insignia, introducción y colores de la insignia y los iconos.
    val bloques = listOf(
        Bloque(BloqueProblema.ACTUAL, R.string.pr_actual_titulo, R.string.pr_actual_insignia, R.string.pr_actual_intro,
            extendidos.tertiaryFixed, extendidos.onTertiaryFixed),
        Bloque(BloqueProblema.INVESTIGACION, R.string.pr_investigacion_titulo, R.string.pr_investigacion_insignia,
            R.string.pr_investigacion_intro, extendidos.secondaryFixed, extendidos.onSecondaryFixed),
    )

    PantallaSecundaria(titulo = stringResource(R.string.sm_problemas_titulo), onVolver = onVolver, modifier = modifier) {
        bloques.forEach { bloque ->
            item(key = bloque.tipo) { CabeceraBloque(bloque) }
            items(problemas.orEmpty().filter { it.bloque == bloque.tipo }, key = { it.id }) { problema ->
                TarjetaProblema(problema, bloque.fondo, bloque.contenido)
            }
        }
    }
}

private data class Bloque(
    val tipo: BloqueProblema,
    @param:StringRes val titulo: Int,
    @param:StringRes val insignia: Int,
    @param:StringRes val intro: Int,
    val fondo: Color,
    val contenido: Color,
)

@Composable
private fun CabeceraBloque(bloque: Bloque) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                text = stringResource(bloque.titulo),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() },
            )
            StatusBadge(text = stringResource(bloque.insignia), containerColor = bloque.fondo, contentColor = bloque.contenido)
        }
        Text(
            text = stringResource(bloque.intro),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TarjetaProblema(problema: Problema, fondoIcono: Color, colorIcono: Color) {
    SectionCard(shape = AppShapes.cardSmall, contentPadding = Spacing.md) {
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            IconCircle(icon = iconoProblema(problema.icono), size = 40.dp, background = fondoIcono, tint = colorIcono)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(problema.titulo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(problema.texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Traduce el nombre de icono del JSON a su drawable. */
@DrawableRes
private fun iconoProblema(nombre: String): Int = when (nombre) {
    "bloodtype" -> R.drawable.ic_bloodtype
    "healing" -> R.drawable.ic_healing
    "autorenew" -> R.drawable.ic_autorenew
    "psychology" -> R.drawable.ic_psychology
    "monitor_heart" -> R.drawable.ic_monitor_heart
    "favorite" -> R.drawable.ic_favorite
    else -> R.drawable.ic_info
}
