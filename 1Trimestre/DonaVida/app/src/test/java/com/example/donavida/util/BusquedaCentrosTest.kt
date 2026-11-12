package com.example.donavida.util

import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.ServicioCentro
import com.example.donavida.data.model.TipoCentro
import org.junit.Assert.assertEquals
import org.junit.Test

class BusquedaCentrosTest {

    private fun centro(
        id: String,
        nombre: String,
        ciudad: String,
        provincia: String,
        codigoPostal: String,
        servicios: Set<ServicioCentro>,
    ) = Centro(
        id = id,
        nombre = nombre,
        tipo = TipoCentro.CENTRO_TRANSFUSION,
        comunidad = "",
        direccion = "",
        ciudad = ciudad,
        provincia = provincia,
        codigoPostal = codigoPostal,
        servicios = servicios,
        latitud = 0.0,
        longitud = 0.0,
        telefono = "",
        horario = "",
        requiereCita = false,
        urlFuente = "https://ejemplo.es",
    )

    private val madrid = centro("madrid", "Centro de Transfusión de Madrid", "Madrid", "Madrid", "28032",
        setOf(ServicioCentro.INSCRIPCION_MEDULA, ServicioCentro.SANGRE_CORDON))
    private val laPaz = centro("la-paz", "Hospital La Paz", "Madrid", "Madrid", "28046",
        setOf(ServicioCentro.INSCRIPCION_MEDULA))
    private val malaga = centro("malaga", "Hospital Regional", "Málaga", "Málaga", "29010",
        setOf(ServicioCentro.PUNTOS_TEMPORALES))
    private val alcala = centro("alcala", "Hospital Príncipe de Asturias", "Alcalá de Henares", "Madrid", "28805",
        setOf(ServicioCentro.SANGRE_CORDON))
    private val todos = listOf(madrid, laPaz, malaga, alcala)

    private fun ids(consulta: String, servicio: ServicioCentro? = null) =
        filtrarCentros(todos, consulta, servicio).map { it.id }

    @Test
    fun consultaVacia_noFiltra() {
        assertEquals(listOf("madrid", "la-paz", "malaga", "alcala"), ids(""))
        assertEquals(listOf("madrid", "la-paz", "malaga", "alcala"), ids("   "))
    }

    @Test
    fun codigoPostal_comparaLaProvincia() {
        assertEquals(listOf("madrid", "la-paz", "alcala"), ids("28006"))
        assertEquals(listOf("malaga"), ids("29"))
    }

    @Test
    fun unaSolaCifra_noSeTrataComoCodigoPostal() {
        assertEquals(emptyList<String>(), ids("2"))
    }

    @Test
    fun texto_ignoraTildesYMayusculas() {
        assertEquals(listOf("malaga"), ids("malaga"))
        assertEquals(listOf("malaga"), ids("MÁLAGA"))
        assertEquals(listOf("alcala"), ids("alcala de"))
    }

    @Test
    fun texto_buscaEnCiudadProvinciaYNombre() {
        // "madrid" aparece en la ciudad de dos, en la provincia de Alcalá y en un nombre.
        assertEquals(listOf("madrid", "la-paz", "alcala"), ids("madrid"))
        assertEquals(listOf("la-paz"), ids("la paz"))
    }

    @Test
    fun sinCoincidencias_devuelveListaVacia() {
        assertEquals(emptyList<String>(), ids("zaragoza"))
    }

    @Test
    fun servicio_dejaSoloLosCentrosQueLoOfrecen() {
        assertEquals(listOf("madrid", "alcala"), ids("", ServicioCentro.SANGRE_CORDON))
    }

    @Test
    fun servicioYConsulta_seCombinan() {
        assertEquals(listOf("madrid", "la-paz"), ids("28", ServicioCentro.INSCRIPCION_MEDULA))
    }
}
