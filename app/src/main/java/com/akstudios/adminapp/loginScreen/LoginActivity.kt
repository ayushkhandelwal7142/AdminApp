package com.akstudios.adminapp.loginScreen

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.akstudios.adminapp.MainActivity
import com.akstudios.adminapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLogin: ImageView
    private lateinit var btnSignUp: TextView
    private lateinit var etAdminName: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var txtShowHide: ImageView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_actiity)

        firebaseAuth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.btnLogin)
        etAdminName = findViewById(R.id.etAdminName)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        etPassword = findViewById(R.id.etPassword)
        txtShowHide = findViewById(R.id.txtShowHide)
        btnSignUp = findViewById(R.id.btnSignUp)
        progressDialog = ProgressDialog(this)
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (sharedPreferences.getString("isLogin", "false") == "true") {
            openDashBoard()
        }

        txtShowHide.setOnClickListener {
            showHidePassword()
        }
        btnLogin.setOnClickListener {
            //login()
            loginUsingPreference()
        }
        btnSignUp.setOnClickListener {
            Toast.makeText(this, "Please contact App Developer for login credentials", Toast.LENGTH_LONG).show()
        }
    }

    private fun showHidePassword() {
        if (etPassword.text.isNotEmpty()) {
            if (etPassword.inputType == 144) { // 144 - means input type is in hide mode
                etPassword.inputType = 129 // 129 - means input type is in show mode
                txtShowHide.setImageResource(R.drawable.ic_show_password)
            } else {
                etPassword.inputType = 144
                txtShowHide.setImageResource(R.drawable.ic_hide_password)
            }
            etPassword.setSelection(etPassword.text.length)
        }
    }

    private fun loginUsingPreference() {
        val adminName = etAdminName.text.toString()
        val email = etEmailAddress.text.toString()
        val password = etPassword.text.toString()

        if (email.isEmpty()) {
            etEmailAddress.apply {
                setError("Required field")
                requestFocus()
            }
        } else if (password.isEmpty()) {
            etPassword.apply {
                setError("Required field")
                requestFocus()
            }
        } else if (adminName.isEmpty()) {
            etAdminName.apply {
                setError("Required field")
                requestFocus()
            }
        } else {
            progressDialog.apply {
                setTitle("Logging in")
                setMessage("Please wait...")
                show()
            }
            if (email.equals("akstudios@gmail.com") && password.equals("12345678")) {
                progressDialog.dismiss()
                editor.putString("isLogin", "true")
                editor.putString("adminName", adminName)
                editor.commit()
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                openDashBoard()
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Incorrect email/password. PLease try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openDashBoard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun login() {
        val email = etEmailAddress.text.toString()
        val password = etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email/password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        } else {
            progressDialog.apply {
                setTitle("Logging in")
                setMessage("Please wait...")
                show()
            }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                if(it.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}