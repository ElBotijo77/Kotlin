package com.example.donavida.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.theme.DonaVidaTheme

/**
 * Pantalla provisional con el aviso "en construcción" y el [contenido] extra que se quiera.
 * Las pestañas ya tienen la barra superior común (AppScaffold), así que esta pantalla solo
 * dibuja la barra de pantalla secundaria (SecondaryTopBar) cuando hay [onVolver].
 */
@Composable
fun PantallaMarcador(
    titulo: String,
    modifier: Modifier = Modifier,
    onVolver: (() -> Unit)? = null,

    contenido: @Composable ColumnScope.() -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (onVolver != null) {
            SecondaryTopBar(title = titulo, onBack = onVolver)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = stringResource(R.string.marcador_en_construccion),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            contenido()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PantallaMarcadorPreview() {
    DonaVidaTheme {
        PantallaMarcador(titulo = stringResource(R.string.titulo_historias), onVolver = {})
    }
}
