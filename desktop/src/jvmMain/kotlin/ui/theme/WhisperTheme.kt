package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

// Couleurs modernes pour le thème clair
private val LightColors = lightColorScheme(
    primary = Color(0xFF006D40),          // Vert émeraude professionnel
    onPrimary = Color.White,
    primaryContainer = Color(0xFF98F7B6),  // Vert pastel pour les conteneurs
    onPrimaryContainer = Color(0xFF002111),
    secondary = Color(0xFF4355B9),         // Bleu professionnel
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDEE0FF), // Bleu pastel
    onSecondaryContainer = Color(0xFF00105C),
    tertiary = Color(0xFF006780),          // Cyan accent
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB8EAFF),
    onTertiaryContainer = Color(0xFF001F28),
    background = Color(0xFFF8FDFF),        // Blanc légèrement bleué
    onBackground = Color(0xFF001F2A),      // Presque noir
    surface = Color(0xFFF8FDFF),
    onSurface = Color(0xFF001F2A),
    surfaceVariant = Color(0xFFDCE5DB),    // Gris très clair
    onSurfaceVariant = Color(0xFF414942)
)

// Couleurs modernes pour le thème sombre
private val DarkColors = darkColorScheme(
    primary = Color(0xFF7CDA9B),          // Vert lumineux
    onPrimary = Color(0xFF003921),
    primaryContainer = Color(0xFF005231),  // Vert foncé
    onPrimaryContainer = Color(0xFF98F7B6),
    secondary = Color(0xFFB9C3FF),         // Bleu lumineux
    onSecondary = Color(0xFF002196),
    secondaryContainer = Color(0xFF2A3C9F), // Bleu foncé
    onSecondaryContainer = Color(0xFFDEE0FF),
    tertiary = Color(0xFF5DD5FC),          // Cyan lumineux
    onTertiary = Color(0xFF003544),
    tertiaryContainer = Color(0xFF004D61),  // Cyan foncé
    onTertiaryContainer = Color(0xFFB8EAFF),
    background = Color(0xFF001F2A),         // Bleu très foncé
    onBackground = Color(0xFFE1E3E3),      // Gris très clair
    surface = Color(0xFF001F2A),
    onSurface = Color(0xFFE1E3E3),
    surfaceVariant = Color(0xFF404943),     // Gris foncé
    onSurfaceVariant = Color(0xFFC0C9C0)
)

@Composable
fun WhisperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private val Typography = Typography()  // Using default Material3 typography