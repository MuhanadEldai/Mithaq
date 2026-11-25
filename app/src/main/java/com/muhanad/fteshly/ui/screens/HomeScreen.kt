package com.muhanad.fteshly.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhanad.fteshly.data.CarCondition
import com.muhanad.fteshly.data.CarRepository
import com.muhanad.fteshly.data.MockCarRepository
import com.muhanad.fteshly.data.UsedCar
import com.muhanad.fteshly.util.NetworkUtils
import com.muhanad.fteshly.util.ShimmerImagePlaceholder
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onCarClick: (UsedCar) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { CarRepository() }

    var isConnected by remember { mutableStateOf(NetworkUtils.isNetworkAvailable(context)) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<UsedCar>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }
    var selectedCar by remember { mutableStateOf<UsedCar?>(null) }

    if (selectedCar != null) {
        CarDetailsScreen(
            car = selectedCar!!,
            onBackClick = { selectedCar = null }
        )
        return
    }

    fun performSearch(query: String) {
        if (query.isBlank()) return
        scope.launch {
            isLoading = true
            hasSearched = true
            searchResults = repository.searchCarsByName(query)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Network Offline & Retry Banner
        if (!isConnected) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "لا توجد صلة بالإنترنت",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = { isConnected = NetworkUtils.isNetworkAvailable(context) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("إعادة المحاولة")
                    }
                }
            }
        }

        // Search Bar (Search by name e.g. Honda, Toyota...)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("ابحث بالاسم، مثل: هوندا، تويوتا، توسان...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            searchResults = emptyList()
                            hasSearched = false
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                    IconButton(onClick = { performSearch(searchQuery) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(26.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { performSearch(searchQuery) })
        )

        // Content Area: Empty State on Start vs Loading vs Results
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (!hasSearched) {
            // Initial State: Do not fetch and show all cars on start
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "ابحث عن سيارتك أو قطع الغيار المطلوبة",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "أدخل اسم الماركة أو السيارة في حقل البحث بالأعلى (مثل: هوندا، تويوتا، لاندكروزر) للعثور على النتائج المتاحة في قاعدة بيانات ميثاق.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "لم يتم العثور على سيارات مطابقة لبحثك.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Search Results List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 160.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(searchResults) { car ->
                    CompactCarCard(
                        car = car,
                        onCardClick = { onCarClick(car) },
                        onFavoriteClick = { MockCarRepository.toggleFavorite(car.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CompactCarCard(
    car: UsedCar,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val conditionColor = if (car.condition == CarCondition.USED) Color(0xFF22C55E) else Color(0xFFF97316)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail Image using ShimmerImagePlaceholder
            ShimmerImagePlaceholder(
                imageUrl = car.imageUrl,
                contentDescription = car.title,
                modifier = Modifier.size(90.dp),
                cornerRadius = 20.dp
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ConditionBadge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = conditionColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = car.condition.displayNameAr,
                            color = conditionColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    // Favorite Button
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (car.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (car.isFavorite) Color.Red else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = car.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = car.model, fontSize = 12.sp, color = Color.Gray)
                    Text(text = "•", fontSize = 12.sp, color = Color.Gray)
                    Text(text = car.location, fontSize = 12.sp, color = Color.Gray)
                }

                Text(
                    text = car.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
