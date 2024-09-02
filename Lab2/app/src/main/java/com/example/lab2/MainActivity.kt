package com.example.lab2

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import java.io.OutputStream


class MainActivity : AppCompatActivity() {
    // creating the object of type DrawView
    // in order to get the reference of the View
    private var paint: DrawView? = null

    // creating objects of type button
    private var save: ImageButton? = null
    private var color: ImageButton? = null
    private var stroke: ImageButton? = null
    private var undo: ImageButton? = null

    // creating a RangeSlider object, which will
    // help in selecting the width of the Stroke
    private var rangeSlider: RangeSlider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the reference of the views from their ids
        paint = findViewById<View>(R.id.draw_view) as DrawView
        rangeSlider = findViewById<View>(R.id.rangebar) as RangeSlider
        undo = findViewById<View>(R.id.btn_undo) as ImageButton
        save = findViewById<View>(R.id.btn_save) as ImageButton
        color = findViewById<View>(R.id.btn_color) as ImageButton
        stroke = findViewById<View>(R.id.btn_stroke) as ImageButton

        // creating a OnClickListener for each button,
        // to perform certain actions

        // the undo button will remove the most
        // recent stroke from the canvas
        undo!!.setOnClickListener { paint!!.undo() }

        // the save button will save the current
        // canvas which is actually a bitmap
        // in form of PNG, in the storage
        save!!.setOnClickListener {
            // getting the bitmap from DrawView class
            val bmp = paint!!.save()

            // opening a OutputStream to write into the file
            var imageOutStream: OutputStream? = null
            val cv = ContentValues()

            // name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")

            // type of the file
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")

            // location of the file to be saved
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            // get the Uri of the file which is to be created in the storage
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
            try {
                // open the output stream with the above uri
                imageOutStream = contentResolver.openOutputStream(uri!!)

                // this method writes the files in storage
                bmp!!.compress(
                    Bitmap.CompressFormat.PNG, 100,
                    imageOutStream!!
                )

                // close the output stream after use
                imageOutStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // the color button will allow the user
        // to select the color of his brush
        color!!.setOnClickListener {

            val colorPicker = ColorPicker(this@MainActivity)
            colorPicker.setOnFastChooseColorListener(object : OnFastChooseColorListener() {
                fun setOnFastChooseColorListener(position: Int, color: Int) {
                    // get the integer value of color
                    // selected from the dialog box and
                    // set it as the stroke color
                    paint!!.setColor(color)
                }

                fun onCancel() {
                    colorPicker.dismissDialog()
                }
            }) // set the number of color columns
                // you want to show in dialog.
                .setColumns(5) // set a default color selected
                // in the dialog
                .setDefaultColorButton(Color.parseColor("#000000"))
                .show()
        }

        // the button will toggle the visibility of the RangeBar/RangeSlider
        stroke!!.setOnClickListener {
            if (rangeSlider!!.visibility == View.VISIBLE) rangeSlider!!.visibility =
                View.GONE else rangeSlider!!.visibility = View.VISIBLE
        }

        // set the range of the RangeSlider
        rangeSlider!!.valueFrom = 0.0f
        rangeSlider!!.valueTo = 100.0f

        // adding a OnChangeListener which will
        // change the stroke width
        // as soon as the user slides the slider
        rangeSlider!!.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            paint!!.setStrokeWidth(
                value.toInt()
            )
        })

        // pass the height and width of the custom view
        // to the init method of the DrawView object
        val vto = paint!!.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint!!.measuredWidth
                val height = paint!!.measuredHeight
                paint!!.init(height, width)
            }
        })
    }
}
