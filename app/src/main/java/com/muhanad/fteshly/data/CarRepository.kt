package com.muhanad.fteshly.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CarRepository {
    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        null
    }

    suspend fun searchCarsByName(query: String): List<UsedCar> {
        val trimmedQuery = query.trim().lowercase()
        if (trimmedQuery.isEmpty()) return emptyList()

        // Mock fallback dataset for instant search testing (images stored on Cloudflare R2 / remote storage)
        val mockDatabase = listOf(
            UsedCar("1", "هيونداي النترا مستعملة نظيفة", "2019", "22,000,000 ج.س", CarCondition.USED, "الخرطوم", "0912345678", "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&w=800&q=80", false),
            UsedCar("2", "هيونداي توسان (مشلعة لقطع الغيار)", "2016", "11,500,000 ج.س", CarCondition.DISMANTLED, "أمدرمان", "0923456789", "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80", false),
            UsedCar("3", "تويوتا لاندكروزر V8", "2018", "45,000,000 ج.س", CarCondition.USED, "الخرطوم", "0934567890", "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=800&q=80", false),
            UsedCar("4", "تويوتا هايلكس (دفرنسات ومكينة)", "2015", "16,000,000 ج.س", CarCondition.DISMANTLED, "بورتسودان", "0945678901", "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=800&q=80", false),
            UsedCar("5", "نيسان باترول سفاري", "2014", "28,000,000 ج.س", CarCondition.USED, "مدني", "0956789012", "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80", false)
        )

        return try {
            if (firestore == null) {
                return mockDatabase.filter { it.title.lowercase().contains(trimmedQuery) || it.model.contains(trimmedQuery) }
            }

            // Query Firestore collection "cars"
            val snapshot = firestore.collection("cars")
                .get()
                .await()

            val firestoreCars = snapshot.documents.mapNotNull { doc ->
                val title = doc.getString("title") ?: return@mapNotNull null
                val model = doc.getString("model") ?: "2020"
                val price = doc.getString("price") ?: "0 ج.س"
                val conditionStr = doc.getString("condition") ?: "USED"
                val condition = try { CarCondition.valueOf(conditionStr) } catch (e: Exception) { CarCondition.USED }
                val location = doc.getString("location") ?: "السودان"
                val phone = doc.getString("phone") ?: "0912345678"
                val imageUrl = doc.getString("imageUrl") ?: "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
                
                UsedCar(
                    id = doc.id,
                    title = title,
                    model = model,
                    price = price,
                    condition = condition,
                    location = location,
                    phone = phone,
                    imageUrl = imageUrl,
                    isFavorite = false
                )
            }

            val combined = (firestoreCars + mockDatabase).distinctBy { it.id }
            combined.filter { it.title.lowercase().contains(trimmedQuery) || it.model.contains(trimmedQuery) }
        } catch (e: Exception) {
            mockDatabase.filter { it.title.lowercase().contains(trimmedQuery) || it.model.contains(trimmedQuery) }
        }
    }
}
