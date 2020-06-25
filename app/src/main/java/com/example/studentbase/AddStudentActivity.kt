package com.example.studentbase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_student.*

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        btnSave.setOnClickListener {
            if (editUpStudentName.text.isEmpty()){
                Toast.makeText(this, "Enter Student Name", Toast.LENGTH_SHORT).show()
                editUpStudentName.requestFocus()
            } else {
                val student = Student()
                student.studentName = editUpStudentName.text.toString()
                if (editUpCredit.text.isEmpty())
                    student.credit = 0.0 else
                    student.credit = editUpCredit.text.toString().toDouble()
                MainActivity.dbHandler.addStudent (this, student)
                ClearEdit()
                editUpStudentName.requestFocus()
            }
        }
        btnCansel.setOnClickListener {
            ClearEdit()
            finish()
        }
    }

    private fun ClearEdit(){
        editUpStudentName.text.clear()
        editUpCredit.text.clear()
    }

}