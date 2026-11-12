package com.example.donavida.data.repository

import com.example.donavida.data.local.FicherosDatos
import com.example.donavida.data.local.LectorJson
import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.Historia
import com.example.donavida.data.model.MiDonacion
import com.example.donavida.data.model.Mito
import com.example.donavida.data.model.Perfil
import com.example.donavida.data.model.TerminoGlosario

// Implementaciones que leen los JSON de ejemplo. Cuando haya API, se crearán otras clases
// (por ejemplo RepositorioCentrosRemoto) con las mismas interfaces y solo cambiará
// ContenedorDependencias.

class RepositorioCentrosLocal(private val lector: LectorJson) : RepositorioCentros {
    override suspend fun obtenerCentros(): List<Centro> =
        lector.leer<List<Centro>>(FicherosDatos.CENTROS).sortedBy { it.nombre }

    override suspend fun obtenerCentro(id: String): Centro? =
        obtenerCentros().firstOrNull { it.id == id }
}

class RepositorioHistoriasLocal(private val lector: LectorJson) : RepositorioHistorias {
    override suspend fun obtenerHistorias(): List<Historia> =
        lector.leer(FicherosDatos.HISTORIAS)

    override suspend fun obtenerHistoria(id: String): Historia? =
        obtenerHistorias().firstOrNull { it.id == id }
}

class RepositorioMitosLocal(private val lector: LectorJson) : RepositorioMitos {
    override suspend fun obtenerMitos(): List<Mito> = lector.leer(FicherosDatos.MITOS)
}

class RepositorioGlosarioLocal(private val lector: LectorJson) : RepositorioGlosario {
    override suspend fun obtenerTerminos(): List<TerminoGlosario> =
        lector.leer<List<TerminoGlosario>>(FicherosDatos.GLOSARIO).sortedBy { it.termino }
}

class RepositorioMiDonacionLocal(private val lector: LectorJson) : RepositorioMiDonacion {
    override suspend fun obtenerMiDonacion(): MiDonacion = lector.leer(FicherosDatos.MI_DONACION)
}

class RepositorioPerfilLocal(private val lector: LectorJson) : RepositorioPerfil {
    override suspend fun obtenerPerfil(): Perfil = lector.leer(FicherosDatos.PERFIL)
}
