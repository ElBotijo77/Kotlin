package com.example.donavida.ui.screens.historias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.Historia
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.screens.fichacentro.abrirWeb
import com.example.donavida.ui.screens.sabermas.ListaViewModel
import com.example.donavida.ui.screens.sabermas.PantallaSecundaria
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.Spacing

/** Casos reales publicados en medios, con enlace a la noticia cuando lo hay. */
@Composable
fun PantallaHistorias(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaViewModel<Historia> = viewModel(factory = ListaViewModel.fabrica { repositorioHistorias.obtenerHistorias() }),
) {
    val casos by viewModel.elementos.collectAsStateWithLifecycle()
    val context = LocalContext.current
    PantallaSecundaria(titulo = stringResource(R.string.titulo_historias), onVolver = onVolver, modifier = modifier) {
        items(casos.orEmpty(), key = { it.id }) { caso ->
            TarjetaCaso(caso, onVerFuente = { url -> abrirWeb(context, url) })
        }
        item {
            Text(
                text = stringResource(R.string.casos_nota),
                style = AppTypography.caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun TarjetaCaso(caso: Historia, onVerFuente: (String) -> Unit) {
    val colores = MaterialTheme.colorScheme
    SectionCard(contentPadding = Spacing.md) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(caso.titulo, style = MaterialTheme.typography.headlineSmall, color = colores.onSurface)
            caso.subtitulo?.let { Text(it, style = AppTypography.caption, color = colores.primary) }
        }
        Spacer(Modifier.size(Spacing.sm))
        Text(caso.texto, style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
        caso.fuenteUrl?.let { url ->
            TextButton(onClick = { onVerFuente(url) }) {
                Text(stringResource(R.string.casos_ver_fuente), style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.width(Spacing.xs))
                Icon(painterResource(R.drawable.ic_open_in_new), contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}
