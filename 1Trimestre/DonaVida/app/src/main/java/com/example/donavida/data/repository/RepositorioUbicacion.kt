package com.example.donavida.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.donavida.data.model.Ubicacion
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Ubicación de la persona, para ordenar los centros por cercanía y mostrar distancias.
 *
 * Privacidad: la ubicación solo vive en memoria mientras la app está abierta. No se guarda
 * en disco ni en DataStore, no se envía a ningún servidor y no se usa Geocoder (que puede
 * mandar las coordenadas a un servidor para convertirlas en un nombre de calle).
 */
interface RepositorioUbicacion {
    /** Última ubicación obtenida, o null si aún no se ha pedido. */
    val ubicacion: StateFlow<Ubicacion?>

    /**
     * Pide la ubicación actual y la guarda en [ubicacion].
     * @return la ubicación, o null si el sistema no la ha podido calcular.
     * @throws SecurityException si no se ha concedido el permiso de ubicación.
     */
    suspend fun obtenerUbicacion(): Ubicacion?
}

/** Implementación con FusedLocationProviderClient de Google Play Services. */
class RepositorioUbicacionFused(private val context: Context) : RepositorioUbicacion {

    private val cliente by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val _ubicacion = MutableStateFlow<Ubicacion?>(null)
    override val ubicacion: StateFlow<Ubicacion?> = _ubicacion.asStateFlow()

    @SuppressLint("MissingPermission") // El permiso se comprueba justo antes.
    override suspend fun obtenerUbicacion(): Ubicacion? {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        if (!concedido) throw SecurityException("Falta el permiso de ubicación")

        val posicion = suspendCancellableCoroutine { continuacion ->
            val cancelacion = CancellationTokenSource()
            cliente.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancelacion.token)
                .addOnSuccessListener { continuacion.resume(it) }
                .addOnFailureListener { continuacion.resumeWithException(it) }
            continuacion.invokeOnCancellation { cancelacion.cancel() }
        }
        return posicion?.let { Ubicacion(it.latitude, it.longitude) }
            ?.also { _ubicacion.value = it }
    }
}
