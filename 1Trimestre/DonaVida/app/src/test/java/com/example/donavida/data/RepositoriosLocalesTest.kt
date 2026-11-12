package com.example.donavida.data

import com.example.donavida.data.local.LectorJson
import com.example.donavida.data.model.EstadoEtapa
import com.example.donavida.data.repository.RepositorioCentrosLocal
import com.example.donavida.data.repository.RepositorioGlosarioLocal
import com.example.donavida.data.repository.RepositorioHistoriasLocal
import com.example.donavida.data.repository.RepositorioMiDonacionLocal
import com.example.donavida.data.repository.RepositorioMitosLocal
import com.example.donavida.data.repository.RepositorioPerfilLocal
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Prueba los repositorios con los JSON reales de assets, leídos desde el disco del PC.
 * Si alguien rompe un JSON (una coma de más, un campo que falta), estos tests fallan.
 */
class RepositoriosLocalesTest {

    // Los tests de Gradle se ejecutan desde la carpeta del módulo (app/).
    private val lector = LectorJson { ruta -> File("src/main/assets/$ruta").inputStream() }

    @Test
    fun centros_seLeenConIdsUnicosYCoordenadasEnEspana() = runBlocking {
        val centros = RepositorioCentrosLocal(lector).obtenerCentros()

        assertTrue(centros.isNotEmpty())
        assertEquals(centros.size, centros.map { it.id }.toSet().size)
        centros.forEach { c ->
            // Rectángulo que cubre la Península, Baleares y Canarias.
            assertTrue("${c.id}: latitud fuera de España", c.latitud in 27.0..44.0)
            assertTrue("${c.id}: longitud fuera de España", c.longitud in -18.5..4.5)
            assertTrue("${c.id}: urlFuente debe ser https", c.urlFuente.startsWith("https://"))
        }
    }

    @Test
    fun obtenerCentro_devuelveElCentroONull() = runBlocking {
        val repo = RepositorioCentrosLocal(lector)
        assertNotNull(repo.obtenerCentro("ejemplo-madrid"))
        assertNull(repo.obtenerCentro("no-existe"))
    }

    @Test
    fun historias_mitosYGlosario_seLeenConIdsUnicos() = runBlocking {
        val historias = RepositorioHistoriasLocal(lector).obtenerHistorias()
        val mitos = RepositorioMitosLocal(lector).obtenerMitos()
        val glosario = RepositorioGlosarioLocal(lector).obtenerTerminos()

        listOf(historias.map { it.id }, mitos.map { it.id }, glosario.map { it.id }).forEach { ids ->
            assertTrue(ids.isNotEmpty())
            assertEquals(ids.size, ids.toSet().size)
        }
        assertTrue("Debe haber mitos", mitos.any { it.esMito })
        assertTrue("Debe haber preguntas frecuentes", mitos.any { !it.esMito })
    }

    @Test
    fun miDonacion_esGenericaYSinFechas() = runBlocking {
        val miDonacion = RepositorioMiDonacionLocal(lector).obtenerMiDonacion()

        assertTrue(miDonacion.esEjemplo)
        assertEquals(1, miDonacion.etapas.count { it.estado == EstadoEtapa.EN_CURSO })
        // Privacidad: ningún número en los textos (ni fechas, ni edades, ni códigos postales).
        miDonacion.etapas.forEach { etapa ->
            assertFalse(etapa.id, (etapa.titulo + etapa.descripcion).any { it.isDigit() })
        }
    }

    @Test
    fun perfil_esDeEjemploYTieneNombre() = runBlocking {
        val perfil = RepositorioPerfilLocal(lector).obtenerPerfil()

        assertTrue(perfil.esEjemplo)
        assertTrue(perfil.nombre.isNotBlank())
    }

    @Test
    fun centroDeMadrid_traeLosDatosDeLaFicha() = runBlocking {
        val madrid = RepositorioCentrosLocal(lector).obtenerCentro("ejemplo-madrid")

        assertNotNull(madrid)
        assertEquals(2, madrid!!.horarios.size)
        assertEquals(java.time.LocalDate.of(2026, 11, 3), madrid.fechaVerificacion)
        madrid.horarios.forEach { franja ->
            assertTrue(franja.dias, franja.apertura < franja.cierre)
            assertTrue(franja.dias, franja.diasSemana.isNotEmpty())
        }
    }

    @Test
    fun centros_tienenDatosParaBuscarYFiltrar() = runBlocking {
        RepositorioCentrosLocal(lector).obtenerCentros().forEach { c ->
            assertTrue("${c.id}: código postal de 5 cifras", c.codigoPostal.length == 5 && c.codigoPostal.all { it.isDigit() })
            assertTrue("${c.id}: falta la ciudad", c.ciudad.isNotBlank())
            assertTrue("${c.id}: falta la provincia", c.provincia.isNotBlank())
            assertTrue("${c.id}: sin servicios", c.servicios.isNotEmpty())
        }
    }
}
