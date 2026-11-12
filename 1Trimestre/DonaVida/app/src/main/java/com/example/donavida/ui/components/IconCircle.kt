package com.example.donavida.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.theme.DonaVidaTheme

/**
 * Icono centrado dentro de un círculo de color. Por defecto es decorativo
 * ([contentDescription] = null): el texto de al lado ya dice lo que significa.
 */
@Composable
fun IconCircle(
    @DrawableRes icon: Int,
    size: Dp,
    background: Color,
    tint: Color,
    modifier: Modifier = Modifier,
    iconSize: Dp = size * 0.5f,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(background, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Preview
@Composable
private fun IconCirclePreview() {
    DonaVidaTheme {
        IconCircle(
            icon = R.drawable.ic_favorite,
            size = 40.dp,
            background = MaterialTheme.colorScheme.surfaceContainer,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
