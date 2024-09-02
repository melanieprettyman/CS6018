package com.example.lab2_draw

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels



// SecondFragment.kt
class SecondFragment : Fragment() {

    private lateinit var drawingView: DrawingView
    private val drawingViewModel: DrawingViewModel by activityViewModels()


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.second_fragment, container, false)
        drawingView = view.findViewById(R.id.drawing_view)

        drawingView.setPaths(drawingViewModel.paths)

        drawingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawingView.startDrawing(event.x, event.y)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    drawingView.continueDrawing(event.x, event.y)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    drawingViewModel.paths.add(drawingView.getCurrentPath())
                    true
                }
                else -> false
            }
        }
        val backButton: ImageButton = view.findViewById(R.id.btn_undo)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        return view
    }
}
