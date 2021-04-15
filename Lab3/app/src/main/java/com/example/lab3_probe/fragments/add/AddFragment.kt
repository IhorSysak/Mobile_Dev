package com.example.lab3_probe.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lab3_probe.R
import com.example.lab3_probe.data.User
import com.example.lab3_probe.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.File

class AddFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

            val file = File("/data/data/com.example.lab3_probe/databases/user_database-wal")
            var count = file.length()
            if (count <= 37112){
                Toast.makeText(requireContext(), "DataBase is empty", Toast.LENGTH_LONG).show()
            }


        }

        view.add_btn.setOnClickListener{

            val radioGroup = view.radioGroup
            val checkBox = view.checkBox
            val checkBox2 = view.checkBox2
            val checkedRadioButtonId = radioGroup.checkedRadioButtonId

            var transferMessageFigure: String? = ""
            var transferMessageParameter: String? = ""
            displayMessage.text = ""

            if (checkedRadioButtonId != -1 && (checkBox.isChecked || checkBox2.isChecked)) {

                val selectedRadioButton = view.findViewById<RadioButton>(checkedRadioButtonId)
                displayMessage.text = selectedRadioButton.text
                transferMessageFigure = "${selectedRadioButton.text}"

                if (checkBox.isChecked) {
                    displayMessage.text = "${displayMessage.text} ${checkBox.text}"
                    transferMessageParameter = "${transferMessageParameter} ${checkBox.text}"
                }
                if (checkBox2.isChecked) {
                    displayMessage.text = "${displayMessage.text} ${checkBox2.text}"
                    transferMessageParameter = "${transferMessageParameter} ${checkBox2.text}"
                }
            }

            if(checkedRadioButtonId == -1 && !checkBox.isChecked && !checkBox2.isChecked) {
                Toast.makeText(requireContext(), "Please, select something", Toast.LENGTH_LONG).show()
            }
            else if (!checkBox.isChecked && !checkBox2.isChecked) {
                Toast.makeText(requireContext(), "Please, select CheckBox", Toast.LENGTH_LONG).show()
            }
            else if (checkedRadioButtonId == -1 && (checkBox.isChecked || checkBox2.isChecked)) {
                Toast.makeText(requireContext(), "Please, select RadioButton", Toast.LENGTH_LONG).show()
            }
            else{
                if (inputCheck(transferMessageFigure.toString(), transferMessageParameter.toString())) {
                    val user = User(0, transferMessageFigure.toString(), transferMessageParameter.toString())
                    mUserViewModel.addUser(user)
                    Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
                    //findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }
            }
        }

        return view
    }

    private fun inputCheck(transferMessageFigure: String, transferMessageParameter: String) : Boolean{
        return !(TextUtils.isEmpty(transferMessageFigure) && TextUtils.isEmpty(transferMessageParameter))
    }
}