package com.example.slotapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slotapi.ui.theme.SlotAPITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlotAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenEjercicioUno(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    SlotDemo(
        modifier = modifier,
        topContent = {
            Text(text = "Top content")
        },
        middleContent = {
            ButtonDemo()
        },
        bottomContent = {
            Text(text = "Bottom content")
        }
    )
}

@Composable
fun SlotDemo(
    topContent: @Composable () -> Unit,
    middleContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        topContent()
        Spacer(modifier = Modifier.height(16.dp))
        middleContent()
        Spacer(modifier = Modifier.height(16.dp))
        bottomContent()
    }
}

@Composable
fun ButtonDemo() {
    Button(onClick = {}) {
        Text(text = "Click aqui")
    }
}

@Composable
fun ScreenEjercicioUno(modifier: Modifier = Modifier) {
    SlotDemo(
        modifier = modifier,
        topContent = {
            Nombre()
            Espacio()
        },
        middleContent = {
            NombreInput()
            BotonPulsar()
            Espacio()
        },
        bottomContent = {
            Header()
        }
    )
}

@Composable
fun Nombre() {
    Text(
        text = "Ejercicio 1",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp),
        fontSize = 26.sp
    )
}

@Composable
fun NombreInput() {
    var nombre by remember { mutableStateOf("") }

    OutlinedTextField(
        value = nombre,
        onValueChange = { nombre = it },
        placeholder = { Text("Introduce tu nombre") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun BotonPulsar() {
    Button(onClick = {}) {
        Text(text = "Pulsar")
    }
}

@Composable
fun Header() {
    Text(text = "2025 Curso de Android",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,)
}

@Composable
fun Espacio() {
    Spacer(modifier = Modifier.height(200.dp))
}

@Preview(showBackground = true)
@Composable
fun EjercicioUnoPreview() {
    SlotAPITheme {
        ScreenEjercicioUno()
    }
}