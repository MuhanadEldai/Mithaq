package com.muhanad.fteshly.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhanad.fteshly.data.MockCarRepository
import com.muhanad.fteshly.data.UsedCar

@Composable
fun FavoritesScreen(
    onCarClick: (UsedCar) -> Unit
) {
    var selectedCar by remember { mutableStateOf<UsedCar?>(null) }
    val favoriteCars = MockCarRepository.cars.filter { it.isFavorite }

    if (selectedCar != null) {
        CarDetailsScreen(
            car = selectedCar!!,
            onBackClick = { selectedCar = null }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "السيارات المفضلة",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        if (favoriteCars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "لا توجد سيارات في المفضلة حالياً",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 160.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteCars) { car ->
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
