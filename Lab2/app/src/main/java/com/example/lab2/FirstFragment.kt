package com.example.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_first.view.*

class FirstFragment : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_first, container, false)

        communicator = activity as Communicator

        val radioGroup = view.radioGroup
        val checkBox = view.checkBox
        val checkBox2 = view.checkBox2

        view.sendBtn.setOnClickListener{

            val checkedRadioButtonId = radioGroup.checkedRadioButtonId
            var transferMessage: String? = ""
            if (checkedRadioButtonId != -1) {

                val selectedRadioButton = view.findViewById<RadioButton>(checkedRadioButtonId)
                transferMessage = "${selectedRadioButton.text}"

                if (checkBox.isChecked) {
                    transferMessage = "${transferMessage} ${checkBox.text}"
                }
                if (checkBox2.isChecked) {
                    transferMessage = "${transferMessage} ${checkBox2.text}"
                }
            }

            if(checkedRadioButtonId == -1 && checkBox.isChecked == false && checkBox2.isChecked == false) {
                Toast.makeText(requireContext(), "Please, select something", Toast.LENGTH_LONG).show()
            }
            else if (checkBox.isChecked == false && checkBox2.isChecked == false) {
                Toast.makeText(requireContext(), "Please, select CheckBox", Toast.LENGTH_LONG).show()
            }
            else if (checkedRadioButtonId == -1 && (checkBox.isChecked || checkBox2.isChecked)) {
                Toast.makeText(requireContext(), "Please, select RadioButton", Toast.LENGTH_LONG).show()
            }
            else {
                communicator.passDataCom(transferMessage.toString())
            }
        }

        return view
    }


}