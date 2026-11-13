package com.example.donavida

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.donavida.ui.DonaVidaApp
import com.example.donavida.ui.theme.DonaVidaTheme

/** Única Activity: solo aplica el tema y monta la interfaz; el resto es Compose. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Pantalla de carga del sistema (tema Theme.DonaVida.Starting). Debe ir antes de
        // super.onCreate(); desaparece en cuanto la app dibuja su primer fotograma.
        installSplashScreen()
        // La app solo tiene tema claro: iconos de las barras del sistema siempre oscuros,
        // aunque el móvil esté en modo oscuro (si no, saldrían blancos sobre fondo claro).
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(ScrimBarraNavegacion, ScrimBarraNavegacion),
        )
        super.onCreate(savedInstanceState)
        setContent {
            DonaVidaTheme {
                DonaVidaApp()
            }
        }
    }

    private companion object {
        /** Velo blanco (90 %) tras los botones de navegación clásicos, como el de Android. */
        val ScrimBarraNavegacion = Color.argb(0xE6, 0xFF, 0xFF, 0xFF)
    }
}
