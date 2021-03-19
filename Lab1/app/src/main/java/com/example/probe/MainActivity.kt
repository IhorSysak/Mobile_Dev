package com.example.probe

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val checkBox2 = findViewById<CheckBox>(R.id.checkBox2)



        button.setOnClickListener {
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId

            textView.text = ""
            if (checkedRadioButtonId != -1 && (checkBox.isChecked || checkBox2.isChecked)) {
                val selectedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
                textView.text = selectedRadioButton.text
                if (checkBox.isChecked) {
                    textView.text = "${textView.text} ${checkBox.text}"
                }
                if (checkBox2.isChecked) {
                    textView.text = "${textView.text} ${checkBox2.text}"
                }
            }

            if (checkBox.isChecked == false && checkBox2.isChecked == false) {
                textView.text = "Please, select CheckBox"
            }
            if (checkedRadioButtonId == -1 && (checkBox.isChecked || checkBox2.isChecked)) {
                textView.text = "Please, select RadioButton"
            } else if (checkedRadioButtonId == -1 && checkBox.isChecked == false && checkBox2.isChecked == false) {
                textView.text = "Please, select something"
            }

        }
    }
}


