package com.example.makart

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ImageBitmap
import java.time.LocalDateTime

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// This ViewModel class is used to store and manage the state of the drawing strokes
// for the DrawingView. It retains the list of strokes across configuration changes,
// such as screen rotations, fragmentation change, and editing.



class DrawingViewModel : ViewModel() {
    private val _currentPathProperty = MutableStateFlow(StrokeProperties())
    val currentPathProperty: StateFlow<StrokeProperties> = _currentPathProperty.asStateFlow()



    // List of drawn lines
    val lines = mutableStateListOf<Line>()

    init {
        Log.d("DrawingViewModel", "Lines restored: ${lines.size}")
    }

    // Add a line to the drawing
    fun addLine(line: Line) {
        lines.add(line)
    }


}



data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val strokeWidth: Dp = 1.dp
)

data class StrokeProperties(
    val color: Color = Color.Black,
    val strokeWidth: Float = 5f,
    val strokeCap: StrokeCap = StrokeCap.Round,
)




// Drawing class that contains metadata for each drawing
data class Drawing(
    val name: String,
    val thumbnail: ImageBitmap,
    val lastModified: LocalDateTime,
    val lines: List<Line> // Stores the lines needed to re-draw the canvas
)