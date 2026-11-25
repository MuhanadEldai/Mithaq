package com.muhanad.mithaq.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val adminPhone = "249998989362" // Admin WhatsApp number

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

        // About & Admin Contact Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "عن التطبيق وتواصل الإدارة", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "تطبيق ميثاق للسيارات المشلعة والمستعملة - المنصة الأولى لبيع وشراء السيارات وقطع الغيار في السودان.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$adminPhone?text=مرحباً، أردت الاستفسار بخصوص تطبيق ميثاق"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDCFCE7)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = Color(0xFF16A34A))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "تواصل مع الإدارة عبر واتساب", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
