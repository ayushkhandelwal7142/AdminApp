package com.akstudios.adminapp.attendance

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.akstudios.adminapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNewStudentActivity : AppCompatActivity() {
    private lateinit var btnAddStudent: Button
    private lateinit var spClass: Spinner
    private lateinit var spGender: Spinner
    private lateinit var addNewStudentName: EditText
    private lateinit var addNewRollNo: EditText
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_student)

        progressDialog = ProgressDialog(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        btnAddStudent = findViewById(R.id.btnAddStudent)
        spClass = findViewById(R.id.addNewSpClass)
        spGender = findViewById(R.id.addNewSpGender)
        addNewStudentName = findViewById(R.id.addNewSName)
        addNewRollNo = findViewById(R.id.addNewRollNo)
        val spCLassItems = arrayListOf("Choose class", "Nursery", "KG1", "KG2", "1", "2", "3", "4")
        val sClassAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spCLassItems)
        sClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spClass.adapter = sClassAdapter
        val spGenderItems = arrayListOf("Gender", "Male", "Female")
        val sGenderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spGenderItems)
        sGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender.adapter = sGenderAdapter

        var sClass = "Choose class"
        var sGender = "Gender"
        spClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                sClass = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Please choose the category", Toast.LENGTH_LONG).show()
            }
        }
        spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                sGender = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Please choose the category", Toast.LENGTH_LONG).show()
            }
        }

        btnAddStudent.setOnClickListener {
            val name = addNewStudentName.text.toString()
            val rollNo = addNewRollNo.text.toString()
            val stClass = sClass
            val gender = sGender
            if (name.isEmpty() || rollNo.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_LONG).show()
            } else if (stClass == "Choose class") {
                Toast.makeText(this, "Please choose class", Toast.LENGTH_LONG).show()
            } else if (gender == "Gender") {
                Toast.makeText(this, "Please choose Gender", Toast.LENGTH_LONG).show()
            } else {
                val listData = AttendanceData(name, rollNo, sGender, sClass)
                addNewStudent(listData)
            }
        }
    }
    private fun addNewStudent(data: AttendanceData) {
        progressDialog.show()
        val reference = databaseReference.child("StudentData")
        val uniqueKey = reference.push().key
        if (uniqueKey != null) {
            data.uniqueKey = uniqueKey
            reference.child(uniqueKey).setValue(data).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Student Data uploaded successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, AttendanceActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Could Not Upload Student Data. PLease try again later", Toast.LENGTH_LONG).show()
            }
        }
        else {
            progressDialog.dismiss()
            Toast.makeText(this, "Error. Could not found unique key", Toast.LENGTH_LONG).show()
        }
    }
}