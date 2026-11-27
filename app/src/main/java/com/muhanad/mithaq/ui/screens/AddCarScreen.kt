package com.muhanad.mithaq.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddCarScreen(
    onCarAdded: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var gear by remember { mutableStateOf("أوتوماتيك") }
    var fuel by remember { mutableStateOf("بنزين") }
    var phone by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 140.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = "إضافة سيارة مباشرة إلى قاعدة البيانات", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { 
                            name = it
                            validationError = null 
                        },
                        label = { Text("اسم السيارة (مطلوب)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("الموديل / سنة الصنع (مثال: 2018)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { 
                            price = it
                            validationError = null
                        },
                        label = { Text("السعر (مطلوب - مثال: 35,000,000 ج.س)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = distance,
                        onValueChange = { distance = it },
                        label = { Text("الممشى (مثال: 25,000 كم)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true
                    )

                    // Gear type selection
                    Text(text = "ناقل الحركة:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = gear == "أوتوماتيك",
                                onClick = { gear = "أوتوماتيك" }
                            )
                            Text(text = "أوتوماتيك")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = gear == "عادي",
                                onClick = { gear = "عادي" }
                            )
                            Text(text = "عادي")
                        }
                    }

                    // Fuel type selection
                    Text(text = "نوع الوقود:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = fuel == "بنزين",
                                onClick = { fuel = "بنزين" }
                            )
                            Text(text = "بنزين")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = fuel == "جاز",
                                onClick = { fuel = "جاز" }
                            )
                            Text(text = "جاز")
                        }
                    }

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
                        onValueChange = { 
                            phone = it
                            validationError = null
                        },
                        label = { Text("رقم الواتساب للتواصل (مطلوب)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }
        }

        if (validationError != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = validationError!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (errorMessage != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "خطأ في الحفظ: $errorMessage",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (showSuccess) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "تمت إضافة السيارة إلى قاعدة البيانات بنجاح! انتقل للرئيسية وابحث بالاسم لعرضها.",
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    validationError = null
                    errorMessage = null
                    showSuccess = false

                    if (name.isBlank() || price.isBlank() || phone.isBlank()) {
                        validationError = "يرجى تعبئة الحقول المطلوبة: (اسم السيارة، السعر، رقم الهاتف)"
                        return@Button
                    }

                    isSubmitting = true
                    val carData = hashMapOf(
                        "n" to name.trim(),
                        "mod" to model.ifBlank { "2022" }.trim(),
                        "pr" to price.trim(),
                        "loc" to location.ifBlank { "الخرطوم" }.trim(),
                        "ph" to phone.trim(),
                        "img" to listOf(
                            "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=800&q=80",
                            "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
                            "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80"
                        ),
                        "dis" to distance.ifBlank { "25,000 كم" }.trim(),
                        "gear" to gear,
                        "fuel" to fuel
                    )

                    try {
                        FirebaseFirestore.getInstance()
                            .collection("cars")
                            .add(carData)
                            .addOnSuccessListener {
                                isSubmitting = false
                                showSuccess = true
                                name = ""
                                model = ""
                                price = ""
                                location = ""
                                phone = ""
                                distance = ""
                                onCarAdded()
                            }
                            .addOnFailureListener { e ->
                                isSubmitting = false
                                errorMessage = e.localizedMessage ?: "فشل الاتصال بقاعدة البيانات"
                            }
                    } catch (e: Exception) {
                        isSubmitting = false
                        errorMessage = e.localizedMessage ?: "حدث خطأ غير متوقع"
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.AddBox, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "حفظ وإضافة مباشرة في Firestore", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
