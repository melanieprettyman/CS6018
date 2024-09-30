package com.example.lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.lab5.ui.theme.Lab5Theme

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow



class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init sensorManager to manage and interact with hardware sensors
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        // init gravity sensory
        val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        // Check if the gravity sensor is available.
        // gravityFlow returns a Flow<FloatArray> for processing sensor data
        if (gravitySensor != null) {
            val gravityFlow: Flow<FloatArray> = movementDetector(gravitySensor, sensorManager)

            setContent {
                Lab5Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            var xVal by remember { mutableFloatStateOf(0f) }
                            var yVal by remember { mutableFloatStateOf(0f) }

                            Marble(xVal, yVal)

                            // collect the gravity sensor data from the Flow and
                            // update xVal and yVal accordingly
                            LaunchedEffect(key1 = gravityFlow) {
                                gravityFlow.collect { movementReading ->
                                    xVal = movementReading[0]
                                    yVal = -movementReading[1] // Invert y-axis to match coordinate system
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Log.e("Sensor", "Gravity sensor not available")
        }
    }


    // Displays moving marble. The position of the marble is determined by the values of x and y,
    // which come from the gravity sensor readings.

    @Composable
    fun Marble(x: Float, y: Float, modifier: Modifier = Modifier) {
        // represent the current position of the marble
        var xOffset by remember { mutableFloatStateOf(165f) }
        var yOffset by remember { mutableFloatStateOf(375f) }

        // Smoothing factor to control the movement speed
        val smoothingFactor = 5f
        xOffset += x / smoothingFactor
        yOffset += y / smoothingFactor

        // check boundaries to ensure the marble stays within the screen boundaries.
        BoxWithConstraints {
            val maxX = with(LocalDensity.current) { constraints.maxWidth.toDp() - 80.dp } // max - marble size
            val maxY = with(LocalDensity.current) { constraints.maxHeight.toDp() - 80.dp }

            // Ensure the marble stays within the screen boundaries
            xOffset = xOffset.coerceIn(0f, maxX.value)
            yOffset = yOffset.coerceIn(0f, maxY.value)

            Box(
                modifier = modifier
                    .offset(x = xOffset.dp, y = yOffset.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .drawBehind { drawRect(Color.Magenta) },
                contentAlignment = Alignment.Center
            ) {
            }
        }
    }

    // Sets up the sensor listener and returns a Flow<FloatArray> that emits sensor data
    private fun movementDetector(gravity: Sensor, sensorManager: SensorManager): Flow<FloatArray> {
        return channelFlow {
            // Init lister that listens for changes in sensor data.
            // When sensor data changes, sends the data through the channel.
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let { channel.trySend(it.values).isSuccess }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Handle accuracy changes if necessary
                }
            }
            sensorManager.registerListener(listener, gravity, SensorManager.SENSOR_DELAY_GAME)

            awaitClose { sensorManager.unregisterListener(listener) } // Cleanup to avoid memory leaks
        }
    }
}