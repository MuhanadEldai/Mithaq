package com.muhanad.fteshly.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    darkTheme: Boolean,
    onThemeChanged: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "الإعدادات",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Language Setting
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text(text = "Language / اللغة", fontWeight = FontWeight.Bold)
                            Text(text = "العربية / English", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                }

                HorizontalDivider()

                // Dark Mode Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Brightness4, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text(text = "الوضع الداكن / Dark Mode", fontWeight = FontWeight.Bold)
                            Text(text = if (darkTheme) "الوضع الداكن مفعل" else "الوضع الفاتح مفعل", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Switch(
                        checked = darkTheme,
                        onCheckedChange = onThemeChanged
                    )
                }

                HorizontalDivider()

                // Notifications Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column {
                            Text(text = "الإشعارات", fontWeight = FontWeight.Bold)
                            Text(text = "استقبال إشعارات البلاغات الجديدة", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = onNotificationsChanged
                    )
                }
            }
        }
    }
}
