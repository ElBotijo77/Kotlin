package com.example.claser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.claser.ui.theme.ClaseRTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClaseRTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Variables de estado: Se recuerdan con el remember
    // Para girar guardar el safeState
    var linearSelected by remember { mutableStateOf(true) }
    var imageSelected by remember { mutableStateOf(true) }
    val onLinearClick = {value: Boolean ->
        linearSelected = value
    }
    val onTitleClick = {value: Boolean ->
        imageSelected = value
    }

    ScreenContent(
        linearSelected = linearSelected,
        imageSelected = imageSelected,
        onLinearClick = onLinearClick,
        onTitleClick = onTitleClick,
        titleContent = {
            if (imageSelected) {
                TitleImage(drawing = R.drawable.baseline_arrow_circle_down_24)
            }
            else {
                Text("Descargando",
                    style=MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(30.dp))
            }
        },
        progressContent = {
            if (linearSelected) {
                LinearProgressIndicator(Modifier.height(40.dp))
            }
            else {
                CircularProgressIndicator(Modifier.size(200.dp),
                    strokeWidth = 18.dp)
            }
        }

    )
}

@Composable
fun ScreenContent(linearSelected : Boolean,
                  imageSelected : Boolean,
                  onTitleClick : (Boolean) -> Unit,
                  onLinearClick : (Boolean) -> Unit,        //Slots, funciones que se colocaran en algun sitio
                  titleContent : @Composable () -> Unit,
                  progressContent : @Composable () -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Slot 1
        titleContent()
        // Slot 2
        progressContent()
        CheckBoxes(linearSelected,imageSelected,onTitleClick,onLinearClick)
    }
}

@Composable
fun CheckBoxes (linearSelected : Boolean,
               imageSelected : Boolean,
               onTitleClick : (Boolean) -> Unit,
               onLinearClick : (Boolean) -> Unit){
    Row(
        Modifier.padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Checkbox(
            checked = imageSelected,
            onCheckedChange = onTitleClick
        )

        Text("Image Tittle")
        Spacer(Modifier.width(20.dp))

        Checkbox(
            checked = linearSelected,
            onCheckedChange = onLinearClick
        )
        Text("Linear Progress")
    }
}

@Composable
fun TitleImage(drawing: Int){
    Image(
        painter = painterResource(id = drawing),
        contentDescription = "Imagen de titulo",
        modifier = Modifier.size(150.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClaseRTheme {
        MainScreen()
    }
}