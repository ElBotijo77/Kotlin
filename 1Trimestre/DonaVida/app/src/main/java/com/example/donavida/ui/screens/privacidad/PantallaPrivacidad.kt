package com.example.donavida.ui.screens.privacidad

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.donavida.R
import com.example.donavida.ui.components.PantallaMarcador

@Composable
fun PantallaPrivacidad(onVolver: () -> Unit, onPerfil: () -> Unit, modifier: Modifier = Modifier) {
    PantallaMarcador(titulo = stringResource(R.string.titulo_privacidad), onVolver = onVolver, onPerfil = onPerfil, modifier = modifier)
}
