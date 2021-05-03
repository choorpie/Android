package com.example.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.done_button)

        button.setOnClickListener{
            val outputText = findViewById<TextView>(R.id.outputTemp_text)
            val editText = findViewById<EditText>(R.id.inputTemp_edit)

            val b = editText.text.toString()

            if(!(b.equals(""))){
                val inputTemp = Integer.parseInt(b)
                outputText.visibility = View.VISIBLE
                val outputTmp = (inputTemp - 32.0) * 5 / 9

                outputText.text = outputTmp.toString()
            }
        }
    }
}