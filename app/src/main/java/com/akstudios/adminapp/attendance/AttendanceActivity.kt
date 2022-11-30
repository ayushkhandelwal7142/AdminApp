package com.akstudios.adminapp.attendance

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.MainActivity
import com.akstudios.adminapp.R
import com.akstudios.adminapp.databinding.ActivityAttendanceBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AttendanceActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var databaseReference: DatabaseReference
    private var list: ArrayList<AttendanceData> = arrayListOf()
    private lateinit var btnCalender: ImageButton
    lateinit var btnAddNewStudent: ImageView
    private lateinit var btnMarkAttendance: Button
    private lateinit var progressBar: ProgressBar
    //private lateinit var attendanceList: ListView
    private lateinit var txtDate: TextView
    private  var sName: String = ""
    private  var sRollNumber: String = ""
    private  var sGender: String = ""
    private  var sClass: String = ""
      var date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressBar = findViewById(R.id.progressBar)
        val toolbar = findViewById<Toolbar>(R.id.attendanceToolBar)
        btnCalender = findViewById(R.id.btnCalender)
        btnAddNewStudent = findViewById(R.id.btnAddNewStudent)
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance)
        txtDate = findViewById(R.id.txtDate)
        setSupportActionBar(toolbar)

        val date = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        txtDate.text = date

        databaseReference = FirebaseDatabase.getInstance().reference
        getStudentList()

        btnCalender.setOnClickListener {
            selectDate()
        }
        btnAddNewStudent.setOnClickListener {
            val intent = Intent(this, AddNewStudentActivity::class.java)
            startActivity(intent)
        }
        btnMarkAttendance.setOnClickListener {
            markAttendance()
        }
    }

    private fun markAttendance() {
        val alertDialog = android.app.AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Mark Attendance")
            setMessage("Do you want to mark or update attendance for $date ?")
            setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val attendanceList: RecyclerView = findViewById(R.id.attendanceRV)
                    var hp = HashMap<String, Int>()
                    for (i in 0 until attendanceList.childCount) {
                        val newView = attendanceList.getChildAt(i)
                        val checkbox = newView.findViewById<CheckBox>(R.id.checkBox)
                        val rollNo = newView.findViewById<TextView>(R.id.rollNo)
                        if (checkbox.isChecked) {
                            hp[rollNo.text.toString()] = 1
                        } else {
                            hp[rollNo.text.toString()] = 0
                        }
                    }
                    val reference = databaseReference.child("Attendance")

                    val uniqueKey = reference.push().key
                    if (uniqueKey != null) {
                        reference.child(txtDate.text.toString()).setValue(hp).addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this@AttendanceActivity, "Student Data uploaded successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@AttendanceActivity, AttendanceActivity::class.java)
                            startActivity(intent)
                            finish()
                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(this@AttendanceActivity, "Could Not Upload Student Data. PLease try again later", Toast.LENGTH_LONG).show()
                        }
                    }
                    else {
                        progressDialog.dismiss()
                        Toast.makeText(this@AttendanceActivity, "Error. Could not found unique key", Toast.LENGTH_LONG).show()
                    }
                }

            })
            setNegativeButton("No", object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {}
            })
        }.show()

    }

    private fun selectDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(supportFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            val dateFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            date = dateFormatter.format(Date(it))
            txtDate.text = date
        }
    }

    private fun getStudentList() {
        val reference = databaseReference.child("StudentData")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // no data available
                    Toast.makeText(this@AttendanceActivity, "No Data Available", Toast.LENGTH_LONG).show()
                    binding.txtNoDataAvailable.visibility = View.VISIBLE
                    binding.attendanceRV.visibility = View.GONE
                    progressBar.visibility = View.GONE
                } else {
                    for (i in snapshot.children) {
                        val data = i.getValue(AttendanceData::class.java)
                        if (data != null) {
                            list.add(data)
                        }
                        val fAdapter = AttendanceAdapter(this@AttendanceActivity, list)
                        binding.attendanceRV.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this@AttendanceActivity)
                            adapter = fAdapter
                        }
                        binding.txtNoDataAvailable.visibility = View.GONE
                        binding.attendanceRV.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // to be written
            }
        })
    }
}