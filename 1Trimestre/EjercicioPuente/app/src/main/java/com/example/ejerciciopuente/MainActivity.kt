package com.example.ejerciciopuente

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                EjercicioPuente()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EjercicioPuente() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi primera pantalla!") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_input_add),
                    contentDescription = "Añadir"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1: Imagen circular con borde
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            // 2: Texto de bienvenida
            Text(
                text = "¡Bienvenido a mi app!",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // 3: Dos botones
            Button(onClick = { }) {
                Text("Entrar")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(onClick = { }) {
                Text("Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EjercicioPuentePreview() {
    EjercicioPuente()
}