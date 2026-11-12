package com.example.donavida.data.local

import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

/**
 * Lee un fichero JSON y lo convierte en objetos de Kotlin.
 *
 * No depende directamente de Android: recibe una función [abrir] que, dada una ruta,
 * devuelve el contenido. En la app esa función lee de assets ([desdeAssets]); en los
 * tests lee del disco. Así podemos probar los JSON reales sin emulador.
 */
class LectorJson(private val abrir: (ruta: String) -> InputStream) {

    /**
     * Lee [ruta] y la convierte en el tipo T. Se ejecuta en Dispatchers.IO (hilos pensados
     * para leer ficheros o red) para no bloquear el hilo principal, el que pinta la pantalla.
     */
    suspend inline fun <reified T> leer(ruta: String): T = withContext(Dispatchers.IO) {
        val texto = abrirTexto(ruta)
        formato.decodeFromString<T>(texto)
    }

    @PublishedApi
    internal fun abrirTexto(ruta: String): String =
        abrir(ruta).bufferedReader(Charsets.UTF_8).use { it.readText() }

    companion object {
        /** ignoreUnknownKeys: si una futura API añade campos, la app no falla por ello. */
        @PublishedApi
        internal val formato = Json { ignoreUnknownKeys = true }

        fun desdeAssets(assets: AssetManager) = LectorJson { ruta -> assets.open(ruta) }
    }
}

/** Rutas de los ficheros dentro de app/src/main/assets. */
object FicherosDatos {
    const val CENTROS = "data/centros.json"
    const val HISTORIAS = "data/historias.json"
    const val MITOS = "data/mitos.json"
    const val GLOSARIO = "data/glosario.json"
    const val MI_DONACION = "data/mi_donacion.json"
    const val PERFIL = "data/perfil.json"
}
