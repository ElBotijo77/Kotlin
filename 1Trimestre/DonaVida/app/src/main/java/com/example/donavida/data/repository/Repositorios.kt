package com.example.donavida.data.repository

import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.Historia
import com.example.donavida.data.model.MiDonacion
import com.example.donavida.data.model.Mito
import com.example.donavida.data.model.Perfil
import com.example.donavida.data.model.TerminoGlosario

// Contratos de acceso a datos. Los ViewModel solo conocerán estas interfaces, nunca de dónde
// salen los datos: hoy, JSON en assets; mañana, una API. Las funciones son "suspend" porque
// leer un fichero o llamar a la red tarda y no debe bloquear la pantalla.

interface RepositorioCentros {
    suspend fun obtenerCentros(): List<Centro>
    /** null si no existe ningún centro con ese id. */
    suspend fun obtenerCentro(id: String): Centro?
}

interface RepositorioHistorias {
    suspend fun obtenerHistorias(): List<Historia>
    suspend fun obtenerHistoria(id: String): Historia?
}

interface RepositorioMitos {
    suspend fun obtenerMitos(): List<Mito>
}

interface RepositorioGlosario {
    suspend fun obtenerTerminos(): List<TerminoGlosario>
}

interface RepositorioMiDonacion {
    suspend fun obtenerMiDonacion(): MiDonacion
}

interface RepositorioPerfil {
    suspend fun obtenerPerfil(): Perfil
}
