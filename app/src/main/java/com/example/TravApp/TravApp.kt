package com.example.TravApp

import android.app.Application
import com.google.android.libraries.places.api.Places

class TravApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Инициализация Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyA9SIru1MyHRY-LDzERZ68Dl9pv89pTvVs")
        }
    }
}
