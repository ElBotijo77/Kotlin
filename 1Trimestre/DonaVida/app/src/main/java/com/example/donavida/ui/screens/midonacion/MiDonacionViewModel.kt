package com.example.donavida.ui.screens.midonacion

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * PROTOTIPO: solo recuerda si la sesión está "iniciada", para que no se pierda al cambiar de
 * pestaña. El inicio de sesión es simulado; no hay ninguna autenticación real.
 */
class MiDonacionViewModel : ViewModel() {
    private val _sesionIniciada = MutableStateFlow(false)
    val sesionIniciada: StateFlow<Boolean> = _sesionIniciada.asStateFlow()

    fun cambiarSesion(iniciada: Boolean) {
        _sesionIniciada.value = iniciada
    }
}
