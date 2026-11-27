package com.muhanad.mithaq.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CarRepository {

    private fun getFirestoreInstance(): FirebaseFirestore? {
        return try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e("CarRepository", "Failed to get Firestore instance: ${e.localizedMessage}")
            null
        }
    }

    suspend fun searchCarsByName(query: String): List<UsedCar> {
        val trimmedQuery = query.trim().lowercase()
        if (trimmedQuery.isEmpty()) return emptyList()

        val firestore = getFirestoreInstance()
        if (firestore == null) {
            Log.e("CarRepository", "Firestore instance is null!")
            return emptyList()
        }

        return try {
            // Fetch real documents from Firestore "cars" collection
            val snapshot = firestore.collection("cars")
                .get()
                .await()

            Log.d("CarRepository", "Fetched ${snapshot.documents.size} documents from Firestore.")

            val firestoreCars = snapshot.documents.map { doc ->
                val data = doc.data
                Log.d("CarRepository", "Doc ID: ${doc.id}, Raw Data: $data")

                val rawName = doc.get("n") ?: doc.get("name") ?: doc.get("title") ?: "سيارة ${doc.id}"
                val rawModel = doc.get("mod") ?: doc.get("model") ?: "2022"
                val rawPrice = doc.get("pr") ?: doc.get("price") ?: "0 ج.س"
                val rawLocation = doc.get("loc") ?: doc.get("location") ?: "السودان"
                val rawPhone = doc.get("ph") ?: doc.get("phone") ?: "0912345678"
                
                // Multi-Images URL handling (List or String or comma-separated)
                val imgField = doc.get("img") ?: doc.get("images") ?: doc.get("imageUrl")
                val imageUrls: List<String> = when (imgField) {
                    is List<*> -> imgField.mapNotNull { it?.toString() }.filter { it.isNotBlank() }
                    is String -> if (imgField.contains(",")) imgField.split(",").map { it.trim() } else if (imgField.isNotBlank()) listOf(imgField) else emptyList()
                    else -> emptyList()
                }.ifEmpty {
                    listOf("https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80")
                }

                val rawDis = doc.get("dis") ?: doc.get("distance") ?: "25,000 كم"
                val rawGear = doc.get("gear") ?: doc.get("gearType") ?: "أوتوماتيك"
                val rawFuel = doc.get("fuel") ?: doc.get("fuelType") ?: "بنزين"

                UsedCar(
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
                    isFavorite = false
                )
            }

            val filtered = firestoreCars.filter { car ->
                val normalizedName = car.name.lowercase().replace("أ", "ا").replace("إ", "ا").replace("آ", "ا")
                val normalizedQuery = trimmedQuery.replace("أ", "ا").replace("إ", "ا").replace("آ", "ا")

                normalizedName.contains(normalizedQuery) || 
                car.model.lowercase().contains(normalizedQuery) ||
                car.location.lowercase().contains(normalizedQuery) ||
                car.id.lowercase().contains(normalizedQuery)
            }

            if (filtered.isEmpty() && firestoreCars.isNotEmpty()) {
                firestoreCars
            } else {
                filtered
            }
        } catch (e: Exception) {
            Log.e("CarRepository", "Error searching cars from Firestore", e)
            emptyList()
        }
    }
}
