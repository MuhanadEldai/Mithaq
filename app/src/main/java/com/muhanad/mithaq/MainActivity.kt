package com.muhanad.mithaq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.muhanad.mithaq.data.MockCarRepository
import com.muhanad.mithaq.ui.main.MainScreen
import com.muhanad.mithaq.ui.theme.FteshlyTheme
import com.muhanad.mithaq.util.AppUpdateManagerHelper
import com.muhanad.mithaq.util.InAppReviewHelper

class MainActivity : ComponentActivity() {
    private lateinit var updateManagerHelper: AppUpdateManagerHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Notification permission granted or denied handling
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MockCarRepository with context for persistent favorites
        MockCarRepository.init(this)

        // Request POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val prefs = getSharedPreferences("fattish_li_prefs", MODE_PRIVATE)
        // Default to false (Light Mode) on first time open
        val savedDarkMode = prefs.getBoolean("dark_mode_pref", false)

        val lightBackgroundColor = "#E5E4E4".toColorInt()
        val darkBackgroundColor = "#121212".toColorInt()

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

        applySystemBarColors(savedDarkMode)

        updateManagerHelper = AppUpdateManagerHelper(this)
        updateManagerHelper.checkForAppUpdate()

        // Trigger In-App Review on the 2nd app launch
        InAppReviewHelper.checkAndLaunchReview(this)

        setContent {
            var darkTheme by remember { mutableStateOf(savedDarkMode) }

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
