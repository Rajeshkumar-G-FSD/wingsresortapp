package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ResortGold,
    onPrimary = ResortTealDark,
    primaryContainer = ResortTealPrimary,
    onPrimaryContainer = ResortGoldLight,
    secondary = ResortTealAccent,
    onSecondary = Color.White,
    secondaryContainer = ResortTealDeep,
    onSecondaryContainer = ResortTealLight,
    tertiary = CoralAccent,
    onTertiary = Color.White,
    background = ResortTealDark,
    onBackground = TextPrimaryDark,
    surface = ResortSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = ResortCardDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = CardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = ResortTealPrimary,
    onPrimary = Color.White,
    primaryContainer = ResortTealLight,
    onPrimaryContainer = ResortTealDark,
    secondary = ResortGoldDark,
    onSecondary = Color.White,
    secondaryContainer = ResortGoldLight,
    onSecondaryContainer = ResortTealDark,
    tertiary = CoralAccent,
    onTertiary = Color.White,
    background = ResortSand,
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = ResortTealLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = ResortGold.copy(alpha = 0.3f)
)

@Composable
fun WingsResortTheme(
    darkTheme: Boolean = true, // Luxury dark theme default for resort ambiance
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
