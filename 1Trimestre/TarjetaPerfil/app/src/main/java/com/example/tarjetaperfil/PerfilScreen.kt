package com.example.perfil

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ---------------------------------------------------------------
// Modelo de datos sencillo para la lista de intereses
// ---------------------------------------------------------------
data class Interes(val nombre: String, val icono: ImageVector)

// 12 elementos (el enunciado pide mínimo 10), cada uno con su icono
private val intereses = listOf(
    Interes("Desarrollo Android", Icons.Default.Build),
    Interes("Diseño de interfaces", Icons.Default.Star),
    Interes("Viajar", Icons.Default.Place),
    Interes("Domotica", Icons.Default.Home),
    Interes("Musica", Icons.Default.PlayArrow),
    Interes("Compras online", Icons.Default.ShoppingCart),
    Interes("Newsletters tech", Icons.Default.Email),
    Interes("Ajustes y productividad", Icons.Default.Settings),
    Interes("Fotografia", Icons.Default.Face),
    Interes("Cocina", Icons.Default.Favorite),
    Interes("Deporte", Icons.Default.ThumbUp),
    Interes("Networking", Icons.Default.Call)
)

// ---------------------------------------------------------------
// Pantalla principal
// ---------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen() {
    // Estado para el botón Seguir / Siguiendo (requisito extra)
    var siguiendo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: editar perfil */ }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()                 // ocupa toda la pantalla: weight(1f) de la lista queda bien definido
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // tarjeta centrada
        ) {
            // 1. Tarjeta de perfil
            TarjetaPerfil(nombre = "Ana García", cargo = "Desarrolladora Android")

            Spacer(Modifier.height(16.dp))

            // 2. Botones de igual ancho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { siguiendo = !siguiendo },
                    modifier = Modifier.weight(1f)
                ) {
                    if (siguiendo) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(if (siguiendo) "Siguiendo" else "Seguir")
                }
                OutlinedButton(
                    onClick = { /* TODO: enviar mensaje */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Mensaje")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Intereses",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()            // alineado a la izquierda aunque la Column centre
                    .padding(bottom = 8.dp)
            )

            // 3. Lista con scroll: weight(1f) hace que ocupe solo el espacio restante
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                itemsIndexed(intereses) { index, interes ->
                    ListItem(
                        headlineContent = { Text(interes.nombre) },
                        leadingContent = {
                            Icon(
                                imageVector = interes.icono,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    // Separador entre elementos, pero no después del último
                    if (index < intereses.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------
// Tarjeta con avatar, nombre, cargo y estadísticas
// ---------------------------------------------------------------
@Composable
private fun TarjetaPerfil(nombre: String, cargo: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar circular con iniciales
            // Orden de modifiers: size -> clip -> background -> border
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(3.dp, MaterialTheme.colorScheme.tertiary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = obtenerIniciales(nombre),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = cargo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Fila de estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Estadistica(numero = "42", etiqueta = "Proyectos")
                Estadistica(numero = "1.3k", etiqueta = "Seguidores")
                Estadistica(numero = "180", etiqueta = "Siguiendo")
            }
        }
    }
}

@Composable
private fun Estadistica(numero: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = numero,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

// "Ana García" -> "AG"
private fun obtenerIniciales(nombre: String): String =
    nombre.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

// ---------------------------------------------------------------
// Previews: tema claro y oscuro
// ---------------------------------------------------------------
@Preview(showBackground = true, showSystemUi = true, name = "Claro")
@Composable
private fun PerfilScreenPreviewClaro() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        PerfilScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PerfilScreenPreviewOscuro() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        PerfilScreen()
    }
}