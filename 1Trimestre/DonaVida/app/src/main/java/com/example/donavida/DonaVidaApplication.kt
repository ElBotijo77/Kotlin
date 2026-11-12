package com.example.donavida

import android.app.Application
import com.example.donavida.data.ContenedorDependencias

/**
 * Se crea antes que cualquier pantalla y vive mientras la app está abierta: buen sitio para
 * el contenedor de dependencias. Está registrada en AndroidManifest.xml (android:name).
 */
class DonaVidaApplication : Application() {
    lateinit var contenedor: ContenedorDependencias
        private set

    override fun onCreate() {
        super.onCreate()
        contenedor = ContenedorDependencias(this)
    }
}
