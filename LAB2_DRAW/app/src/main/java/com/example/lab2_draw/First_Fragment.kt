package com.example.lab2_draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.first_fragment, container, false)
        val drawButton: Button = view.findViewById(R.id.draw_btn)

        drawButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SecondFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
