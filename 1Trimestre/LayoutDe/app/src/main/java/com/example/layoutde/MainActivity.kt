package com.example.layoutde

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.layoutde.ui.theme.LayoutDeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayoutDeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    //DemoFlowColumn(modifier)
    DemoFlowRow(modifier)
}

data class ItemProperties(
    val color: Color,
    val width: Dp,
    val height: Dp
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoFlowRow(modifier: Modifier = Modifier) {
    val items = (1 .. 12).map {
        ItemProperties(
            width = Random.nextInt(20, 100).dp,
            height = Random.nextInt(10, 40).dp,
            color = Color(
                Random.nextInt(255),
                Random.nextInt(255),
                Random.nextInt(255),
                255
            )
        )
    }

    FlowRow(modifier.width(400.dp),
        horizontalArrangement = Arrangement.Start) {
        items.forEach { properties ->
            Box(modifier = Modifier
                .align(Alignment.Bottom)
                .padding(2.dp)
                .width(properties.width)
                .height(properties.height)
                .clip(RoundedCornerShape(8.dp))
                .background(properties.color)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoFlowColumn(modifier: Modifier = Modifier) {
    val items = (1 .. 24).map {
        ItemProperties(
            width = Random.nextInt(20, 100).dp,
            height = Random.nextInt(10, 40).dp,
            color = Color(
                Random.nextInt(255),
                Random.nextInt(255),
                Random.nextInt(255),
                255
            )
        )
    }

    FlowColumn(modifier
        .width(300.dp)
        .height(120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center) {
/*        items.forEach { properties ->
            Box(modifier = Modifier
                .padding(2.dp)
                .width(30.dp)
                .height(properties.height)
                .clip(RoundedCornerShape(8.dp))
                .background(properties.color)
            )
        }*/
        items.forEachIndexed { index, properties ->
            var peso = 0.5f
            if (index % 2 == 0) {
                peso = 2f
            } else if (index % 3 == 0) {
                peso = 3f
            }
            Box(modifier = Modifier
                .weight(peso)
                .padding(2.dp)
                .width(30.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(properties.color)
            )
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
    LayoutDeTheme {
        Greeting("Android")
    }
}