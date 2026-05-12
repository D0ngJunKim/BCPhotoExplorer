package com.bc.app

import android.app.Application
import android.os.Build
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.allowHardware
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ExplorerApplication : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .allowHardware(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            .build()
    }
}
