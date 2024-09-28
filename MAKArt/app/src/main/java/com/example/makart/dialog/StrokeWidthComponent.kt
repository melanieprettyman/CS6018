package com.example.makart.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.makart.StrokeProperties


@Composable
fun StrokeWidthComponent(
    currentStrokeProperty: StrokeProperties,
    updateCurrentStrokeProperty: (newStrokeWidth: Float?, strokeCap: StrokeCap?) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(350.dp)
            .padding(16.dp)
            .wrapContentHeight()
    ) {
        // Close button in the top-right corner with added padding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp, top = 12.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = { onDismiss() }) {
                Text(
                    text = "X",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Main Content
        Column(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // Stroke width slider with rounded value
            Text(
                text = "Adjust Stroke Width: ${"%.2f".format(currentStrokeProperty.strokeWidth)}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Slider(
                value = currentStrokeProperty.strokeWidth,
                onValueChange = {
                    updateCurrentStrokeProperty(it, null)
                },
                valueRange = 1f..50f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}
