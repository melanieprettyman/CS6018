package com.example.lab2_draw
import android.graphics.Path
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    val paths = mutableListOf<Path>()
}