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
import androidx.lifecycle.Observer

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

        // paths changes, the observer is triggered and updates the DrawingView with
        // the latest paths from the ViewModel
        drawingViewModel.paths.observe(viewLifecycleOwner, Observer { paths ->
            drawingView.setPaths(paths)
        })

        // Touch listener for drawing actions
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
                // Adds the path when drawing is finished and reset the current path in the DrawingView
                MotionEvent.ACTION_UP -> {
                    drawingViewModel.addPath(drawingView.getCurrentPath())
                    drawingView.resetCurrentPath()
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
