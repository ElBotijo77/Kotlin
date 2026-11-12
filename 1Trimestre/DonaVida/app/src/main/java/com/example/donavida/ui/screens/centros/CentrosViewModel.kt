package com.example.donavida.ui.screens.centros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.donavida.DonaVidaApplication
import com.example.donavida.data.model.Centro
import com.example.donavida.data.model.FranjaHorario
import com.example.donavida.data.model.ServicioCentro
import com.example.donavida.data.model.Ubicacion
import com.example.donavida.data.repository.RepositorioCentros
import com.example.donavida.data.repository.RepositorioUbicacion
import com.example.donavida.util.EstadoApertura
import com.example.donavida.util.calcularEstadoApertura
import com.example.donavida.util.distanciaKm
import com.example.donavida.util.filtrarCentros
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDateTime

private const val LimiteBusquedaUbicacionMs = 20_000L

enum class VistaCentros { LISTA, MAPA }

enum class EstadoUbicacion { INACTIVA, BUSCANDO, DETECTADA, DENEGADA, ERROR }

/** Un centro de la lista con lo que se calcula al momento: apertura y distancia. */
data class CentroConDistancia(
    val centro: Centro,
    /** null si el centro no tiene horario detallado. */
    val estadoApertura: EstadoApertura?,
    /** La franja en la que está abierto ahora, para decir "Abierto hoy de 8:30 a 21:00". */
    val franjaActual: FranjaHorario?,
    /** null si no se conoce la ubicación de la persona. */
    val distanciaKm: Double?,
)

data class CentrosUiState(
    val cargando: Boolean = true,
    /** Lo que hay escrito en el buscador (se actualiza al instante). */
    val consulta: String = "",
    /** null = sin filtro de especialidad. */
    val servicioSeleccionado: ServicioCentro? = ServicioCentro.INSCRIPCION_MEDULA,
    val vista: VistaCentros = VistaCentros.LISTA,
    val estadoUbicacion: EstadoUbicacion = EstadoUbicacion.INACTIVA,
    /** Ya filtrados; por distancia si hay ubicación y, si no, por nombre. */
    val centros: List<CentroConDistancia> = emptyList(),
    val hayUbicacion: Boolean = false,
)

/**
 * Estado de la lista de centros. La búsqueda se aplica 300 ms después de dejar de escribir
 * (debounce) para no filtrar con cada letra.
 *
 * El permiso de ubicación lo pide la pantalla (necesita una Activity); el ViewModel solo
 * recibe el resultado con [alConcederPermiso] o [alDenegarPermiso].
 */
@OptIn(FlowPreview::class)
class CentrosViewModel(
    private val repositorioCentros: RepositorioCentros,
    private val repositorioUbicacion: RepositorioUbicacion,
    private val ahora: () -> LocalDateTime = LocalDateTime::now,
) : ViewModel() {

    /** Lo que controla la persona desde la pantalla. */
    private data class Controles(
        val consulta: String = "",
        val servicio: ServicioCentro? = ServicioCentro.INSCRIPCION_MEDULA,
        val vista: VistaCentros = VistaCentros.LISTA,
        val estadoUbicacion: EstadoUbicacion = EstadoUbicacion.INACTIVA,
    )

    private val controles = MutableStateFlow(
        Controles(
            // Si la ubicación ya se obtuvo antes (sigue en memoria), se muestra como detectada.
            estadoUbicacion = if (repositorioUbicacion.ubicacion.value != null) {
                EstadoUbicacion.DETECTADA
            } else {
                EstadoUbicacion.INACTIVA
            },
        ),
    )

    /** La consulta que realmente se aplica, con el retraso del debounce. */
    private val consultaAplicada = MutableStateFlow("")

    /** null mientras se cargan. */
    private val todosLosCentros = MutableStateFlow<List<Centro>?>(null)

    val uiState: StateFlow<CentrosUiState> = combine(
        controles,
        consultaAplicada,
        todosLosCentros,
        repositorioUbicacion.ubicacion,
    ) { c, consulta, centros, ubicacion ->
        CentrosUiState(
            cargando = centros == null,
            consulta = c.consulta,
            servicioSeleccionado = c.servicio,
            vista = c.vista,
            estadoUbicacion = c.estadoUbicacion,
            centros = centros.orEmpty().let { prepararLista(it, consulta, c.servicio, ubicacion) },
            hayUbicacion = ubicacion != null,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CentrosUiState())

    init {
        viewModelScope.launch {
            todosLosCentros.value = runCatching { repositorioCentros.obtenerCentros() }.getOrDefault(emptyList())
        }
        viewModelScope.launch {
            controles.map { it.consulta }
                .distinctUntilChanged()
                .debounce(300)
                .collect { consultaAplicada.value = it }
        }
    }

    private fun prepararLista(
        centros: List<Centro>,
        consulta: String,
        servicio: ServicioCentro?,
        ubicacion: Ubicacion?,
    ): List<CentroConDistancia> {
        val momento = ahora()
        val lista = filtrarCentros(centros, consulta, servicio).map { centro ->
            val estado = centro.horarios.takeIf { it.isNotEmpty() }?.let { calcularEstadoApertura(it, momento) }
            CentroConDistancia(
                centro = centro,
                estadoApertura = estado,
                franjaActual = if (estado is EstadoApertura.Abierto) franjaAbierta(centro, momento) else null,
                distanciaKm = ubicacion?.let {
                    distanciaKm(it.latitud, it.longitud, centro.latitud, centro.longitud)
                },
            )
        }
        return if (ubicacion != null) {
            lista.sortedBy { it.distanciaKm }
        } else {
            lista.sortedBy { it.centro.nombre }
        }
    }

    private fun franjaAbierta(centro: Centro, momento: LocalDateTime): FranjaHorario? {
        val hora = momento.toLocalTime()
        return centro.horarios.firstOrNull {
            momento.dayOfWeek in it.diasSemana && hora >= it.apertura && hora < it.cierre
        }
    }

    fun alCambiarConsulta(consulta: String) = controles.update { it.copy(consulta = consulta) }

    /** Pulsar el chip activo lo deselecciona y se muestran todos los centros. */
    fun alPulsarServicio(servicio: ServicioCentro) = controles.update {
        it.copy(servicio = if (it.servicio == servicio) null else servicio)
    }

    fun alCambiarVista(vista: VistaCentros) = controles.update { it.copy(vista = vista) }

    /** Vacía el buscador (sin esperar al debounce) y quita el filtro de especialidad. */
    fun alBorrarBusqueda() {
        controles.update { it.copy(consulta = "", servicio = null) }
        consultaAplicada.value = ""
    }

    fun alConcederPermiso() {
        controles.update { it.copy(estadoUbicacion = EstadoUbicacion.BUSCANDO) }
        viewModelScope.launch {
            // Sin límite, si el móvil no consigue posición (interior, GPS apagado…) el botón
            // se quedaría en "Buscando" para siempre.
            val ubicacion = runCatching {
                withTimeoutOrNull(LimiteBusquedaUbicacionMs) { repositorioUbicacion.obtenerUbicacion() }
            }.getOrNull()
            controles.update {
                it.copy(estadoUbicacion = if (ubicacion != null) EstadoUbicacion.DETECTADA else EstadoUbicacion.ERROR)
            }
        }
    }

    /**
     * @param permanente true si la persona marcó "no volver a preguntar": entonces solo se
     *   puede conceder desde los ajustes. Si no, el botón vuelve a pedir el permiso.
     */
    fun alDenegarPermiso(permanente: Boolean) = controles.update {
        it.copy(estadoUbicacion = if (permanente) EstadoUbicacion.DENEGADA else EstadoUbicacion.INACTIVA)
    }

    /** La persona ha concedido el permiso desde los ajustes y ha vuelto a la app. */
    fun alRecuperarPermiso() = alConcederPermiso()

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DonaVidaApplication
                CentrosViewModel(
                    repositorioCentros = app.contenedor.repositorioCentros,
                    repositorioUbicacion = app.contenedor.repositorioUbicacion,
                )
            }
        }
    }
}
