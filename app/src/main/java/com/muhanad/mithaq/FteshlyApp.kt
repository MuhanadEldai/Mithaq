package com.muhanad.mithaq

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy

class FteshlyApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setApiKey("AIzaSyAvn5J5iLLpRAR_7G2hfhd4Suy4yZUy5Gc")
                    .setApplicationId("1:135237095782:android:7612dafee65e76a86378a7")
                    .setProjectId("mithaq-6ec84")
                    .setGcmSenderId("135237095782")
                    .build()
                FirebaseApp.initializeApp(this, options)
                Log.d("FteshlyApp", "FirebaseApp initialized programmatically for project: mithaq-6ec84")
            }
        } catch (e: Exception) {
            Log.e("FteshlyApp", "Failed to initialize FirebaseApp", e)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(512L * 1024 * 1024) // 512MB disk cache
                    .build()
            }
            .respectCacheHeaders(false) // Never refetch if cached
            .crossfade(true)
            .build()
    }
}
