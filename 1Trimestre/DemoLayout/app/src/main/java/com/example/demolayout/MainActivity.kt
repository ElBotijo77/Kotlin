package com.example.demolayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demolayout.ui.theme.DemoLayoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding ->
                DemoScreen(modifier = Modifier.padding(innerPadding)
                    .size(width=1000.dp, 850.dp))
            }
        }
    }
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        DemoRow()
    }
}
@Composable
fun DemoRow(modifier: Modifier = Modifier) {
    //Row(verticalAlignment= Alignment.Bottom,
    //Row(horizontalArrangement = Arrangement.SpaceAround,
    Row(
        modifier = modifier.size(width=324.dp, 180.dp)) {
        TextCell("1", Modifier.align(Alignment.Bottom))
        TextCell("2", Modifier.align(Alignment.CenterVertically))
        TextCell("3", Modifier.align(Alignment.Bottom))
        TextCell("4", Modifier.align(Alignment.Top))
        TextCell("5", Modifier.align(Alignment.Bottom))
        TextCell("6", Modifier.align(Alignment.Bottom))
    }
}

@Composable
fun Tablero(modifier: Modifier = Modifier, filas: Int = 8, columnas: Int = 8) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (fila in 0 until filas) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 0 until columnas) {
                    val esClara = (fila + col) % 2 == 0
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(if (esClara) Color(0xFFEEEED2) else Color(0xFF769656)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${fila * columnas + col + 1}", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DemoBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(1200.dp, 700.dp)) {
        TextCell("1", Modifier.align(Alignment.TopStart)
            .background(Color.Blue))
        TextCell("2", Modifier.align(Alignment.Center)
            .background(Color.Green))
        TextCell("3", Modifier.align(Alignment.BottomEnd)
        .background(Color.Black))
        TextCell("4", Modifier.align(Alignment.BottomEnd)
            .background(Color.Black))
        TextCell("5", Modifier.align(Alignment.BottomEnd)
            .background(Color.Black))
        TextCell("6", Modifier.align(Alignment.BottomEnd)
            .background(Color.Black))

    }
}

@Composable
fun TextCell(text: String, modifier: Modifier = Modifier) {
    val cellModifier = modifier
        .padding(4.dp)
        .size(100.dp, 100.dp)
        .border(width = 4.dp, color = Color.Black, shape = CutCornerShape(10.dp))

    Surface(modifier=modifier) {
        Text(
            text = text, cellModifier.then(modifier),
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
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
    DemoLayoutTheme {
        Tablero()
    }
}