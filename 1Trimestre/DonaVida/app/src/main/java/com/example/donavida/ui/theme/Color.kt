package com.example.donavida.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Paleta del diseño de Stitch. Usa los nombres de los roles de Material 3, así que se pasa
// tal cual a lightColorScheme() en Theme.kt. El diseño solo define tema claro.

val Primary = Color(0xFF006070)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF1F7A8C)
val OnPrimaryContainer = Color(0xFFE3F8FF)

val Secondary = Color(0xFFA33D23)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFFF8162)
val OnSecondaryContainer = Color(0xFF731A04)

val Tertiary = Color(0xFF3E5D57)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFF56766F)
val OnTertiaryContainer = Color(0xFFD9FCF3)

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

val Background = Color(0xFFF3FAFF)
val OnBackground = Color(0xFF0E1E24)
val Surface = Color(0xFFF3FAFF)
val OnSurface = Color(0xFF0E1E24)
val SurfaceVariant = Color(0xFFD5E5EE)
val OnSurfaceVariant = Color(0xFF3F484B)
val SurfaceTint = Color(0xFF006879)
val SurfaceBright = Color(0xFFF3FAFF)
val SurfaceDim = Color(0xFFCCDDE5)

val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFE6F6FF)
val SurfaceContainer = Color(0xFFE0F0F9)
val SurfaceContainerHigh = Color(0xFFDAEBF4)
val SurfaceContainerHighest = Color(0xFFD5E5EE)

val Outline = Color(0xFF6F797C)
val OutlineVariant = Color(0xFFBEC8CB)
val InverseSurface = Color(0xFF243239)
val InverseOnSurface = Color(0xFFE3F3FC)
val InversePrimary = Color(0xFF83D2E6)

/**
 * Colores "fixed" del diseño. ColorScheme de Material 3 no los incluye, así que van aparte
 * y se leen con `DonaVidaTheme.extendedColors` (ver Theme.kt).
 */
@Immutable
data class ExtendedColors(
    val primaryFixed: Color,
    val primaryFixedDim: Color,
    val onPrimaryFixed: Color,
    val onPrimaryFixedVariant: Color,
    val secondaryFixed: Color,
    val secondaryFixedDim: Color,
    val onSecondaryFixed: Color,
    val onSecondaryFixedVariant: Color,
    val tertiaryFixed: Color,
    val tertiaryFixedDim: Color,
    val onTertiaryFixed: Color,
    val onTertiaryFixedVariant: Color,
)

val ColoresExtendidosClaros = ExtendedColors(
    primaryFixed = Color(0xFFA9EDFF),
    primaryFixedDim = Color(0xFF83D2E6),
    onPrimaryFixed = Color(0xFF001F26),
    onPrimaryFixedVariant = Color(0xFF004E5B),
    secondaryFixed = Color(0xFFFFDAD2),
    secondaryFixedDim = Color(0xFFFFB4A2),
    onSecondaryFixed = Color(0xFF3C0700),
    onSecondaryFixedVariant = Color(0xFF83260E),
    tertiaryFixed = Color(0xFFC7EAE1),
    tertiaryFixedDim = Color(0xFFACCEC5),
    onTertiaryFixed = Color(0xFF00201B),
    onTertiaryFixedVariant = Color(0xFF2D4C46),
)
