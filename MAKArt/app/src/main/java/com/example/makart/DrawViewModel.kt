package com.example.makart


import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// This ViewModel class is used to store and manage the state of the drawing strokes
// for the DrawingView. It retains the list of strokes across configuration changes,
// such as screen rotations, fragmentation change, and editing.



class DrawingViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentPathProperty = MutableStateFlow(StrokeProperties())
    val currentPathProperty: StateFlow<StrokeProperties> = _currentPathProperty.asStateFlow()

    // Add a line to the current drawing
    val lines = mutableStateListOf<Line>()

    fun addLine(line: Line) {
        lines.add(line)
    }

    //Init JokeDao to access the local Room database for jokes.
    private val drawingDao: DrawingDao = DrawingDatabase.getDatabase(application).drawingDao()

    val drawingList: Flow<List<DrawingEntity>> = drawingDao.getAllDrawings()


    //Coroutine that makes a network request to fetch a random joke. The fetched joke is inserted
    // into the Room database. The MutableStateFlow _currentJoke is updated with the fetched joke,
    // and this triggers an update to the UI.
    fun addDrawing(name: String) {
        viewModelScope.launch {
            try {
                drawingDao.insertDrawing(DrawingEntity(name = name))


            } catch (e: Exception) {
               "Failed to insert drawing"
            }
        }
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
