package com.example.donavida.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/**
 * Barra superior de las pantallas secundarias (las que se abren desde una pestaña):
 * flecha de volver, logo, título y avatar. Mismo alto, fondo y sombra que la de las pestañas.
 */
@Composable
fun SecondaryTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                // Mismo fondo (95 %) y sombra suave que la barra de las pestañas (AppTopBar).
                drawRect(colores.surface.copy(alpha = 0.95f))
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
            .padding(horizontal = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.cd_volver),
                tint = colores.primary,
            )
        }
        Spacer(Modifier.width(Spacing.xs))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(32.dp),
            contentScale = ContentScale.FillHeight,
        )
        Spacer(Modifier.width(Spacing.sm + Spacing.xs))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = colores.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
        )
        // Solo decorativo: no hay pantalla de perfil (Mi donación ya no está en la app).
        Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
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
private fun SecondaryTopBarPreview() {
    DonaVidaTheme {
        SecondaryTopBar(title = "Ficha Centro", onBack = {})
    }
}
