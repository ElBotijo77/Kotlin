package com.example.donavida.ui.screens.fichacentro

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.donavida.DonaVidaApplication
import com.example.donavida.data.model.Centro
import com.example.donavida.data.repository.RepositorioCentros
import com.example.donavida.data.repository.RepositorioUbicacion
import com.example.donavida.ui.navigation.Screen
import com.example.donavida.util.EstadoApertura
import com.example.donavida.util.calcularEstadoApertura
import com.example.donavida.util.distanciaKm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

sealed interface CenterDetailUiState {
    data object Loading : CenterDetailUiState

    /**
     * @param estadoApertura null si el centro no tiene horario detallado.
     * @param distanciaKm null si la persona no ha compartido su ubicación.
     */
    data class Success(
        val centro: Centro,
        val estadoApertura: EstadoApertura?,
        val distanciaKm: Double?,
    ) : CenterDetailUiState

    data object NotFound : CenterDetailUiState
}

/**
 * Carga la ficha del centro cuyo id llega en la ruta ("centros/{centroId}"). Navigation
 * Compose deja los argumentos de la ruta en el [SavedStateHandle].
 *
 * @param ahora reloj inyectable, para poder probar el estado de apertura a cualquier hora.
 */
class CenterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorioCentros: RepositorioCentros,
    private val repositorioUbicacion: RepositorioUbicacion,
    private val ahora: () -> LocalDateTime = LocalDateTime::now,
) : ViewModel() {

    private val centroId: String =
        savedStateHandle.get<String>(Screen.FichaCentro.ARG_CENTRO_ID).orEmpty()

    private val _uiState = MutableStateFlow<CenterDetailUiState>(CenterDetailUiState.Loading)
    val uiState: StateFlow<CenterDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val centro = runCatching { repositorioCentros.obtenerCentro(centroId) }.getOrNull()
            if (centro == null) {
                _uiState.value = CenterDetailUiState.NotFound
                return@launch
            }
            val estadoApertura = centro.horarios
                .takeIf { it.isNotEmpty() }
                ?.let { calcularEstadoApertura(it, ahora()) }
            // La ubicación la obtiene la lista de centros (solo en memoria). Si llega o cambia
            // con la ficha abierta, la distancia se actualiza; si no hay, queda en null.
            repositorioUbicacion.ubicacion.collect { ubicacion ->
                _uiState.value = CenterDetailUiState.Success(
                    centro = centro,
                    estadoApertura = estadoApertura,
                    distanciaKm = ubicacion?.let {
                        distanciaKm(it.latitud, it.longitud, centro.latitud, centro.longitud)
                    },
                )
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DonaVidaApplication
                CenterDetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    repositorioCentros = app.contenedor.repositorioCentros,
                    repositorioUbicacion = app.contenedor.repositorioUbicacion,
                )
            }
        }
    }
}
