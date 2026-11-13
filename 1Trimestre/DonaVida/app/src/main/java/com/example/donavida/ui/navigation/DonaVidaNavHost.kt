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
import com.example.donavida.ui.screens.mitos.PantallaMitos
import com.example.donavida.ui.screens.privacidad.PantallaPrivacidad
import com.example.donavida.ui.screens.sabermas.PantallaPreguntas
import com.example.donavida.ui.screens.sabermas.PantallaProblemas
import com.example.donavida.ui.screens.sabermas.PantallaSaberMas

/**
 * El "mapa" de la app: asocia cada ruta con su pantalla. Las pantallas no reciben el
 * NavController, solo funciones como onAbrirX; así no dependen de cómo se navega.
 *
 * [contentPadding] es el hueco que ocupan las barras del AppScaffold. Inicio lo usa para
 * que su lista pase por debajo de las barras; las pantallas provisionales simplemente se
 * colocan dentro de él.
 *
 * Mi donación ya no tiene ruta: los clientes no la piden. Sus archivos se conservan.
 */
@Composable
fun DonaVidaNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val volver: () -> Unit = { navController.popBackStack() }
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
            )
        }
        composable(Screen.Centros.route) {
            PantallaCentros(
                contentPadding = contentPadding,
                onVerDetalles = { id -> navController.navigate(Screen.FichaCentro.crearRuta(id)) },
                // "¿Tienes dudas?" cambia a la pestaña Saber más y abre Preguntas frecuentes;
                // al volver se queda en Saber más.
                onDudas = {
                    navController.navegarAPestana(Pestana.SABER_MAS)
                    navController.navigate(Screen.Preguntas.route)
                },
            )
        }
        composable(Screen.ComoFunciona.route) {
            PantallaComoFunciona(
                onBuscarCentros = { navController.navegarAPestana(Pestana.CENTROS) },
                modifier = dentroDelMargen,
            )
        }
        composable(Screen.SaberMas.route) {
            PantallaSaberMas(
                onProblemas = { navController.navigate(Screen.Problemas.route) },
                onCasos = { navController.navigate(Screen.Casos.route) },
                onPreguntas = { navController.navigate(Screen.Preguntas.route) },
                onMitos = { navController.navigate(Screen.Mitos.route) },
                onPrivacidad = { navController.navigate(Screen.Privacidad.route) },
                modifier = dentroDelMargen,
            )
        }

        // --- Pantallas secundarias ---
        composable(
            route = Screen.FichaCentro.route,
            arguments = listOf(
                navArgument(Screen.FichaCentro.ARG_CENTRO_ID) { type = NavType.StringType },
            ),
        ) {
            // La ficha tiene su propio Scaffold (barra superior y sin barra inferior), así
            // que gestiona ella misma los márgenes del sistema. El id lo lee su ViewModel.
            CenterDetailRoute(onBack = volver)
        }
        composable(Screen.Problemas.route) { PantallaProblemas(onVolver = volver, modifier = dentroDelMargen) }
        composable(Screen.Casos.route) { PantallaHistorias(onVolver = volver, modifier = dentroDelMargen) }
        composable(Screen.Preguntas.route) { PantallaPreguntas(onVolver = volver, modifier = dentroDelMargen) }
        composable(Screen.Mitos.route) { PantallaMitos(onVolver = volver, modifier = dentroDelMargen) }
        composable(Screen.Privacidad.route) { PantallaPrivacidad(onVolver = volver, modifier = dentroDelMargen) }
    }
}
