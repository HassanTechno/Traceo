package com.hassantechno.traceo.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val TraceoBackground = Color(0xFF07111F)
val TraceoBackgroundTop = Color(0xFF10243A)
val TraceoBackgroundBottom = Color(0xFF050B13)
val TraceoSurface = Color(0xFF111E2D)
val TraceoSurfaceVariant = Color(0xFF17293B)
val TraceoAccent = Color(0xFF079CFF)
val TraceoCyan = Color(0xFF00D4FF)
val TraceoMuted = Color(0xFFA5B7CC)
val TraceoText = Color(0xFFF5F8FF)
val TraceoOutline = Color(0xFF34485D)
val TraceoSuccess = Color(0xFF00C66A)
val TraceoWarning = Color(0xFFFFB020)
val TraceoDanger = Color(0xFFFF8585)

private val TraceoColorScheme = darkColorScheme(
    primary = TraceoAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0C3A5C),
    onPrimaryContainer = TraceoText,
    secondary = TraceoCyan,
    onSecondary = Color(0xFF00202B),
    secondaryContainer = Color(0xFF12384A),
    onSecondaryContainer = TraceoText,
    background = TraceoBackground,
    onBackground = TraceoText,
    surface = TraceoSurface,
    onSurface = TraceoText,
    surfaceVariant = TraceoSurfaceVariant,
    onSurfaceVariant = TraceoMuted,
    outline = TraceoOutline,
    error = TraceoDanger,
    onError = Color.White,
)

private val TraceoTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 21.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
)

private val TraceoShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun TraceoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TraceoColorScheme,
        typography = TraceoTypography,
        shapes = TraceoShapes,
        content = content,
    )
}
