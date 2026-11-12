package com.example.donavida.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.donavida.R

/**
 * Todos los destinos de la app. Una ruta es como una URL interna: el NavHost decide qué
 * pantalla mostrar según la ruta. Al ser una sealed class, el compilador conoce la lista
 * completa y no hay cadenas sueltas por el código.
 */
sealed class Screen(val route: String) {
    // --- Pestañas ---
    data object Inicio : Screen("inicio")
    data object Centros : Screen("centros")
    data object ComoFunciona : Screen("como_funciona")
    data object MiDonacion : Screen("mi_donacion")

    // --- Pantallas secundarias ---
    data object Historias : Screen("historias")
    data object Mitos : Screen("mitos")
    data object Privacidad : Screen("privacidad")

    /**
     * La ficha recibe el id del centro como argumento: "centros/hospital-la-paz".
     * Empieza por "centros/" para que la pestaña Centros siga marcada al abrirla.
     */
    data object FichaCentro : Screen("centros/{centroId}") {
        const val ARG_CENTRO_ID = "centroId"
        fun crearRuta(centroId: String) = "centros/$centroId"
    }
}

/** Las cuatro pestañas de la barra inferior, en el orden en que aparecen. */
enum class Pestana(
    val screen: Screen,
    @param:StringRes val etiqueta: Int,
    @param:DrawableRes val icono: Int,
) {
    INICIO(Screen.Inicio, R.string.pestana_inicio, R.drawable.ic_home),
    CENTROS(Screen.Centros, R.string.pestana_centros, R.drawable.ic_local_hospital),
    COMO_FUNCIONA(Screen.ComoFunciona, R.string.pestana_como_funciona, R.drawable.ic_auto_awesome),
    MI_DONACION(Screen.MiDonacion, R.string.pestana_mi_donacion, R.drawable.ic_volunteer_activism);

    /** true si [ruta] es esta pestaña o una pantalla que cuelga de ella ("centros/xyz"). */
    fun contiene(ruta: String?): Boolean =
        ruta == screen.route || ruta?.startsWith("${screen.route}/") == true

    companion object {
        /** La pestaña cuya pantalla principal es exactamente [ruta], o null. */
        fun deRuta(ruta: String?): Pestana? = entries.firstOrNull { it.screen.route == ruta }
    }
}

/**
 * Navega a una pestaña con el patrón recomendado: no se apilan pantallas al cambiar de
 * pestaña y cada una recuerda dónde estabas.
 */
fun NavController.navegarAPestana(pestana: Pestana) {
    navigate(pestana.screen.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
