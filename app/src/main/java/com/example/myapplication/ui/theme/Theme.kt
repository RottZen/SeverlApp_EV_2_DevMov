package com.example.severlapp_cenamad.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaNegroVerde = darkColorScheme(
    primary = VerdePrimario,
    onPrimary = SobreVerdePrimario,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = SobreVerdeContenedor,
    secondary = Secundario,
    onSecondary = SobreSecundario,
    secondaryContainer = ContenedorSecundario,
    onSecondaryContainer = SobreContenedorSecundario,
    tertiary = Terciario,
    onTertiary = SobreTerciario,
    tertiaryContainer = ContenedorTerciario,
    onTertiaryContainer = SobreContenedorTerciario,
    background = FondoNegro,
    onBackground = SobreFondo,
    surface = FondoNegro,
    onSurface = SobreFondo,
    surfaceVariant = VariantePanel,
    onSurfaceVariant = SobreVariantePanel,
    outline = Borde,
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerLow = Color(0xFF0A0F0A),
    surfaceContainer = Color(0xFF111811),
    surfaceContainerHigh = Color(0xFF151D16),
    surfaceContainerHighest = Color(0xFF18211A)
)

@Composable
fun SeverlApp_CENAMADTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaNegroVerde,
        typography = Typography,
        content = content
    )
}