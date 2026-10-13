package com.example.zerowaste.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ForestGreen = Color(0xFF26734D)
private val DeepForest = Color(0xFF174B34)
private val SoftGreen = Color(0xFFDCEFE2)
private val Mint = Color(0xFFB9DEC8)
private val Cream = Color(0xFFF7F8F2)
private val Ink = Color(0xFF1B2921)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = SoftGreen,
    onPrimaryContainer = DeepForest,
    secondary = Color(0xFF557568),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0EEE6),
    onSecondaryContainer = Color(0xFF1C382B),
    tertiary = Color(0xFF8A641C),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE8B4),
    onTertiaryContainer = Color(0xFF4C3700),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    background = Cream,
    onBackground = Ink,
    surface = Color(0xFFFFFFFF),
    onSurface = Ink,
    surfaceVariant = Color(0xFFE9EEE8),
    onSurfaceVariant = Color(0xFF4A5B50),
    outline = Color(0xFF7A897E)
)

private val DarkColorScheme = darkColorScheme(
    primary = Mint,
    onPrimary = Color(0xFF003920),
    primaryContainer = DeepForest,
    onPrimaryContainer = SoftGreen,
    secondary = Color(0xFFB9D5C5),
    onSecondary = Color(0xFF243B2E),
    secondaryContainer = Color(0xFF354D3F),
    onSecondaryContainer = Color(0xFFD5EBDD),
    tertiary = Color(0xFFE7C477),
    onTertiary = Color(0xFF402D00),
    tertiaryContainer = Color(0xFF5B4510),
    onTertiaryContainer = Color(0xFFFFE8B4),
    background = Color(0xFF101812),
    onBackground = Color(0xFFE1E9E0),
    surface = Color(0xFF18221B),
    onSurface = Color(0xFFE1E9E0),
    surfaceVariant = Color(0xFF344239),
    onSurfaceVariant = Color(0xFFC0CEC2)
)

@Composable
fun ZerowasteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) androidx.compose.material3.dynamicDarkColorScheme(context)
            else androidx.compose.material3.dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
