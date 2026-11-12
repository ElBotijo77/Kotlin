package com.example.donavida.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DistanciaTest {

    @Test
    fun mismoPunto_esCero() {
        assertEquals(0.0, distanciaKm(40.4168, -3.7038, 40.4168, -3.7038), 0.0001)
    }

    @Test
    fun madridBarcelona_esUnos505Km() {
        // Distancia en línea recta conocida entre ambos centros urbanos: ~505 km.
        assertEquals(505.0, distanciaKm(40.4168, -3.7038, 41.3874, 2.1686), 5.0)
    }

    @Test
    fun madridGranada_esUnos360Km() {
        assertEquals(360.0, distanciaKm(40.4168, -3.7038, 37.1773, -3.5986), 5.0)
    }

    @Test
    fun formatear_unKmOMas_usaUnDecimalConComa() {
        assertEquals("A 2,4 km", formatearDistancia(2.43))
        assertEquals("A 1,0 km", formatearDistancia(1.0))
        assertEquals("A 360,2 km", formatearDistancia(360.2))
    }

    @Test
    fun formatear_menosDeUnKm_usaMetrosRedondeadosADecenas() {
        assertEquals("A 850 m", formatearDistancia(0.852))
        assertEquals("A 0 m", formatearDistancia(0.0))
    }

    @Test
    fun formatear_casiUnKm_pasaAKilometros() {
        // 999 m se redondea a 1000 m: mejor "1,0 km" que "1000 m".
        assertEquals("A 1,0 km", formatearDistancia(0.999))
    }
}
