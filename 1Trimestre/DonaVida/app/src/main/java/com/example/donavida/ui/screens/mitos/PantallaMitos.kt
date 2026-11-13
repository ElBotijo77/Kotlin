package com.example.donavida.ui.screens.mitos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.Mito
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.StatusBadge
import com.example.donavida.ui.screens.sabermas.ListaViewModel
import com.example.donavida.ui.screens.sabermas.PantallaSecundaria
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/** Mitos: cada tarjeta enfrenta lo que se dice ("MITO") con lo que es verdad ("REALIDAD"). */
@Composable
fun PantallaMitos(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaViewModel<Mito> = viewModel(factory = ListaViewModel.fabrica { repositorioMitos.obtenerMitos() }),
) {
    val entradas by viewModel.elementos.collectAsStateWithLifecycle()
    PantallaSecundaria(
        titulo = stringResource(R.string.titulo_mitos),
        onVolver = onVolver,
        modifier = modifier,
        separacion = 12.dp,
    ) {
        items(entradas.orEmpty().filter { it.esMito }, key = { it.id }) { mito -> TarjetaMito(mito) }
    }
}

@Composable
private fun TarjetaMito(mito: Mito) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    SectionCard(shape = AppShapes.cardSmall, contentPadding = Spacing.md) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            StatusBadge(
                text = stringResource(R.string.mitos_mito),
                containerColor = extendidos.secondaryFixed,
                contentColor = extendidos.onSecondaryFixed,
            )
            Text(mito.enunciado, style = MaterialTheme.typography.labelLarge, color = colores.onSurface)
        }
        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            StatusBadge(
                text = stringResource(R.string.mitos_realidad),
                containerColor = extendidos.tertiaryFixed,
                contentColor = extendidos.onTertiaryFixed,
            )
            Text(mito.respuesta, style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
        }
    }
}
