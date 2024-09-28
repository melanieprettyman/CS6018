package com.example.makart.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SaveDrawingDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val drawingName = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Save Drawing")
                // "X" button for dismiss in the top-right
                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.TopEnd) // Aligns the X to top-right
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close dialog",
                        tint = Color.Black
                    )
                }
            }
        },
        text = {
            Column {
                Text(text = "Enter a name for your drawing:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = drawingName.value,
                    onValueChange = { newText -> drawingName.value = newText },
                    placeholder = { Text(text = "Drawing name") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(drawingName.value) }) {
                Text(text = "Save")
            }
        }
    )
}

@Preview
@Composable
fun PreviewSaveDrawingDialog() {
    SaveDrawingDialog(
        onConfirm = {},
        onDismiss = {}
    )
}
