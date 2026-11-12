package com.example.donavida.util

import com.example.donavida.data.model.FranjaHorario
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

/** Horario del centro de Madrid: L-S 08:30-21:00; domingos y festivos 09:00-20:00. */
class EstadoAperturaTest {

    private val horarios = listOf(
        FranjaHorario(
            dias = "Lunes a sábado",
            apertura = LocalTime.of(8, 30),
            cierre = LocalTime.of(21, 0),
            diasSemana = setOf(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY,
            ),
        ),
        FranjaHorario(
            dias = "Domingos y festivos",
            apertura = LocalTime.of(9, 0),
            cierre = LocalTime.of(20, 0),
            diasSemana = setOf(DayOfWeek.SUNDAY),
        ),
    )

    // 11/11/2026 es miércoles; 15/11/2026, domingo.

    @Test
    fun entreSemana_aMediaManana_estaAbiertoHastaLas21() {
        val estado = calcularEstadoApertura(horarios, LocalDateTime.of(2026, 11, 11, 10, 0))
        assertEquals(EstadoApertura.Abierto(LocalTime.of(21, 0)), estado)
    }

    @Test
    fun deMadrugada_estaCerradoYAbreHoyALas0830() {
        val estado = calcularEstadoApertura(horarios, LocalDateTime.of(2026, 11, 11, 3, 0))
        assertEquals(EstadoApertura.Cerrado(LocalDateTime.of(2026, 11, 11, 8, 30), 0), estado)
    }

    @Test
    fun domingo_usaElHorarioDeDomingos() {
        val abierto = calcularEstadoApertura(horarios, LocalDateTime.of(2026, 11, 15, 19, 30))
        assertEquals(EstadoApertura.Abierto(LocalTime.of(20, 0)), abierto)

        // A las 20:30 del domingo ya ha cerrado: abre el lunes (mañana) a las 08:30.
        val cerrado = calcularEstadoApertura(horarios, LocalDateTime.of(2026, 11, 15, 20, 30))
        assertEquals(EstadoApertura.Cerrado(LocalDateTime.of(2026, 11, 16, 8, 30), 1), cerrado)
    }

    @Test
    fun sabadoPorLaNoche_abreElDomingoALas9() {
        val estado = calcularEstadoApertura(horarios, LocalDateTime.of(2026, 11, 14, 22, 0))
        assertEquals(EstadoApertura.Cerrado(LocalDateTime.of(2026, 11, 15, 9, 0), 1), estado)
    }

    @Test
    fun justoALaHoraDeCierre_estaCerrado() {
        val estado = calcularEstadoApertura(horarios, LocalDateTime.of(2026, 11, 11, 21, 0))
        assertEquals(EstadoApertura.Cerrado(LocalDateTime.of(2026, 11, 12, 8, 30), 1), estado)
    }

    @Test
    fun sinHorarios_estaCerradoSinProximaApertura() {
        val estado = calcularEstadoApertura(emptyList(), LocalDateTime.of(2026, 11, 11, 10, 0))
        assertEquals(EstadoApertura.Cerrado(proximaApertura = null), estado)
    }
}
