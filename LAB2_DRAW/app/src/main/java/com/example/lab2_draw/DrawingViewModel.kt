package com.example.lab2_draw

import android.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    // Live list of paths. Observer is registered in 2nd Fragment
    private val _paths = MutableLiveData<MutableList<Path>>(mutableListOf())

    // _path getter
    val paths: LiveData<MutableList<Path>> get() = _paths

    // Adds a new Path to the _paths list and notify observers
    fun addPath(path: Path) {
        val currentPaths = _paths.value ?: mutableListOf()
        currentPaths.add(path)
        _paths.value = currentPaths
    }
}
