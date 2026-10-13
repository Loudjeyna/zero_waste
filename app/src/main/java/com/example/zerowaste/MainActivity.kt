package com.example.zerowaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.zerowaste.ui.screens.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            // 1. استدعاء الدالة في بيئة Composable أولاً لتجنب الأخطاء
            val systemDarkMode = isSystemInDarkTheme()

            // 2. حالة الوضع الداكن محفوظة بأمان
            var isDarkMode by rememberSaveable {
                mutableStateOf(systemDarkMode)
            }

            // 3. حالة اللغة الإنجليزية محفوظة أيضاً (افتراضياً العربية false)
            var isEnglish by rememberSaveable {
                mutableStateOf(false)
            }

            // تعريف ألوان الوضع الداكن
            val darkColorScheme = darkColorScheme(
                primary = Color(0xFF81C784),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF2E4A2F),
                onPrimaryContainer = Color(0xFFC8E6C9),
                secondary = Color(0xFF80CBC4),
                secondaryContainer = Color(0xFF37474F),
                onSecondaryContainer = Color(0xFFB2EBF2),
                tertiary = Color(0xFFFFAB91),
                tertiaryContainer = Color(0xFF4E342E),
                onTertiaryContainer = Color(0xFFFFCCBC),
                background = Color(0xFF121212),
                surface = Color(0xFF1E1E1E),
                onSurface = Color(0xFFE0E0E0),
                onSurfaceVariant = Color(0xFFBDBDBD),
                error = Color(0xFFEF5350),
                errorContainer = Color(0xFFB71C1C),
                onErrorContainer = Color(0xFFFFCDD2)
            )

            // تعريف ألوان الوضع الفاتح
            val lightColorScheme = lightColorScheme(
                primary = Color(0xFF2E7D32),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFC8E6C9),
                onPrimaryContainer = Color(0xFF1B5E20),
                secondary = Color(0xFF00897B),
                secondaryContainer = Color(0xFFB2DFDB),
                onSecondaryContainer = Color(0xFF004D40),
                tertiary = Color(0xFFFF7043),
                tertiaryContainer = Color(0xFFFFE0B2),
                onTertiaryContainer = Color(0xFFE65100),
                background = Color(0xFFFFFBFE),
                surface = Color(0xFFFFFBFE),
                onSurface = Color(0xFF1C1B1F),
                onSurfaceVariant = Color(0xFF49454F),
                error = Color(0xFFD32F2F),
                errorContainer = Color(0xFFFFCDD2),
                onErrorContainer = Color(0xFFB71C1C)
            )

            MaterialTheme(
                colorScheme = if (isDarkMode) darkColorScheme else lightColorScheme,
                content = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // تمرير الحالة ودالة التبديل إلى HomeScreen
                        HomeScreen(
                            isDarkMode = isDarkMode,
                            onToggleTheme = { isDarkMode = !isDarkMode },
                            isEnglish = isEnglish,
                            onToggleLanguage = { isEnglish = !isEnglish }
                        )
                    }
                }
            )
        }
    }
}