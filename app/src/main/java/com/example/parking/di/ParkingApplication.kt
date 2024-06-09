package com.example.parking.di

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
/**
 * The application class for the Parking app, implementing `ImageLoaderFactory` to provide image loading configuration.
 */
class ParkingApplication : Application(), ImageLoaderFactory {

    /**
     * Called when the application is starting, before any other application objects have been created.
     *
     * This method is overridden to perform initialization tasks when the app is created.
     */
    override fun onCreate() {
        super.onCreate()
    }

    /**
     * Creates and configures a new instance of `ImageLoader`.
     *
     * This method sets up the memory and disk cache for the image loader.
     *
     * @return A configured `ImageLoader` instance.
     */
    override fun newImageLoader(): ImageLoader {
        // Configures and returns a new ImageLoader instance
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // Sets the memory cache size to 25% of the app's memory
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache")) // Sets the disk cache directory
                    .maxSizePercent(0.1) // Sets the disk cache size to 10% of the app's available disk space
                    .build()
            }
            .build()
    }
}

