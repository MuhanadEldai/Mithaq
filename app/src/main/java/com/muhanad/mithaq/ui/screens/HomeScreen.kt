package com.muhanad.mithaq.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhanad.mithaq.R
import com.muhanad.mithaq.data.CarRepository
import com.muhanad.mithaq.data.MockCarRepository
import com.muhanad.mithaq.data.UsedCar
import com.muhanad.mithaq.util.NetworkUtils
import com.muhanad.mithaq.util.ShimmerImagePlaceholder
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    searchResults: List<UsedCar>,
    onSearchResultsChanged: (List<UsedCar>) -> Unit,
    hasSearched: Boolean,
    onHasSearchedChanged: (Boolean) -> Unit,
    onCarClick: (UsedCar) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { CarRepository() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isConnected by remember { mutableStateOf(NetworkUtils.isNetworkAvailable(context)) }
    var isLoading by remember { mutableStateOf(false) }
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
        keyboardController?.hide()
        scope.launch {
            isLoading = true
            onHasSearchedChanged(true)
            val results = repository.searchCarsByName(query)
            onSearchResultsChanged(results)
            isLoading = false
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 160.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Methaq Header Banner Image (Collapses and scrolls up on scroll)
        item {
            Image(
                painter = painterResource(id = R.drawable.methaqheader),
                contentDescription = "Methaq Header",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(bottomStart = 26.dp, bottomEnd = 26.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Network Offline & Retry Banner
        if (!isConnected) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
        }

        // Header & Search Section
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Info
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "مرحباً بك في ميثاق",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ابحث عن سيارتك المستعملة بكل سهولة",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
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
                                    onSearchQueryChanged("")
                                    keyboardController?.hide()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(26.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { performSearch(searchQuery) })
                )

                // Dedicated Search Button below the edit text
                Button(
                    onClick = { performSearch(searchQuery) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "بحث", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Loading Progress Bar during search
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Content Area: Empty State on Start vs Loading vs Results
        if (isLoading && searchResults.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        } else if (!hasSearched) {
            // Initial State
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "ابحث عن سيارتك المطلوبة",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "أدخل اسم الماركة أو السيارة في حقل البحث بالأعلى (مثل: هوندا، تويوتا، لاندكروزر) واضغط بحث للعثور على النتائج المتاحة في قاعدة بيانات ميثاق.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else if (searchResults.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لم يتم العثور على سيارات مطابقة لبحثك.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Search Results List with padding
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            items(searchResults) { car ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                contentDescription = car.name,
                modifier = Modifier.size(90.dp),
                cornerRadius = 20.dp
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = car.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "الموديل: ${car.model}", fontSize = 12.sp, color = Color.Gray)
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
