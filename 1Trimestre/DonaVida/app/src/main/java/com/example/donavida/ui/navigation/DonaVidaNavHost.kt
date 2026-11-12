package com.example.donavida.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.donavida.ui.screens.centros.PantallaCentros
import com.example.donavida.ui.screens.comofunciona.PantallaComoFunciona
import com.example.donavida.ui.screens.fichacentro.CenterDetailRoute
import com.example.donavida.ui.screens.historias.PantallaHistorias
import com.example.donavida.ui.screens.inicio.HomeRoute
import com.example.donavida.ui.screens.midonacion.PantallaMiDonacion
import com.example.donavida.ui.screens.mitos.PantallaMitos
import com.example.donavida.ui.screens.privacidad.PantallaPrivacidad

/**
 * El "mapa" de la app: asocia cada ruta con su pantalla. Las pantallas no reciben el
 * NavController, solo funciones como onAbrirX; así no dependen de cómo se navega.
 *
 * [contentPadding] es el hueco que ocupan las barras del AppScaffold. Inicio lo usa para
 * que su lista pase por debajo de las barras; las pantallas provisionales simplemente se
 * colocan dentro de él.
 */
@Composable
fun DonaVidaNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val volver: () -> Unit = { navController.popBackStack() }
    // Igual que el avatar de las pestañas (DonaVidaApp): aún no hay perfil, va a Mi donación.
    val abrirPerfil: () -> Unit = { navController.navegarAPestana(Pestana.MI_DONACION) }
    // consumeWindowInsets evita que la barra superior de las pantallas secundarias vuelva a
    // sumar el hueco de la barra de estado, que ya está incluido en este margen.
    val dentroDelMargen = Modifier
        .padding(contentPadding)
        .consumeWindowInsets(contentPadding)

    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route,
        modifier = modifier,
    ) {
        // --- Pestañas ---
        composable(Screen.Inicio.route) {
            HomeRoute(
                contentPadding = contentPadding,
                onFindCenter = { navController.navegarAPestana(Pestana.CENTROS) },
                onSeeProcess = { navController.navegarAPestana(Pestana.COMO_FUNCIONA) },
                onMyDonation = { navController.navegarAPestana(Pestana.MI_DONACION) },
            )
        }
        composable(Screen.Centros.route) {
            PantallaCentros(
                contentPadding = contentPadding,
                onVerDetalles = { id -> navController.navigate(Screen.FichaCentro.crearRuta(id)) },
                // "¿Tienes dudas?" lleva a Mitos y preguntas frecuentes.
                onDudas = { navController.navigate(Screen.Mitos.route) },
            )
        }
        composable(Screen.ComoFunciona.route) {
            PantallaComoFunciona(
                onBuscarCentros = { navController.navegarAPestana(Pestana.CENTROS) },
                modifier = dentroDelMargen,
            )
        }
        composable(Screen.MiDonacion.route) { PantallaMiDonacion(dentroDelMargen) }

        // --- Pantallas secundarias ---
        composable(
            route = Screen.FichaCentro.route,
            arguments = listOf(
                navArgument(Screen.FichaCentro.ARG_CENTRO_ID) { type = NavType.StringType },
            ),
        ) {
            // La ficha tiene su propio Scaffold (barra superior y sin barra inferior), así
            // que gestiona ella misma los márgenes del sistema. El id lo lee su ViewModel.
            CenterDetailRoute(onBack = volver, onProfileClick = abrirPerfil)
        }
        composable(Screen.Historias.route) { PantallaHistorias(onVolver = volver, onPerfil = abrirPerfil, modifier = dentroDelMargen) }
        composable(Screen.Mitos.route) { PantallaMitos(onVolver = volver, onPerfil = abrirPerfil, modifier = dentroDelMargen) }
        composable(Screen.Privacidad.route) { PantallaPrivacidad(onVolver = volver, onPerfil = abrirPerfil, modifier = dentroDelMargen) }
    }
}
