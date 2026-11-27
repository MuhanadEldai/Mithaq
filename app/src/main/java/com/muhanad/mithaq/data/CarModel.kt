package com.muhanad.mithaq.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore

data class UsedCar(
    val id: String,
    val name: String,
    val model: String,
    val price: String,
    val location: String,
    val phone: String,
    val imageUrls: List<String>,
    val distance: String = "غير محدد",
    val gear: String = "أوتوماتيك",
    val fuel: String = "بنزين",
    var isFavorite: Boolean = false
) {
    val imageUrl: String
        get() = imageUrls.firstOrNull() ?: "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
}

object MockCarRepository {
    val cars = mutableStateListOf<UsedCar>()
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("mithaq_favs_prefs", Context.MODE_PRIVATE)
            loadFavorites()
            fetchAllCarsFromFirestore()
        }
    }

    private fun loadFavorites() {
        val favIds = prefs?.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        cars.forEach { car ->
            if (favIds.contains(car.id)) {
                car.isFavorite = true
            }
        }
    }

    private fun saveFavorites() {
        val favIds = cars.filter { it.isFavorite }.map { it.id }.toSet()
        prefs?.edit()?.putStringSet("favorite_ids", favIds)?.apply()
    }

    private fun fetchAllCarsFromFirestore() {
        try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("cars").get().addOnSuccessListener { snapshot ->
                val favIds = prefs?.getStringSet("favorite_ids", emptySet()) ?: emptySet()
                snapshot.documents.forEach { doc ->
                    val rawName = doc.get("n") ?: doc.get("name") ?: doc.get("title") ?: "سيارة"
                    val rawModel = doc.get("mod") ?: doc.get("model") ?: "2022"
                    val rawPrice = doc.get("pr") ?: doc.get("price") ?: "0 ج.س"
                    val rawLocation = doc.get("loc") ?: doc.get("location") ?: "السودان"
                    val rawPhone = doc.get("ph") ?: doc.get("phone") ?: "0912345678"
                    
                    val imgField = doc.get("img") ?: doc.get("images") ?: doc.get("imageUrl")
                    val imageUrls: List<String> = when (imgField) {
                        is List<*> -> imgField.mapNotNull { it?.toString() }.filter { it.isNotBlank() }
                        is String -> if (imgField.contains(",")) imgField.split(",").map { it.trim() } else if (imgField.isNotBlank()) listOf(imgField) else emptyList()
                        else -> emptyList()
                    }.ifEmpty {
                        listOf("https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
                        )
                    }

                    val rawDis = doc.get("dis") ?: doc.get("distance") ?: "25,000 كم"
                    val rawGear = doc.get("gear") ?: doc.get("gearType") ?: "أوتوماتيك"
                    val rawFuel = doc.get("fuel") ?: doc.get("fuelType") ?: "بنزين"

                    val car = UsedCar(
                        id = doc.id,
                        name = rawName.toString(),
                        model = rawModel.toString(),
                        price = rawPrice.toString(),
                        location = rawLocation.toString(),
                        phone = rawPhone.toString(),
                        imageUrls = imageUrls,
                        distance = rawDis.toString(),
                        gear = rawGear.toString(),
                        fuel = rawFuel.toString(),
                        isFavorite = favIds.contains(doc.id)
                    )

                    if (cars.none { it.id == car.id }) {
                        cars.add(car)
                    } else {
                        val index = cars.indexOfFirst { it.id == car.id }
                        if (index != -1 && favIds.contains(car.id)) {
                            cars[index] = cars[index].copy(isFavorite = true)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore if offline
        }
    }

    fun addCar(car: UsedCar) {
        if (cars.none { it.id == car.id }) {
            val favIds = prefs?.getStringSet("favorite_ids", emptySet()) ?: emptySet()
            cars.add(0, car.copy(isFavorite = favIds.contains(car.id)))
        }
    }

    fun toggleFavorite(car: UsedCar) {
        val index = cars.indexOfFirst { it.id == car.id }
        if (index != -1) {
            val current = cars[index]
            cars[index] = current.copy(isFavorite = !current.isFavorite)
        } else {
            cars.add(car.copy(isFavorite = true))
        }
        saveFavorites()
    }

    fun updateCarList(newCars: List<UsedCar>) {
        val favIds = prefs?.getStringSet("favorite_ids", emptySet()) ?: emptySet()
        newCars.forEach { fetched ->
            val isFav = favIds.contains(fetched.id)
            val existingIndex = cars.indexOfFirst { it.id == fetched.id }
            if (existingIndex == -1) {
                cars.add(fetched.copy(isFavorite = isFav))
            } else {
                cars[existingIndex] = fetched.copy(isFavorite = cars[existingIndex].isFavorite || isFav)
            }
        }
    }
}
