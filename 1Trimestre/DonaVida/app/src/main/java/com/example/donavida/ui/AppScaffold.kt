package com.example.donavida.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.navigation.BarraInferior
import com.example.donavida.ui.navigation.Pestana
import com.example.donavida.ui.navigation.Screen
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/**
 * Armazón común: barra superior (solo en las cuatro pestañas; las pantallas secundarias
 * traen la suya con flecha de volver) y barra inferior.
 *
 * Las barras son semitransparentes y el contenido pasa por debajo de ellas, así que cada
 * pantalla recibe en [content] el hueco que deben dejar sus primeros y últimos elementos.
 */
@Composable
fun AppScaffold(
    rutaActual: String?,
    onPestanaPulsada: (Pestana) -> Unit,
    onNotificaciones: () -> Unit,
    onPerfil: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val pestanaActual = Pestana.deRuta(rutaActual)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (pestanaActual != null) {
                AppTopBar(
                    seccion = stringResource(pestanaActual.etiqueta),
                    onNotificaciones = onNotificaciones,
                    onPerfil = onPerfil,
                )
            }
        },
        bottomBar = {
            // La ficha de centro es una pantalla de detalle a pantalla completa, sin pestañas.
            if (rutaActual != Screen.FichaCentro.route) BarraInferior(rutaActual, onPestanaPulsada)
        },
        content = content,
    )
}

/**
 * Barra superior de 64 dp: logo, nombre de la app con la sección debajo, notificaciones y
 * avatar. El desenfoque de fondo del diseño (backdrop-blur) no existe en Compose, así que
 * se usa solo la transparencia.
 */
@Composable
fun AppTopBar(
    seccion: String,
    onNotificaciones: () -> Unit,
    onPerfil: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                // El diseño pide 80 % con desenfoque; sin desenfoque el texto de debajo se lee a
                // través y estorba, así que se sube la opacidad.
                drawRect(colores.surface.copy(alpha = 0.95f))
                // Sombra muy suave: un degradado de 6 dp justo debajo de la barra.
                val alto = 6.dp.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.06f), Color.Transparent),
                        startY = size.height,
                        endY = size.height + alto,
                    ),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, alto),
                )
            }
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .height(64.dp)
            .padding(start = Spacing.margin, end = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(32.dp),
            contentScale = ContentScale.FillHeight,
        )
        Spacer(Modifier.width(Spacing.sm + Spacing.xs))
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics(mergeDescendants = true) { heading() },
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                color = colores.primary,
                maxLines = 1,
            )
            Text(
                text = seccion,
                style = AppTypography.caption,
                color = colores.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        IconButton(onClick = onNotificaciones, modifier = Modifier.size(44.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_notifications),
                contentDescription = stringResource(R.string.cd_notificaciones),
                tint = colores.onSurfaceVariant,
            )
        }
        val descripcionPerfil = stringResource(R.string.cd_perfil)
        // Zona táctil de 44 dp aunque el avatar mida 32 dp.
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .clickable(role = Role.Button, onClick = onPerfil)
                .semantics { contentDescription = descripcionPerfil },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.avatar_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(2.dp, colores.primary.copy(alpha = 0.2f), CircleShape),
            )
        }
    }
}

@Preview
@Composable
private fun AppTopBarPreview() {
    DonaVidaTheme {
        AppTopBar(seccion = "Inicio", onNotificaciones = {}, onPerfil = {})
    }
}
