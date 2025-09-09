package com.example.offerkotlin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF009688), // Primary color
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB2DFDB), // Primary color
    onPrimary = Color(0xFFFFFFFF), // Text / Icons
    primaryContainer = Color(0xFFB2DFDB), // Light primary color
    secondary = Color(0xFF607D8B), // Accent color
    // onSecondary is not specified in your palette, so it will fall back to a default.
    // secondaryContainer and tertiary colors are not specified.
    background = Color(0xFFFFFFFF), // A good default for a light background
    onBackground = Color(0xFF212121), // Primary text
    surface = Color(0xFFFFFFFF), // A good default for a light surface
    onSurface = Color(0xFF212121), // Primary text
    surfaceVariant = Color(0xFFBDBDBD), // Divider color
    onSurfaceVariant = Color(0xFF757575) // Secondary text
)

@Composable
fun OfferKotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}