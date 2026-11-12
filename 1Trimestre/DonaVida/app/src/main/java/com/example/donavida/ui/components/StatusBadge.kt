package com.example.donavida.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/** Píldora de estado ("Abierto hoy hasta las 21:00") con un punto de color opcional delante. */
@Composable
fun StatusBadge(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
) {
    Row(
        modifier = modifier
            .background(containerColor, AppShapes.pill)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (dotColor != null) {
            Box(
                Modifier
                    .size(8.dp)
                    .background(dotColor, CircleShape),
            )
        }
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = contentColor)
    }
}

@Preview
@Composable
private fun StatusBadgePreview() {
    DonaVidaTheme {
        val colores = MaterialTheme.colorScheme
        val extendidos = DonaVidaTheme.extendedColors
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            StatusBadge(
                text = "Abierto hoy hasta las 21:00",
                containerColor = extendidos.tertiaryFixed,
                contentColor = extendidos.onTertiaryFixed,
                dotColor = colores.tertiary,
            )
            StatusBadge(
                text = "Atención continua",
                containerColor = extendidos.tertiaryFixed,
                contentColor = extendidos.onTertiaryFixed,
            )
        }
    }
}
