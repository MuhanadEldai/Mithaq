package com.muhanad.fteshly

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import com.muhanad.fteshly.ui.main.MainScreen
import com.muhanad.fteshly.ui.theme.FteshlyTheme
import com.muhanad.fteshly.util.AppUpdateManagerHelper

class MainActivity : ComponentActivity() {
    private lateinit var updateManagerHelper: AppUpdateManagerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("fattish_li_prefs", MODE_PRIVATE)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val systemDark = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        val savedDarkMode = prefs.getBoolean("dark_mode_pref", systemDark)

        // Define your exact colors here
        val lightBackgroundColor = Color.parseColor("#E5E4E4FF") // Your exact Hex color
        val darkBackgroundColor = Color.parseColor("#121212")  // Replace with your actual DarkBackground hex

        // Function to update system bars based on theme
        fun applySystemBarColors(isDark: Boolean) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(lightBackgroundColor, darkBackgroundColor),
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim = lightBackgroundColor,
                    darkScrim = darkBackgroundColor,
                    detectDarkMode = { isDark }
                )
            )
        }

        // Apply initially
        applySystemBarColors(savedDarkMode)

        updateManagerHelper = AppUpdateManagerHelper(this)
        updateManagerHelper.checkForAppUpdate()

        setContent {
            var darkTheme by remember { mutableStateOf(savedDarkMode) }

            // Update system bars immediately when user toggles the theme
            LaunchedEffect(darkTheme) {
                applySystemBarColors(darkTheme)
            }

            FteshlyTheme(darkTheme = darkTheme) {
                MainScreen(
                    darkTheme = darkTheme,
                    onThemeChanged = { newDarkTheme ->
                        darkTheme = newDarkTheme
                        prefs.edit().putBoolean("dark_mode_pref", newDarkTheme).apply()
                    }
                )
            }
        }
    }
}