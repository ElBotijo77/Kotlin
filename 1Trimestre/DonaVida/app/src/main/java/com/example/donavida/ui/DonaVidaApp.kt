package com.example.donavida.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.donavida.ui.navigation.DonaVidaNavHost
import com.example.donavida.ui.navigation.Pestana
import com.example.donavida.ui.navigation.navegarAPestana

/** Raíz de la interfaz: el armazón común (AppScaffold) con el NavHost dentro. */
@Composable
fun DonaVidaApp() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()

    AppScaffold(
        rutaActual = entradaActual?.destination?.route,
        onPestanaPulsada = { pestana -> navController.navegarAPestana(pestana) },
        // Aún no hay pantalla de notificaciones.
        onNotificaciones = {},
        // Aún no hay pantalla de perfil: de momento lleva a Mi donación.
        onPerfil = { navController.navegarAPestana(Pestana.MI_DONACION) },
    ) { margenInterior ->
        DonaVidaNavHost(
            navController = navController,
            contentPadding = margenInterior,
        )
    }
}
