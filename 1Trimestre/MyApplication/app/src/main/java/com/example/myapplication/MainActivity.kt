package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import androidx.core.app.NotificationCompat
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DemoScreen();
            }
        }
    }
}

@Composable
fun DemoScreen(){
    // Ideal para guardar siempre la ultima posicion para repintar en el mismo valor
    // Continuamente se esta llamando al codigo
    var sliderPosition by remember { mutableStateOf(value = 20f) }
    val handlePositionChange = { position: Float ->
        sliderPosition = position
    }

    // Maquetacion, metemos en una columna el DemoText y el DemoSlider
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        DemoText("Illo", 40f)
        // Añade un espacio con un valor dado
        Spacer(modifier = Modifier.height(150.dp))
        DemoSlider(
            sliderPosition = sliderPosition,
            onPositionChange = handlePositionChange
        )
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = sliderPosition.toInt().toString() + "sp"
        )
    }
}

@Composable
fun DemoText(message: String, fonSize: Float){
    Text(
        text = message,
        fontSize = fonSize.sp,
        fontWeight = FontWeight.Bold
    )
}

// Segundo parametro: Pasamos una funcion que no vale nada
@Composable
fun DemoSlider(sliderPosition: Float, onPositionChange: (Float) -> Unit){
    Slider(
        modifier = Modifier.padding(10.dp),
        valueRange = 20f.. 38f,
        onValueChange = {onPositionChange(it)},
        value = sliderPosition
    )
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
    MyApplicationTheme {
        DemoScreen()
    }
}