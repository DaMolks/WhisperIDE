package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

// Définition des couleurs pour chaque thème
data class WhisperColorScheme(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color
)

// Thème sombre par défaut
val DarkColorScheme = WhisperColorScheme(
    primary = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

// Thème clair
val LightColorScheme = WhisperColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color(0xFFFAFAFA),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Gestionnaire de thème
class WhisperThemeState(
    initialTheme: WhisperColorScheme = DarkColorScheme
) {
    var currentTheme = initialTheme
        private set

    fun toggleTheme() {
        currentTheme = if (currentTheme == DarkColorScheme) LightColorScheme else DarkColorScheme
    }
}

// Composition locale pour le thème
val LocalWhisperTheme = compositionLocalOf { DarkColorScheme }

@Composable
fun WhisperTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = remember { 
        if (darkTheme) DarkColorScheme else LightColorScheme 
    }

    androidx.compose.material.MaterialTheme(
        colors = androidx.compose.material.darkColors(
            primary = colorScheme.primary,
            secondary = colorScheme.secondary,
            background = colorScheme.background,
            surface = colorScheme.surface
        ),
        content = content
    )
}