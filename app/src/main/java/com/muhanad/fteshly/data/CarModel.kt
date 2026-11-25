package com.muhanad.fteshly.data

import androidx.compose.runtime.mutableStateListOf

data class UsedCar(
    val id: String,
    val title: String,
    val model: String,
    val price: String,
    val condition: CarCondition, // USED or DISMANTLED (مشلعة أو مستعملة)
    val location: String,
    val phone: String,
    val imageUrl: String,
    var isFavorite: Boolean = false
)

enum class CarCondition(val displayNameAr: String) {
    USED("مستعملة"),
    DISMANTLED("مشلعة")
}

object MockCarRepository {
    val cars = mutableStateListOf(
        UsedCar(
            "1",
            "تويوتا لاندكروزر V8",
            "2018",
            "45,000,000 ج.س",
            CarCondition.USED,
            "الخرطوم",
            "0912345678",
            "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=800&q=80",
            true
        ),
        UsedCar(
            "2",
            "هيونداي توسان (للبورصة / قطع غيار)",
            "2015",
            "12,000,000 ج.س",
            CarCondition.DISMANTLED,
            "أمدرمان",
            "0923456789",
            "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
            false
        ),
        UsedCar(
            "3",
            "نيسان باترول صفاري",
            "2014",
            "28,000,000 ج.س",
            CarCondition.USED,
            "بورتسودان",
            "0934567890",
            "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80",
            false
        ),
        UsedCar(
            "4",
            "تويوتا هايلكس مكينة ودفرنسات",
            "2016",
            "18,500,000 ج.س",
            CarCondition.DISMANTLED,
            "الولاية الشمالية",
            "0945678901",
            "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=800&q=80",
            true
        ),
        UsedCar(
            "5",
            "كيا اسبورتاج فل كامل",
            "2020",
            "32,000,000 ج.س",
            CarCondition.USED,
            "مدني",
            "0956789012",
            "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80",
            false
        )
    )

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
