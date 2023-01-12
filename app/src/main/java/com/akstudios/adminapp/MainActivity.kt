package com.akstudios.adminapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akstudios.adminapp.databinding.ActivityMainBinding
import com.akstudios.adminapp.faculty.UpdateFacultyActivity
import com.akstudios.adminapp.gelleryImage.UploadImageActivity
import com.akstudios.adminapp.loginScreen.LoginActivity
import com.akstudios.adminapp.notice.DeleteNoticeActivity
import com.akstudios.adminapp.notice.UploadNoticeActivity
import com.akstudios.adminapp.pdf.UploadPdfActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var btnSignOut: ImageButton
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var adminName:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        toolbar = findViewById(R.id.appBar)
        btnSignOut = findViewById(R.id.btnSignOut)
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        adminName = sharedPreferences.getString("adminName", "Admin").toString()
        toolbar.findViewById<TextView>(R.id.txtAdminName).text = "Welcome $adminName"
        setSupportActionBar(toolbar)

        if (sharedPreferences.getString("isLogin", "false") == "false") {
            openLoginActivity()
        }
        btnSignOut.setOnClickListener {
            logOutUsingPreference()
            //signOut()
        }
        binding.takeAttendance.setOnClickListener {
            val url = "https://docs.google.com/spreadsheets/d/1RAfGkFFMOgWNxJxbeidTO9xV5U_R1PeAQbvXk9-t28o/edit#gid=186001220"
            //val url = "https://docs.google.com/spreadsheets/d/1bARJisqIq6-kdUDi19JcMRYs7GDGqkctaFfc2abTMPU/edit#gid=186001220"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
//            val intent = Intent(this, AttendanceActivity::class.java)
//            startActivity(intent)
        }
        binding.addNotice.setOnClickListener {
            val intent = Intent(this, UploadNoticeActivity::class.java)
            startActivity(intent)
        }
        binding.uploadGalleryImage.setOnClickListener {
            val intent = Intent(this, UploadImageActivity::class.java)
            startActivity(intent)
        }
        binding.uploadEbook.setOnClickListener {
            val intent = Intent(this, UploadPdfActivity::class.java)
            startActivity(intent)
        }
        binding.updateFaculty.setOnClickListener {
            val intent = Intent(this, UpdateFacultyActivity::class.java)
            startActivity(intent)
        }
        binding.deleteNotice.setOnClickListener {
            val intent = Intent(this, DeleteNoticeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logOutUsingPreference() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Sign Out?")
            setMessage("Do you really want to sign out?")
            setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    editor.putString("isLogin", "false")
                    editor.commit()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })
            setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {}
            })
        }.show()
    }

    private fun openLoginActivity() {
        Log.d("Logged out of app", "User redirected from MainActivity to LoginActivity")
        Toast.makeText(this, "Please login again to continue using app", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signOut() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Sign Out?")
            setMessage("Do you really want to sign out?")
            setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    if (firebaseAuth.currentUser != null) {
                        firebaseAuth.signOut()
                        if (firebaseAuth.currentUser == null) {
                            Toast.makeText(this@MainActivity, "User signed out successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "User not signed in", Toast.LENGTH_SHORT).show()
                    }

                }
            })
            setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {}
            })
        }.show()
    }
}