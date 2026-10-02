package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = DarkBg,
    primaryContainer = BeadBrownDark,
    onPrimaryContainer = GoldLight,
    secondary = GoldPrimary,
    onSecondary = Color.Black,
    secondaryContainer = DarkCard,
    onSecondaryContainer = DarkTextInk,
    tertiary = AccentGreen,
    onTertiary = Color.White,
    background = DarkBg,
    onBackground = DarkTextInk,
    surface = DarkSurface,
    onSurface = DarkTextInk,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkTextMuted,
    outline = DarkLine,
    error = AccentRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BeadBrown,
    onPrimary = Color.White,
    primaryContainer = WarmCardBeige,
    onPrimaryContainer = BeadBrownDark,
    secondary = GoldPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFBEFD3),
    onSecondaryContainer = BeadBrownDark,
    tertiary = AccentGreen,
    onTertiary = Color.White,
    background = WarmBgLight,
    onBackground = TextInk,
    surface = WarmSurfaceLight,
    onSurface = TextInk,
    surfaceVariant = WarmCardBeige,
    onSurfaceVariant = TextMuted,
    outline = WarmLine,
    error = AccentRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
