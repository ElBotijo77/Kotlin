package com.example.donavida.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.donavida.R

/** Plus Jakarta Sans (licencia OFL), empaquetada en res/font con los cuatro pesos que se usan. */
val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

// El diseño da el espaciado entre letras en "em". Compose también admite em, que equivale a
// multiplicarlo por el tamaño de la fuente, así que se usa directamente.
private fun estilo(tamano: Int, interlineado: Int, peso: FontWeight, espaciadoEm: Double) = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = peso,
    fontSize = tamano.sp,
    lineHeight = interlineado.sp,
    letterSpacing = espaciadoEm.em,
)

private val Base = Typography()

val Typography = Typography(
    displayLarge = estilo(34, 42, FontWeight.Bold, -0.02),
    headlineLarge = estilo(26, 34, FontWeight.Bold, -0.015),
    headlineMedium = estilo(22, 28, FontWeight.SemiBold, -0.01),
    headlineSmall = estilo(19, 26, FontWeight.SemiBold, 0.0),
    bodyLarge = estilo(17, 26, FontWeight.Normal, 0.0),
    bodyMedium = estilo(16, 24, FontWeight.Normal, 0.0),
    bodySmall = estilo(14, 20, FontWeight.Normal, 0.01),
    labelLarge = estilo(16, 22, FontWeight.SemiBold, 0.01),
    labelMedium = estilo(14, 18, FontWeight.SemiBold, 0.015),
    labelSmall = estilo(12, 16, FontWeight.SemiBold, 0.02),
    // El diseño no define estos estilos: se dejan los de Material 3 con nuestra fuente.
    displayMedium = Base.displayMedium.copy(fontFamily = PlusJakartaSans),
    displaySmall = Base.displaySmall.copy(fontFamily = PlusJakartaSans),
    titleLarge = Base.titleLarge.copy(fontFamily = PlusJakartaSans),
    titleMedium = Base.titleMedium.copy(fontFamily = PlusJakartaSans),
    titleSmall = Base.titleSmall.copy(fontFamily = PlusJakartaSans),
)

/** Estilos del diseño que no tienen hueco en la Typography de Material 3. */
object AppTypography {
    val caption = estilo(12, 16, FontWeight.Normal, 0.01)
}
