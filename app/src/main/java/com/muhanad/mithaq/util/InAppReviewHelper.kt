package com.muhanad.mithaq.util

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory

object InAppReviewHelper {
    fun checkAndLaunchReview(activity: Activity) {
        val prefs = activity.getSharedPreferences("mithaq_review_prefs", Context.MODE_PRIVATE)
        val launchCount = prefs.getInt("launch_count", 0) + 1
        prefs.edit().putInt("launch_count", launchCount).apply()

        // Trigger in-app review on the second app open
        if (launchCount == 2) {
            try {
                val manager = ReviewManagerFactory.create(activity)
                val request = manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo = task.result
                        manager.launchReviewFlow(activity, reviewInfo)
                    }
                }
            } catch (e: Exception) {
                // Fallback gracefully if review flow fails
            }
        }
    }
}
