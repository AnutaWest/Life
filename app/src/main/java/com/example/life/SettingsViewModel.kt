package com.example.life

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    val speed = MutableLiveData(2)
    val width = MutableLiveData(20)
    val height = MutableLiveData(40)
}