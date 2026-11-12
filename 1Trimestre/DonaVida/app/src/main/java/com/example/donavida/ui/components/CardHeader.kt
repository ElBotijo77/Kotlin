package com.example.donavida.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donavida.R
import com.example.donavida.ui.theme.DonaVidaTheme
import com.example.donavida.ui.theme.Spacing

/**
 * Cabecera de tarjeta: icono de 20 dp y título en labelLarge, ambos en primary, con un hueco
 * opcional a la derecha ([trailing]) para un badge o similar.
 */
@Composable
fun CardHeader(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(Spacing.sm))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
        )
        if (trailing != null) {
            Spacer(Modifier.width(Spacing.sm))
            trailing()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardHeaderPreview() {
    DonaVidaTheme {
        CardHeader(
            icon = R.drawable.ic_schedule_filled,
            title = "Horario ininterrumpido",
            modifier = Modifier.padding(Spacing.md),
            trailing = {
                StatusBadge(
                    text = "Atención continua",
                    containerColor = DonaVidaTheme.extendedColors.tertiaryFixed,
                    contentColor = DonaVidaTheme.extendedColors.onTertiaryFixed,
                )
            },
        )
    }
}
