package com.muhanad.fteshly.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhanad.fteshly.data.CarCondition
import com.muhanad.fteshly.data.MockCarRepository
import com.muhanad.fteshly.data.UsedCar
import java.util.UUID

@Composable
fun AddCarScreen(
    onCarAdded: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf(CarCondition.USED) }
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.AddBox, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Text(text = "إضافة سيارة للبيع (مستعملة أو مشلعة)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("اسم السيارة / نوع قطع الغيار") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("الموديل (مثال: 2018)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("السعر (مثال: 35,000,000 ج.س)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("الموقع / المدينة (مثال: الخرطوم)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الواتساب للتواصل (مثال: 0912345678)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                // Condition Selection
                Text(text = "حالة السيارة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = condition == CarCondition.USED,
                            onClick = { condition = CarCondition.USED }
                        )
                        Text(text = "مستعملة")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = condition == CarCondition.DISMANTLED,
                            onClick = { condition = CarCondition.DISMANTLED }
                        )
                        Text(text = "مشلعة (قطع غيار)")
                    }
                }
            }
        }

        if (showSuccess) {
            Text(
                text = "تمت إضافة السيارة بنجاح!",
                color = Color(0xFF22C55E),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (title.isNotBlank() && price.isNotBlank() && phone.isNotBlank()) {
                    val newCar = UsedCar(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        model = model.ifBlank { "غير محدد" },
                        price = price,
                        condition = condition,
                        location = location.ifBlank { "السودان" },
                        phone = phone,
                        imageUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
                        isFavorite = false
                    )
                    MockCarRepository.addCar(newCar)
                    showSuccess = true
                    title = ""
                    model = ""
                    price = ""
                    location = ""
                    phone = ""
                    onCarAdded()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp)
        ) {
            Icon(Icons.Default.AddBox, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "نشر الإعلان", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
