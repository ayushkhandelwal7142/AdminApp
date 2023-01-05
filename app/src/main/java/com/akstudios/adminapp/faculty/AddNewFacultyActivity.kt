package com.akstudios.adminapp.faculty

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import com.akstudios.adminapp.R
import com.akstudios.adminapp.dataClasses.GalleryImageData
import com.akstudios.adminapp.databinding.ActivityAddNewFacultyBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNewFacultyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewFacultyBinding
    private val REQ = 1
    private var bitmap: Bitmap? = null
    private var selectedCategory: String = ""
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var downloadUrl: String = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var post: String
    private lateinit var phNumber: String
    private lateinit var items: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_faculty)

        binding = ActivityAddNewFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        databaseReference = FirebaseDatabase.getInstance().reference.child("Faculty")
        storageReference = FirebaseStorage.getInstance().reference.child("Faculty")
        items = arrayListOf("Select Subject", "English", "Hindi", "Maths")
        Log.d("ArrayList of Items", items.toString())

        progressDialog = ProgressDialog(this)
        binding.spinnerFacultyCategory.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)
//        binding.spinnerFacultyCategory.onItemSelectedListener.apply{
//
//        }
        binding.addTeacherImage.setOnClickListener {
        openGallery()
        }
        binding.btnAddFaculty.setOnClickListener {
            name = binding.facultyName.text.toString()
            email = binding.facultyEmail.text.toString()
            post = binding.facultyPost.text.toString()
            phNumber = binding.facultyContactNo.text.toString()
            selectedCategory = binding.spinnerFacultyCategory.selectedItem.toString()
            Log.d("Name", name)
            Log.d("E-mail", email)
            Log.d("post", post)
            Log.d("phNumber", phNumber)
            Log.d("Selected Category", selectedCategory)
            if (name.isEmpty() || email.isEmpty() || post.isEmpty() || phNumber.isEmpty() || selectedCategory == "Select Subject") {
                when {
                    name.isEmpty() -> binding.facultyName.apply {
                        error = "Please type your name"
                        requestFocus()
                    }
                    email.isEmpty() -> binding.facultyEmail.apply {
                        error = "Please type your email address"
                        requestFocus()
                    }
                    post.isEmpty() -> binding.facultyPost.apply {
                        error = "Please mention your post"
                        requestFocus()
                    }
                    phNumber.isEmpty() -> binding.facultyContactNo.apply {
                        error = "Please mention your contact number"
                        requestFocus()
                    }
                    else -> Toast.makeText(applicationContext, "Please select post", Toast.LENGTH_LONG).show()
                }
            } else {
                progressDialog.show()
                uploadProfilePic()
            }
        }
    }

    private fun uploadProfilePic() {
        progressDialog.setMessage("Uploading...")
        progressDialog.show()
            val baos = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val finalImage = baos.toByteArray()
            val filePath = storageReference.child(selectedCategory).child("$finalImage jpeg")
            val uploadTask: UploadTask = filePath.putBytes(finalImage)
            uploadTask.addOnCompleteListener (this@AddNewFacultyActivity, OnCompleteListener {
                if (it.isSuccessful) {
                    uploadTask.addOnSuccessListener {
                         filePath.downloadUrl.addOnSuccessListener {
                             downloadUrl = it.toString()
                             uploadData()
                         }
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun uploadData() {
        databaseReference = databaseReference.child(selectedCategory)
        val uniqueKey = databaseReference.push().key

        // Get date and time
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        val date = currentDate.format(Date())
        val currentTime = SimpleDateFormat("hh:mm a", Locale.UK)
        val time = currentTime.format(Date())
        val facultyData = FacultyData(uniqueKey, downloadUrl, date, time, name, email, post, selectedCategory, phNumber)

        if (uniqueKey != null) {
            databaseReference.child(uniqueKey).setValue(facultyData).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Faculty Data uploaded successfully", Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Could Not Upload Faculty Data. PLease try again later", Toast.LENGTH_LONG).show()
            }
        } else {
            progressDialog.dismiss()
            Toast.makeText(this, "Error. Could not found unique key", Toast.LENGTH_LONG).show()
        }
    }

    private fun openGallery() {
        val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImage, REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.addTeacherImage.setImageBitmap(bitmap)
        }
    }
}