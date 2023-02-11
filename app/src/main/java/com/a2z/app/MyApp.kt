package com.a2z.app

import android.app.Application
import com.a2z_di.app.DistApp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DistApp().init(this)
    }
}