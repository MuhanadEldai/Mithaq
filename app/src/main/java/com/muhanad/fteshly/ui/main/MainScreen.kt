package com.muhanad.fteshly.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhanad.fteshly.data.UsedCar
import com.muhanad.fteshly.ui.screens.AddCarScreen
import com.muhanad.fteshly.ui.screens.CarDetailsScreen
import com.muhanad.fteshly.ui.screens.FavoritesScreen
import com.muhanad.fteshly.ui.screens.HomeScreen
import com.muhanad.fteshly.ui.screens.SettingsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "الرئيسية", Icons.Default.Home)
    object Favorites : Screen("favorites", "المفضلة", Icons.Default.Favorite)
    object AddCar : Screen("add_car", "إضافة سيارة", Icons.Default.AddBox)
    object Settings : Screen("settings", "الإعدادات", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    darkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val navController = remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedCar by remember { mutableStateOf<UsedCar?>(null) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    // Reset selectedCar when switching tabs
    LaunchedEffect(navController.value) {
        selectedCar = null
    }

    // Handle system back button to return to list or Home tab
    BackHandler(enabled = selectedCar != null || navController.value != Screen.Home) {
        if (selectedCar != null) {
            selectedCar = null
        } else {
            navController.value = Screen.Home
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (selectedCar != null) {
                            Text(
                                text = "تفاصيل السيارة",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        } else {
                            Icon(
                                imageVector = navController.value.icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(
                                text = navController.value.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (selectedCar != null) {
                        IconButton(onClick = { selectedCar = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (selectedCar == null) {
                        IconButton(onClick = {
                            notificationsEnabled = !notificationsEnabled
                        }) {
                            Icon(
                                imageVector = if (notificationsEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (selectedCar == null) {
                // Floating semi-transparent bottom navigation bar with enhanced shadow & sliding oval
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .height(68.dp)
                            .fillMaxWidth()
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(32.dp),
                                clip = false,
                                ambientColor = Color.Black.copy(alpha = 0.3f),
                                spotColor = Color.Black.copy(alpha = 0.3f)
                            )
                            .clip(RoundedCornerShape(32.dp)),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        tonalElevation = 0.dp
                    ) {
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            val items = listOf(Screen.Home, Screen.Favorites, Screen.AddCar, Screen.Settings)
                            val selectedIndex = items.indexOf(navController.value)

                            val horizontalPadding = 8.dp
                            val availableWidth = maxWidth - (horizontalPadding * 2)
                            val tabWidth = availableWidth / items.size
                            val ovalWidth = tabWidth - 8.dp
                            val ovalHeight = 52.dp
                            val verticalOffset = 8.dp

                            val indicatorOffset by animateDpAsState(
                                targetValue = horizontalPadding +
                                        (tabWidth * selectedIndex) +
                                        ((tabWidth - ovalWidth) / 2),
                                animationSpec = tween(durationMillis = 300),
                                label = "indicatorOffset"
                            )

                            // Sliding Selection Oval Pill Background
                            Box(
                                modifier = Modifier
                                    .offset(x = indicatorOffset, y = verticalOffset)
                                    .width(ovalWidth)
                                    .height(ovalHeight)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                            )

                            // Row of clickable tab items on top
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = horizontalPadding),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items.forEach { screen ->
                                    val selected = navController.value == screen
                                    val tint by animateColorAsState(
                                        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        animationSpec = tween(durationMillis = 300),
                                        label = "tint"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(ovalHeight)
                                            .clip(RoundedCornerShape(26.dp))
                                            .clickable { navController.value = screen },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = screen.icon,
                                                contentDescription = screen.title,
                                                tint = tint,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.height(1.dp))
                                            Text(
                                                text = screen.title,
                                                fontSize = 10.sp,
                                                color = tint,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateLeftPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateRightPadding(LayoutDirection.Ltr)
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (selectedCar != null) {
                CarDetailsScreen(
                    car = selectedCar!!,
                    onBackClick = { selectedCar = null }
                )
            } else {
                Crossfade(
                    targetState = navController.value,
                    animationSpec = tween(300),
                    label = "screenCrossfade"
                ) { screen ->
                    when (screen) {
                        is Screen.Home -> HomeScreen(onCarClick = { selectedCar = it })
                        is Screen.Favorites -> FavoritesScreen(onCarClick = { selectedCar = it })
                        is Screen.AddCar -> AddCarScreen(
                            onCarAdded = { navController.value = Screen.Home }
                        )
                        is Screen.Settings -> SettingsScreen(
                            darkTheme = darkTheme,
                            onThemeChanged = onThemeChanged,
                            notificationsEnabled = notificationsEnabled,
                            onNotificationsChanged = { notificationsEnabled = it }
                        )
                    }
                }
            }

            if (selectedCar == null) {
                // Telegram-style Frosted Glass Gradient Overlay behind the Floating Bottom Bar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(170.dp) // <-- 1. INCREASE HEIGHT HERE
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.9f), // <-- 2. MIDDLE TRANSPARENCY (lower = more transparent)
                                    MaterialTheme.colorScheme.background.copy(alpha = 1f)  // <-- 3. BOTTOM OPACITY (higher = more solid)
                                )
                            )
                        )
                )
            }
        }
    }
}
