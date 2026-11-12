package com.example.donavida.ui.screens.inicio

import androidx.annotation.StringRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.donavida.R
import com.example.donavida.ui.components.IconCircle
import com.example.donavida.ui.components.InfoChip
import com.example.donavida.ui.components.PrimaryPillButton
import com.example.donavida.ui.components.SectionCard
import com.example.donavida.ui.components.TonalPillButton
import com.example.donavida.ui.theme.AppShapes
import com.example.donavida.ui.theme.AppTypography
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/** Versión "con estado": obtiene el ViewModel y le pasa su estado a [HomeScreen]. */
@Composable
fun HomeRoute(
    contentPadding: PaddingValues,
    onFindCenter: () -> Unit,
    onSeeProcess: () -> Unit,
    onMyDonation: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        contentPadding = contentPadding,
        onFindCenter = onFindCenter,
        onSeeProcess = onSeeProcess,
        onMyDonation = onMyDonation,
    )
}

/**
 * Pantalla de Inicio. Solo pinta [uiState] y avisa con callbacks; no conoce el NavController.
 *
 * @param contentPadding hueco de las barras del AppScaffold. Se suma al relleno de la lista,
 *   para que el contenido empiece debajo de la barra superior pero pueda pasar por detrás.
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onFindCenter: () -> Unit,
    onSeeProcess: () -> Unit,
    onMyDonation: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val direccion = LocalLayoutDirection.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(direccion) + Spacing.margin,
            end = contentPadding.calculateEndPadding(direccion) + Spacing.margin,
            top = contentPadding.calculateTopPadding() + Spacing.md,
            bottom = contentPadding.calculateBottomPadding() + Spacing.xl,
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
    ) {
        item { GreetingHeader(userName = uiState.userName, redmoActive = uiState.redmoActive) }
        item { DonorCtaCard(onFindCenter = onFindCenter) }
        item { StatVignette() }
        item { ProcessStepperCard(onSeeProcess = onSeeProcess) }
        item { ReassuranceBanner() }
        item { RegisteredDonorCard(onMyDonation = onMyDonation) }
        item { ClosingImageCard() }
    }
}

// ---------------------------------------------------------------------------------------
// 1. Saludo
// ---------------------------------------------------------------------------------------

@Composable
private fun GreetingHeader(userName: String, redmoActive: Boolean, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .semantics(mergeDescendants = true) { heading() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (userName.isBlank()) {
                        stringResource(R.string.inicio_saludo_sin_nombre)
                    } else {
                        stringResource(R.string.inicio_saludo, userName)
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Spacer(Modifier.width(Spacing.sm))
                SaludoEmoji()
            }
            if (redmoActive) {
                Spacer(Modifier.width(Spacing.sm))
                RedmoStatusChip()
            }
        }
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(R.string.inicio_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** 👋 con un pequeño rebote cada pocos segundos. TalkBack no lo lee: es decorativo. */
@Composable
private fun SaludoEmoji() {
    val transicion = rememberInfiniteTransition(label = "saludo")
    val desplazamiento by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2400
                0f at 0
                -4f at 200
                0f at 400
                -2f at 550
                0f at 700
            },
        ),
        label = "rebote",
    )
    Text(
        text = stringResource(R.string.inicio_saludo_emoji),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier
            .graphicsLayer { translationY = desplazamiento * density }
            .clearAndSetSemantics { },
    )
}

@Composable
private fun RedmoStatusChip(modifier: Modifier = Modifier) {
    val transicion = rememberInfiniteTransition(label = "pulsoRedmo")
    val opacidad by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1000), RepeatMode.Reverse),
        label = "opacidad",
    )
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, AppShapes.pill)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(8.dp)
                .graphicsLayer { alpha = opacidad }
                .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
        )
        Text(
            text = stringResource(R.string.inicio_estado_redmo),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

// ---------------------------------------------------------------------------------------
// 2. Hazte donante
// ---------------------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DonorCtaCard(onFindCenter: () -> Unit, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    val colorDecoracion = colores.surfaceContainerLow.copy(alpha = 0.7f)

    SectionCard(
        modifier = modifier,
        // Círculo de 160 dp que asoma por la esquina superior derecha (desplazado 48 dp).
        decoration = {
            val radio = 80.dp.toPx()
            val desplazamiento = 48.dp.toPx()
            drawCircle(
                color = colorDecoracion,
                radius = radio,
                center = Offset(size.width - radio + desplazamiento, radio - desplazamiento),
            )
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(
                icon = R.drawable.ic_favorite,
                size = 40.dp,
                background = colores.surfaceContainer,
                tint = colores.primary,
            )
            Spacer(Modifier.width(Spacing.sm + Spacing.xs))
            Text(
                text = stringResource(R.string.inicio_cta_etiqueta),
                style = MaterialTheme.typography.labelMedium,
                color = colores.primary,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.inicio_cta_badge),
                style = MaterialTheme.typography.labelSmall,
                color = extendidos.onSecondaryFixed,
                modifier = Modifier
                    .background(extendidos.secondaryFixed, AppShapes.pill)
                    .padding(horizontal = 10.dp, vertical = Spacing.xs),
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Text(
            text = stringResource(R.string.inicio_cta_titulo),
            style = MaterialTheme.typography.headlineMedium,
            color = colores.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(R.string.inicio_cta_texto),
            style = MaterialTheme.typography.bodyMedium,
            color = colores.onSurfaceVariant,
        )
        Spacer(Modifier.height(Spacing.md))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            InfoChip(R.drawable.ic_calendar_today, stringResource(R.string.inicio_chip_edad))
            InfoChip(R.drawable.ic_biotech, stringResource(R.string.inicio_chip_muestra))
            InfoChip(R.drawable.ic_public, stringResource(R.string.inicio_chip_registro))
        }
        Spacer(Modifier.height(Spacing.lg))
        PrimaryPillButton(
            text = stringResource(R.string.inicio_cta_boton),
            icon = R.drawable.ic_pin_drop,
            onClick = onFindCenter,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// ---------------------------------------------------------------------------------------
// 3. Dato
// ---------------------------------------------------------------------------------------

@Composable
private fun StatVignette(modifier: Modifier = Modifier) {
    SectionCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentPadding = Spacing.md,
        elevated = false,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.vignette_woman),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
            )
            Spacer(Modifier.width(Spacing.md))
            Column(modifier = Modifier.semantics(mergeDescendants = true) {}) {
                Text(
                    text = stringResource(R.string.inicio_dato_cifra),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = stringResource(R.string.inicio_dato_texto),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------------------
// 4. El camino
// ---------------------------------------------------------------------------------------

/** Un paso del camino del donante; los colores del círculo cambian en cada paso. */
data class ProcessStep(
    val number: Int,
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
    val nodeColor: Color,
    val numberColor: Color,
)

@Composable
private fun pasosDelProceso(): List<ProcessStep> {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    return listOf(
        ProcessStep(1, R.string.inicio_paso1_titulo, R.string.inicio_paso1_texto, colores.primaryContainer, colores.onPrimary),
        ProcessStep(2, R.string.inicio_paso2_titulo, R.string.inicio_paso2_texto, colores.surfaceContainerHighest, colores.primary),
        ProcessStep(3, R.string.inicio_paso3_titulo, R.string.inicio_paso3_texto, extendidos.secondaryFixed, extendidos.onSecondaryFixed),
    )
}

@Composable
private fun ProcessStepperCard(onSeeProcess: () -> Unit, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    SectionCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircle(
                icon = R.drawable.ic_timeline,
                size = 36.dp,
                background = colores.surfaceContainer,
                tint = colores.primary,
            )
            Spacer(Modifier.width(Spacing.sm + Spacing.xs))
            Text(
                text = stringResource(R.string.inicio_proceso_titulo),
                style = MaterialTheme.typography.headlineSmall,
                color = colores.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() },
            )
            Spacer(Modifier.width(Spacing.sm))
            Text(
                text = stringResource(R.string.inicio_proceso_etapas),
                style = AppTypography.caption,
                color = colores.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(Spacing.lg))
        val pasos = pasosDelProceso()
        pasos.forEachIndexed { indice, paso ->
            PasoDelProceso(paso = paso, esUltimo = indice == pasos.lastIndex)
        }
        Spacer(Modifier.height(Spacing.lg))
        TonalPillButton(
            text = stringResource(R.string.inicio_proceso_boton),
            icon = R.drawable.ic_arrow_forward,
            iconAtEnd = true,
            onClick = onSeeProcess,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Nodo numerado + textos. La línea vertical que une los nodos se dibuja por tramos: cada
 * paso (menos el último) alarga una línea de 2 dp desde su nodo hasta el siguiente.
 * IntrinsicSize.Min hace que la fila mida lo que su texto, y la línea se estira hasta ahí.
 */
@Composable
private fun PasoDelProceso(paso: ProcessStep, esUltimo: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .semantics(mergeDescendants = true) {},
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(paso.nodeColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = paso.number.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = paso.numberColor,
                )
            }
            if (!esUltimo) {
                Box(
                    Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                )
            }
        }
        Spacer(Modifier.width(Spacing.md))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 6.dp, bottom = if (esUltimo) 0.dp else Spacing.lg),
        ) {
            Text(
                text = stringResource(paso.title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text = stringResource(paso.description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ---------------------------------------------------------------------------------------
// 5. Tranquilidad
// ---------------------------------------------------------------------------------------

@Composable
private fun ReassuranceBanner(modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    val extendidos = DonaVidaTheme.extendedColors
    val destacado = stringResource(R.string.inicio_tranquilidad_destacado)
    val textoCompleto = stringResource(R.string.inicio_tranquilidad_texto, destacado)
    // El destacado va dentro del texto traducible; se busca para ponerlo en negrita.
    val texto = buildAnnotatedString {
        val inicio = textoCompleto.indexOf(destacado)
        if (inicio < 0) {
            append(textoCompleto)
        } else {
            append(textoCompleto.substring(0, inicio))
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colores.onSurface)) {
                append(destacado)
            }
            append(textoCompleto.substring(inicio + destacado.length))
        }
    }

    SectionCard(
        modifier = modifier,
        containerColor = colores.surfaceContainerLow,
        elevated = false,
    ) {
        Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
            IconCircle(
                icon = R.drawable.ic_health_and_safety,
                size = 40.dp,
                background = extendidos.tertiaryFixed,
                tint = extendidos.onTertiaryFixed,
            )
            Spacer(Modifier.width(Spacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.inicio_tranquilidad_titulo),
                    style = MaterialTheme.typography.labelMedium,
                    color = colores.tertiary,
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = texto,
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------------------
// 6. Ya soy donante
// ---------------------------------------------------------------------------------------

@Composable
private fun RegisteredDonorCard(onMyDonation: () -> Unit, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    SectionCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_badge),
                contentDescription = null,
                tint = colores.primary,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(Spacing.xs + 2.dp))
            Text(
                text = stringResource(R.string.inicio_comunidad_etiqueta),
                style = MaterialTheme.typography.labelSmall,
                color = colores.primary,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = colores.outlineVariant,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(Modifier.height(Spacing.md))
        Text(
            text = stringResource(R.string.inicio_comunidad_titulo),
            style = MaterialTheme.typography.headlineSmall,
            color = colores.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(R.string.inicio_comunidad_texto),
            style = MaterialTheme.typography.bodyMedium,
            color = colores.onSurfaceVariant,
        )
        Spacer(Modifier.height(Spacing.lg))
        TonalPillButton(
            text = stringResource(R.string.inicio_comunidad_boton),
            icon = R.drawable.ic_volunteer_activism,
            onClick = onMyDonation,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// ---------------------------------------------------------------------------------------
// 7. Cierre
// ---------------------------------------------------------------------------------------

@Composable
private fun ClosingImageCard(modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(144.dp)
            .clip(AppShapes.card),
    ) {
        Image(
            painter = painterResource(R.drawable.closing_abstract),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        // Oscurece la parte de abajo para que el texto claro se lea bien.
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.5f to colores.inverseSurface.copy(alpha = 0.3f),
                        1f to colores.inverseSurface.copy(alpha = 0.7f),
                    ),
                ),
        )
        Text(
            text = stringResource(R.string.inicio_cierre),
            style = AppTypography.caption,
            color = colores.inverseOnSurface,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Spacing.md),
        )
    }
}

// ---------------------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------------------

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF, heightDp = 1900)
@Composable
private fun HomeScreenPreview() {
    DonaVidaTheme {
        HomeScreen(
            uiState = HomeUiState(userName = "Elena", redmoActive = true),
            onFindCenter = {},
            onSeeProcess = {},
            onMyDonation = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun GreetingHeaderPreview() {
    DonaVidaTheme {
        GreetingHeader(userName = "Elena", redmoActive = true, modifier = Modifier.padding(Spacing.margin))
    }
}

@Preview
@Composable
private fun RedmoStatusChipPreview() {
    DonaVidaTheme { RedmoStatusChip() }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun DonorCtaCardPreview() {
    DonaVidaTheme { DonorCtaCard(onFindCenter = {}, modifier = Modifier.padding(Spacing.margin)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun StatVignettePreview() {
    DonaVidaTheme { StatVignette(modifier = Modifier.padding(Spacing.margin)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun ProcessStepperCardPreview() {
    DonaVidaTheme { ProcessStepperCard(onSeeProcess = {}, modifier = Modifier.padding(Spacing.margin)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun ReassuranceBannerPreview() {
    DonaVidaTheme { ReassuranceBanner(modifier = Modifier.padding(Spacing.margin)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun RegisteredDonorCardPreview() {
    DonaVidaTheme { RegisteredDonorCard(onMyDonation = {}, modifier = Modifier.padding(Spacing.margin)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3FAFF)
@Composable
private fun ClosingImageCardPreview() {
    DonaVidaTheme { ClosingImageCard(modifier = Modifier.padding(Spacing.margin)) }
}
