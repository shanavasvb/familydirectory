package com.example.familydirectory.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlueDark,

    secondary = SecondaryBlue,
    onSecondary = Color.White,
    secondaryContainer = SurfaceBlue,
    onSecondaryContainer = SecondaryBlueDark,

    tertiary = AccentCyan,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB2EBF2),
    onTertiaryContainer = Color(0xFF006064),

    background = BackgroundLight,
    onBackground = TextPrimary,

    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceBlue,
    onSurfaceVariant = TextSecondary,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),

    outline = DividerBlue,
    outlineVariant = Color(0xFFE1F5FE)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = PrimaryBlueDark,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueLight,

    secondary = SecondaryBlueLight,
    onSecondary = SecondaryBlueDark,
    secondaryContainer = SecondaryBlueDark,
    onSecondaryContainer = SecondaryBlueLight,

    tertiary = AccentCyan,
    onTertiary = Color(0xFF00363A),
    tertiaryContainer = Color(0xFF004D52),
    onTertiaryContainer = Color(0xFFB2EBF2),

    background = Color(0xFF0D1B2A),
    onBackground = Color(0xFFE1F5FE),

    surface = Color(0xFF1B263B),
    onSurface = Color(0xFFE1F5FE),
    surfaceVariant = Color(0xFF415A77),
    onSurfaceVariant = Color(0xFFB0BEC5),

    error = Color(0xFFFF6B6B),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF778DA9),
    outlineVariant = Color(0xFF415A77)
)

@Composable
fun FamilydirectoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to use custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}