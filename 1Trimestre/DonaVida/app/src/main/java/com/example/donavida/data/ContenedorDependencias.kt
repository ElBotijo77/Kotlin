package com.example.donavida.data

import android.content.Context
import com.example.donavida.data.local.LectorJson
import com.example.donavida.data.repository.RepositorioCentros
import com.example.donavida.data.repository.RepositorioCentrosLocal
import com.example.donavida.data.repository.RepositorioGlosario
import com.example.donavida.data.repository.RepositorioGlosarioLocal
import com.example.donavida.data.repository.RepositorioHistorias
import com.example.donavida.data.repository.RepositorioHistoriasLocal
import com.example.donavida.data.repository.RepositorioMetodos
import com.example.donavida.data.repository.RepositorioMetodosLocal
import com.example.donavida.data.repository.RepositorioMiDonacion
import com.example.donavida.data.repository.RepositorioMiDonacionLocal
import com.example.donavida.data.repository.RepositorioMitos
import com.example.donavida.data.repository.RepositorioMitosLocal
import com.example.donavida.data.repository.RepositorioPerfil
import com.example.donavida.data.repository.RepositorioPerfilLocal
import com.example.donavida.data.repository.RepositorioProblemas
import com.example.donavida.data.repository.RepositorioProblemasLocal
import com.example.donavida.data.repository.RepositorioUbicacion
import com.example.donavida.data.repository.RepositorioUbicacionFused

/**
 * Inyección de dependencias "a mano": el único sitio que decide qué implementación de cada
 * repositorio usa la app. Hay una sola instancia, creada en DonaVidaApplication.
 * Para pasar a una API bastará con cambiar aquí RepositorioXLocal por RepositorioXRemoto.
 */
class ContenedorDependencias(private val context: Context) {
    private val lector = LectorJson.desdeAssets(context.applicationContext.assets)

    val repositorioCentros: RepositorioCentros by lazy { RepositorioCentrosLocal(lector) }
    val repositorioHistorias: RepositorioHistorias by lazy { RepositorioHistoriasLocal(lector) }
    val repositorioMitos: RepositorioMitos by lazy { RepositorioMitosLocal(lector) }
    val repositorioProblemas: RepositorioProblemas by lazy { RepositorioProblemasLocal(lector) }
    val repositorioMetodos: RepositorioMetodos by lazy { RepositorioMetodosLocal(lector) }
    val repositorioGlosario: RepositorioGlosario by lazy { RepositorioGlosarioLocal(lector) }
    val repositorioMiDonacion: RepositorioMiDonacion by lazy { RepositorioMiDonacionLocal(lector) }
    val repositorioPerfil: RepositorioPerfil by lazy { RepositorioPerfilLocal(lector) }

    /** Una sola instancia para toda la app: así la ficha ve la ubicación que obtuvo la lista. */
    val repositorioUbicacion: RepositorioUbicacion by lazy {
        RepositorioUbicacionFused(context.applicationContext)
    }
}
