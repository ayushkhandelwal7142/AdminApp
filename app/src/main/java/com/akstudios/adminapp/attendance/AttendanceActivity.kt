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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.akstudios.adminapp.MainActivity
import com.akstudios.adminapp.R
import com.akstudios.adminapp.databinding.ActivityAttendanceBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AttendanceActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var databaseReference: DatabaseReference
    private var list: ArrayList<AttendanceData> = arrayListOf()
    private lateinit var btnCalender: ImageButton
    lateinit var btnAddNewStudent: ImageView
    private lateinit var btnMarkAttendance: Button
    private lateinit var progressBar: ProgressBar
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

                }

            })
        }

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