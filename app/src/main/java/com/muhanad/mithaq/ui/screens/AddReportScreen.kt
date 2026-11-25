package com.muhanad.mithaq.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddReportScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Icon(
            imageVector = Icons.Default.Report,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "الإبلاغ عن سيارة مفقودة",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "تطبيق فتش لي هو منصة بحث قراءة فقط (Read-Only). لإضافة بلاغ جديد عن سيارة مفقودة، يرجى التواصل عبر البريد الإلكتروني أو الاتصال بالجهات المختصة.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "طريقة الإبلاغ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "• إرسال تفاصيل السيارة ورقم الشاسيه (VIN) إلى فريق الدعم.\n• التواصل عبر البريد: support@fattishli.sd\n• سيتم إدراج بلاغك في قاعدة البيانات بعد التحقق.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@fattishli.sd")
                    putExtra(Intent.EXTRA_SUBJECT, "بلاغ سيارة مفقودة - Missing Car Report")
                }
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Handle exception if no email app
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(26.dp)
        ) {
            Icon(Icons.Default.Email, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "تواصل للإبلاغ عبر البريد", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
