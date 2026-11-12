package com.example.donavida.util

import com.example.donavida.data.model.FranjaHorario
import java.time.LocalDateTime
import java.time.LocalTime

/** Si un centro está abierto en un momento dado, y hasta cuándo o desde cuándo. */
sealed interface EstadoApertura {
    data class Abierto(val cierre: LocalTime) : EstadoApertura

    /**
     * @param proximaApertura null si el centro no tiene ninguna franja de apertura.
     * @param diasHastaApertura 0 = hoy, 1 = mañana, etc. Sirve para decir "abre mañana".
     */
    data class Cerrado(
        val proximaApertura: LocalDateTime?,
        val diasHastaApertura: Int = 0,
    ) : EstadoApertura
}

/**
 * Calcula si el centro está abierto en [ahora]. Función pura (no lee el reloj) para poder
 * probarla con cualquier fecha.
 *
 * - Una franja está abierta desde la apertura (incluida) hasta el cierre (excluido).
 * - Los festivos no se distinguen: el calendario de festivos cambia por comunidad y año,
 *   así que la franja de "domingos y festivos" solo se aplica a los domingos.
 * - Se asume que ninguna franja pasa de medianoche.
 */
fun calcularEstadoApertura(horarios: List<FranjaHorario>, ahora: LocalDateTime): EstadoApertura {
    val hora = ahora.toLocalTime()
    val franjaActual = horarios.firstOrNull { franja ->
        ahora.dayOfWeek in franja.diasSemana && hora >= franja.apertura && hora < franja.cierre
    }
    if (franjaActual != null) return EstadoApertura.Abierto(franjaActual.cierre)

    // Busca la primera apertura posterior a "ahora", de hoy a dentro de una semana.
    for (dias in 0..7) {
        val fecha = ahora.toLocalDate().plusDays(dias.toLong())
        val apertura = horarios
            .filter { fecha.dayOfWeek in it.diasSemana }
            .map { fecha.atTime(it.apertura) }
            .filter { it.isAfter(ahora) }
            .minOrNull()
        if (apertura != null) return EstadoApertura.Cerrado(apertura, dias)
    }
    return EstadoApertura.Cerrado(proximaApertura = null)
}
