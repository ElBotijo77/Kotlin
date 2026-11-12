package com.example.donavida.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.ui.theme.DonaVidaTheme

/**
 * Barra inferior con las cuatro pestañas. No sabe nada del NavController: recibe la ruta
 * actual y avisa con [onPestanaPulsada]. Así es fácil de probar y de previsualizar.
 */
@Composable
fun BarraInferior(
    rutaActual: String?,
    onPestanaPulsada: (Pestana) -> Unit,
) {
    val colores = MaterialTheme.colorScheme
    NavigationBar(
        // Casi opaca: sin el desenfoque del diseño, con 85 % el texto de debajo estorbaba.
        containerColor = colores.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
    ) {
        Pestana.entries.forEach { pestana ->
            NavigationBarItem(
                selected = pestana.contiene(rutaActual),
                onClick = { onPestanaPulsada(pestana) },
                // contentDescription = null porque la etiqueta ya lo dice; si no,
                // TalkBack leería el nombre dos veces.
                icon = { Icon(painterResource(pestana.icono), contentDescription = null) },
                label = {
                    Text(stringResource(pestana.etiqueta), style = MaterialTheme.typography.labelSmall)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colores.primary,
                    selectedTextColor = colores.primary,
                    indicatorColor = colores.surfaceContainerHigh,
                    unselectedIconColor = colores.onSurfaceVariant,
                    unselectedTextColor = colores.onSurfaceVariant,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun BarraInferiorPreview() {
    DonaVidaTheme {
        BarraInferior(rutaActual = Screen.Inicio.route, onPestanaPulsada = {})
    }
}
