package com.example.TravApp.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TravViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
