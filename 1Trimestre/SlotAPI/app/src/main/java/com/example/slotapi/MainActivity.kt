package com.example.slotapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.slotapi.ui.theme.SlotAPITheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlotAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(
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
        topContent = {
            Text(text = "Top content")
        },
        middleContent = {
            ButtonDemo()
        },
        bottomContent = {
            Text(text = "Bottom content")
        })
}

@Composable
fun SlotDemo(
    topContent: @Composable () -> Unit,
    middleContent: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit
) {
    Column {
        topContent()
        Spacer(modifier = Modifier.height(16.dp))
        middleContent()
        Spacer(modifier = Modifier.height(16.dp))
        bottomContent()
    }
}

@Composable
fun ButtonDemo(){
    Button(onClick = {}){
        Text(text = "Click aqui")
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
    SlotAPITheme {
        DemoScreen()
    }
}