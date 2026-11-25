package com.muhanad.mithaq.data

import androidx.compose.runtime.mutableStateListOf

data class UsedCar(
    val id: String,
    val name: String,
    val model: String,
    val price: String,
    val location: String,
    val phone: String,
    val imageUrl: String,
    val distance: String = "غير محدد",
    val gear: String = "أوتوماتيك",
    val fuel: String = "بنزين",
    var isFavorite: Boolean = false
)

object MockCarRepository {
    val cars = mutableStateListOf<UsedCar>()

    fun addCar(car: UsedCar) {
        cars.add(0, car)
    }

    fun toggleFavorite(carId: String) {
        val index = cars.indexOfFirst { it.id == carId }
        if (index != -1) {
            val current = cars[index]
            cars[index] = current.copy(isFavorite = !current.isFavorite)
        }
    }
}
