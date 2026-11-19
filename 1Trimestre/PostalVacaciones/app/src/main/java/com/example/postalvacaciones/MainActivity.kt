package com.example.postalvacaciones

import android.R.attr.lineHeight
import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.postalvacaciones.ui.theme.PostalVacacionesTheme
import androidx.compose.material3.Icon


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostalVacacionesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PostalScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}


// ---------------------------------------------------------------
// EJERCICIO: "Postal de vacaciones"
// Capítulos 26 (Modifiers), 27 (Annotated strings y Brush),
// 28 (Row y Column) y 29 (Box)
//
// Cómo empezar:
// 1. Crea un proyecto Empty Activity llamado PostalDemo
//    (paquete com.example.postaldemo).
// 2. Borra la función Greeting y pega todo este archivo
//    debajo de MainActivity (quita la línea "package" duplicada).
// 3. En onCreate() llama a PostalScreen(Modifier.padding(innerPadding))
//    en lugar de Greeting, y haz lo mismo en el Preview del proyecto.
//
// Si Android Studio marca algún import en rojo: Alt+Enter.
// ---------------------------------------------------------------



// ---------------------------------------------------------------
// Pantalla completa (ya montada, no hace falta tocarla)
// ---------------------------------------------------------------
@Composable
fun PostalScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Etiqueta("Postal desde Mallorca")
        Spacer(Modifier.height(16.dp))
        TextoPostal()
        Spacer(Modifier.height(16.dp))
        Estadisticas()
        Spacer(Modifier.height(16.dp))
        FotoPostal()
    }
}

// ---------------------------------------------------------------
// PARTE 1 (cap. 26 - Modifiers)
// ---------------------------------------------------------------

// TODO 1.1: añade un parámetro "modifier: Modifier = Modifier"
//           (debe ser el primer parámetro opcional) y pásaselo al Text.
// TODO 1.2: crea dentro un modifier con padding(8.dp) y
//           border(2.dp, Color.Black) y aplícalo al Text.
// TODO 1.3: intercambia el orden de padding y border. ¿Qué cambia?
// TODO 1.4: desde PostalScreen, llama a Etiqueta pasándole
//           Modifier.background(Color.Yellow). ¿Dónde se ve el amarillo?
// TODO 1.5: guarda padding+border en un val y el background en otro,
//           y combínalos con .then()
@Composable
fun Etiqueta(texto: String,
              modifier: Modifier = Modifier) {
    val marco = Modifier
        .padding(8.dp)
        .border(2.dp, Color.Black)

    val background = Modifier.background(Color.Yellow)

    Text(texto,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = marco.then(background))
}

// ---------------------------------------------------------------
// PARTE 2 (cap. 27 - Annotated strings y Brush)
// ---------------------------------------------------------------

// TODO 2.1: sustituye este Text por uno que use buildAnnotatedString:
//           "¡Hola " en 30.sp y negrita, "desde " en gris y
//           "Mallorca" en negrita, cursiva y azul (SpanStyle).
// TODO 2.2: añade un párrafo con ParagraphStyle: lineHeight 28.sp y
//           sangría en la primera línea (TextIndent(firstLine = 40.sp)).
//           Texto sugerido: "Hace sol, la comida es increíble y el
//           mar está perfecto. Ojalá estuvierais aquí."
// TODO 2.3: añade una última línea "— Ana" alineada a la derecha
//           (ParagraphStyle con textAlign = TextAlign.End)
// TODO 2.4: debajo, un Text "¡BESOS!" en 50.sp y negrita con un
//           Brush.linearGradient de al menos 3 colores
@Composable
fun TextoPostal() {

    AnotatedString()
    Paragrafo()
    TextoBesos()

}

@Composable
fun TextoBesos() {
    Text(buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                brush = Brush.linearGradient(
                    colors = listOf(Color.Red, Color.Yellow, Color.Blue)
                )
            )
        ){
            append("BESOS")
        }
    })
}
@Composable
fun Paragrafo() {
    Text(
        buildAnnotatedString {
            withStyle(
                ParagraphStyle(
                    textAlign = TextAlign.Justify,
                    textIndent = TextIndent(firstLine = 40.sp),
                    lineHeight = 28.sp
                )
            ) {
                append("Hace sol, la comida es increíble y el mar está perfecto. Ojalá estuvierais aquí.")
            }
            withStyle(
                ParagraphStyle(
                    textAlign = TextAlign.End
                )
            ) {
                append("— Ana")
            }
        }
    )
}
@Composable
fun AnotatedString(){
    Text(buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append("¡Hola ")
        }
        withStyle(
            SpanStyle(
                color = Color.Gray
            )
        ) {
            append("desde ")
        }
        withStyle(
            SpanStyle(
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        ) {
            append("Mallorca")
        }
    })
}

// ---------------------------------------------------------------
// PARTE 3 (cap. 28 - Row y Column)
// ---------------------------------------------------------------
@Composable
fun Dato(numero: String, etiqueta: String) {
    // TODO 3.1: que sea una Column con los dos textos centrados
    //           horizontalmente. Número en 28.sp y negrita,
    //           etiqueta en 14.sp.
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(numero,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(etiqueta,
            fontSize = 14.sp
            )
    }
}

@Composable
fun Estadisticas() {
    Column {
        // TODO 3.2: Row que ocupe todo el ancho con tres Dato
        //           ("12", "días"), ("86", "fotos"), ("340", "km"),
        //           repartidos con Arrangement.SpaceEvenly.
        //           Prueba después SpaceBetween y SpaceAround.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Dato("12", "dias")
            Dato("86", "fotos")
            Dato("340", "km")
        }

        Spacer(Modifier.height(16.dp))

        // TODO 3.3: Row con dos Text: "24" en 48.sp y "°C" en 20.sp.
        //           Primero míralos sin ningún modifier. Después aplica
        //           Modifier.alignByBaseline() a los dos.

        Row() {
            Text("24",
                fontSize = 48.sp,
                modifier = Modifier.alignByBaseline())
            Text("ºC",
                fontSize = 20.sp,
                modifier = Modifier.alignByBaseline())
        }

        Spacer(Modifier.height(16.dp))

        // EXTRA 3.4: Row con tres Box de colores (altura 60.dp) y
        //            pesos 1f, 2f y 1f con Modifier.weight()

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .weight(1f)
                    .height(60.dp)
                    .background(Color.Red)
            )
            Box(
                Modifier
                    .weight(2f)
                    .height(60.dp)
                    .background(Color.Green)
            )
            Box(
                Modifier
                    .weight(1f)
                    .height(60.dp)
                    .background(Color.Blue)
            )
        }
    }
}

// ---------------------------------------------------------------
// PARTE 4 (cap. 29 - Box)
// ---------------------------------------------------------------
@Composable
fun FotoPostal() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))      // 4.1: primero clip…
            .background(Color(0xFF4FC3F7)),       // 4.1: …luego background
        contentAlignment = Alignment.Center       // 4.2
    ) {
        // 4.5: capa oscura, ANTES del icono
        Box(
            Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.2f))
        )

        // 4.2: icono centrado
        Icon(
            Icons.Default.Place,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        // 4.3: insignia circular arriba a la derecha
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Text("NUEVO", color = Color.White, fontSize = 12.sp)
        }

        // 4.4: texto abajo a la izquierda
        Text(
            "Palma de Mallorca",
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PostalScreenPreview() {
    MaterialTheme {
        PostalScreen()
    }
}