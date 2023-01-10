package com.akstudios.adminapp.loginScreen

import android.app.ProgressDialog
import android.content.Intent
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
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_actiity)

        firebaseAuth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.btnLogin)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        progressDialog = ProgressDialog(this)

        btnLogin.setOnClickListener {
            login()
        }
        btnSignUp.setOnClickListener {
            Toast.makeText(this, "Please contact App Developer for login credentials", Toast.LENGTH_LONG).show()
        }
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