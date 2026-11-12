package com.example.donavida.ui.screens.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.donavida.DonaVidaApplication
import com.example.donavida.data.repository.RepositorioPerfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Lo que necesita la pantalla de Inicio para pintarse. */
data class HomeUiState(
    /** Vacío mientras carga o si no hay nombre: entonces el saludo es solo "Hola". */
    val userName: String = "",
    val redmoActive: Boolean = false,
)

class HomeViewModel(
    private val repositorioPerfil: RepositorioPerfil,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Si el perfil no se puede leer, la pantalla sigue funcionando con el saludo genérico.
            val perfil = runCatching { repositorioPerfil.obtenerPerfil() }.getOrNull()
                ?: return@launch
            _uiState.update {
                it.copy(userName = perfil.nombre.trim(), redmoActive = perfil.redmoActivo)
            }
        }
    }

    companion object {
        /** Saca el repositorio del contenedor de la Application (inyección "a mano"). */
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DonaVidaApplication
                HomeViewModel(app.contenedor.repositorioPerfil)
            }
        }
    }
}
