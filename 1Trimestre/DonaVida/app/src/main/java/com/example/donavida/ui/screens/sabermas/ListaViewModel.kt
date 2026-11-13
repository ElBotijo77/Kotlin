package com.example.donavida.ui.screens.sabermas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.donavida.DonaVidaApplication
import com.example.donavida.data.ContenedorDependencias
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de las pantallas de "Saber más": todas solo cargan una lista de un repositorio
 * y la pintan, así que comparten este. [elementos] es null mientras se carga; si el JSON no
 * se puede leer, queda vacía y la pantalla no se cierra.
 */
class ListaViewModel<T>(private val cargar: suspend () -> List<T>) : ViewModel() {

    private val _elementos = MutableStateFlow<List<T>?>(null)
    val elementos: StateFlow<List<T>?> = _elementos.asStateFlow()

    init {
        viewModelScope.launch {
            _elementos.value = runCatching { cargar() }.getOrDefault(emptyList())
        }
    }

    companion object {
        /** Ejemplo: `ListaViewModel.fabrica { repositorioMitos.obtenerMitos() }`. */
        fun <T> fabrica(cargar: suspend ContenedorDependencias.() -> List<T>): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val contenedor = (this[APPLICATION_KEY] as DonaVidaApplication).contenedor
                    ListaViewModel { contenedor.cargar() }
                }
            }
    }
}
