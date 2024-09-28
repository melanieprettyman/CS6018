package com.example.makart
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.makart.dialog.ColorPickerComponent
import com.example.makart.dialog.SaveDrawingDialog
import com.example.makart.dialog.StrokeWidthComponent
import com.github.skydoves.colorpicker.compose.ColorPickerController


@Composable
fun DrawEditorScreen(
    drawingViewModel: DrawingViewModel = viewModel(),
    onBack: () -> Unit
) {
    // Use the lines from the ViewModel
    val lines = drawingViewModel.lines
    val currentStrokeProperties by drawingViewModel.currentPathProperty.collectAsState()
    var selectedColor by remember { mutableStateOf(currentStrokeProperties.color) }
    var strokeWidth by remember { mutableStateOf(currentStrokeProperties.strokeWidth) }
    var eraseMode by remember { mutableStateOf(false) }
    var hexColorString by remember { mutableStateOf("#000000") }

    // State variables to control dialog visibility
    var showColorPickerDialog by remember { mutableStateOf(false) }
    var showStrokeWidthDialog by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    // Color picker controller from the color picker library
    val colorPickerController = remember { ColorPickerController() }
    var currentStrokeProperty by remember {
        mutableStateOf(StrokeProperties(color = selectedColor, strokeWidth = strokeWidth))
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top action bar with buttons (Undo, Save, Color, Stroke, Erase)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painterResource(id = R.drawable.ic_back),
                    contentDescription = "Undo"
                )
            }
            IconButton(onClick = { showSaveDialog = true }) {
                Icon(
                    painterResource(id = R.drawable.ic_floppy_disk),
                    contentDescription = "Save"
                )
            }
            IconButton(onClick = { showColorPickerDialog = true }) {
                Icon(
                    painterResource(id = R.drawable.palette),
                    contentDescription = "Color"
                )
            }
            IconButton(onClick = { showStrokeWidthDialog = true }) {
                Icon(
                    painterResource(id = R.drawable.ic_paint_brush),
                    contentDescription = "Stroke"
                )
            }
            IconButton(onClick = {
                eraseMode = !eraseMode
            }) {
                Icon(
                    painterResource(id = if (eraseMode) R.drawable.ic_eraser_off else R.drawable.ic_eraser),
                    contentDescription = "Erase"
                )
            }
        }

        // Drawing Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        val colorToUse = if (eraseMode) Color.White else selectedColor
                        val widthToUse = if (eraseMode) 20.dp else strokeWidth.dp

                        val line = Line(
                            start = change.position - dragAmount,
                            end = change.position,
                            color = colorToUse,
                            strokeWidth = widthToUse
                        )

                        // Add the new line to the ViewModel
                        drawingViewModel.addLine(line)
                    }
                }
        ) {
            // Draw existing lines from the ViewModel
            lines.forEach { line ->
                drawLine(
                    color = line.color,
                    start = line.start,
                    end = line.end,
                    strokeWidth = line.strokeWidth.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }
    }

    // Show color picker dialog
    if (showColorPickerDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // Semi-transparent background for modal effect
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            ColorPickerComponent(
                hexColorString = hexColorString,
                currentColor = selectedColor,
                updateHexColorCode = { newHex -> hexColorString = newHex },
                updateCurrentColor = { newColor -> selectedColor = newColor },
                controller = colorPickerController,
                onDismiss = { showColorPickerDialog = false } // Handle dialog dismissal
            )
        }
    }

    if (showStrokeWidthDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)) // Semi-transparent background for modal effect
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            StrokeWidthComponent(
                currentStrokeProperty = currentStrokeProperty,
                updateCurrentStrokeProperty = { newStrokeWidth, strokeCap ->
                    newStrokeWidth?.let { strokeWidth = it }
                    currentStrokeProperty = currentStrokeProperty.copy(
                        strokeWidth = newStrokeWidth ?: currentStrokeProperty.strokeWidth,
                        strokeCap = strokeCap ?: currentStrokeProperty.strokeCap,
                    )
                },
                onDismiss = { showStrokeWidthDialog = false }
            )
        }
    }

    // Show save dialog
    if (showSaveDialog) {
        SaveDrawingDialog(
            onConfirm = { drawingName ->
                // Handle the save logic, pass the drawing name to the view model or save functionality
                showSaveDialog = false
                Log.d("Lines", "$lines")
            },
            onDismiss = { showSaveDialog = false }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewDrawEditorScreen() {
    DrawEditorScreen(
        onBack = { /* No action needed for preview */ },
    )
}





