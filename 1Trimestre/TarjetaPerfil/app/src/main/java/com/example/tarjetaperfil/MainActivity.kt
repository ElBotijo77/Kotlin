package com.example.tarjetaperfil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.perfil.PerfilScreen
import com.example.tarjetaperfil.ui.theme.TarjetaPerfilTheme
/*
* Ejercicio completo tarjeta de perfil
Se trata de crear una pantalla con esta estructura:

Scaffold con una TopAppBar (título “Mi perfil”) y un FloatingActionButton (icono de editar).
Una tarjeta (Card) centrada que contenga:
Un avatar circular (Box o Image con CircleShape) con las iniciales del usuario.
El nombre (Text con MaterialTheme.typography.headlineSmall) y el cargo debajo.
Un HorizontalDivider.
Una fila de 3 estadísticas (Proyectos, Seguidores, Siguiendo), cada una como Column con número y etiqueta, repartidas con Arrangement.SpaceEvenly.
Debajo de la tarjeta, una lista de intereses con LazyColumn (mínimo 10 elementos), cada uno en un ListItem con icono a la izquierda.
Un Button “Seguir” y un OutlinedButton “Mensaje” en una Row, de igual ancho (Modifier.weight(1f)).
Requerimientos extra
Que el botón “Seguir” cambie a “Siguiendo” al pulsarlo (remember + mutableStateOf).
Añadir un @Preview con y sin tema oscuro.
Usar colores del tema (MaterialTheme.colorScheme) en lugar de colores fijos.

PerfilScreen.kt

*
* */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TarjetaPerfilTheme {
                PerfilScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TarjetaPerfilTheme {
        PerfilScreen()
    }
}