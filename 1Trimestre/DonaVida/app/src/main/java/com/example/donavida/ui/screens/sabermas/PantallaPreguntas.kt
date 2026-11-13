package com.example.donavida.ui.screens.sabermas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.data.model.Mito
import com.example.donavida.data.model.PuntoDestacado
import com.example.donavida.ui.components.textoConDestacado
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.Spacing

/** Preguntas frecuentes: una tarjeta desplegable por pregunta, todas cerradas al entrar. */
@Composable
fun PantallaPreguntas(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaViewModel<Mito> = viewModel(factory = ListaViewModel.fabrica { repositorioMitos.obtenerMitos() }),
) {
    val entradas by viewModel.elementos.collectAsStateWithLifecycle()
    PantallaSecundaria(
        titulo = stringResource(R.string.sm_preguntas_titulo),
        onVolver = onVolver,
        modifier = modifier,
        separacion = 12.dp,
    ) {
        items(entradas.orEmpty().filter { !it.esMito }, key = { it.id }) { pregunta ->
            TarjetaPregunta(pregunta)
        }
    }
}

/** Copiado y adaptado del desplegable de Mi donación (DesplegablePorQue), que es privado. */
@Composable
private fun TarjetaPregunta(pregunta: Mito) {
    val colores = MaterialTheme.colorScheme
    var abierta by rememberSaveable(pregunta.id) { mutableStateOf(false) }
    val giro by animateFloatAsState(if (abierta) 180f else 0f, label = "giroFlecha")
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = AppShapes.cardSmall,
        color = colores.surfaceContainerLowest,
        shadowElevation = 2.dp,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(role = Role.Button) { abierta = !abierta }
                    .padding(Spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = pregunta.enunciado,
                    style = MaterialTheme.typography.labelLarge,
                    color = colores.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(Spacing.sm))
                Icon(painterResource(R.drawable.ic_expand_more), null, tint = colores.primary, modifier = Modifier.rotate(giro))
            }
            AnimatedVisibility(visible = abierta) {
                Column(
                    modifier = Modifier.padding(start = Spacing.md, end = Spacing.md, bottom = Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    Text(pregunta.respuesta, style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
                    pregunta.puntos.forEach { FilaPunto(it) }
                }
            }
        }
    }
}

@Composable
private fun FilaPunto(punto: PuntoDestacado) {
    val colores = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        // Centrado con la primera línea del texto (bodyMedium tiene 24 sp de interlineado).
        Box(
            Modifier
                .padding(top = 9.dp)
                .size(6.dp)
                .background(colores.primary, CircleShape),
        )
        Text(
            text = textoConDestacado(punto.texto, punto.destacado, SpanStyle(fontWeight = FontWeight.SemiBold, color = colores.onSurface)),
            style = MaterialTheme.typography.bodyMedium,
            color = colores.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
    }
}
