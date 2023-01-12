package com.akstudios.adminapp.faculty

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akstudios.adminapp.R
import com.akstudios.adminapp.databinding.ActivityUpdateTeacherBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class UpdateTeacherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTeacherBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var post: String
    private lateinit var phNumber: String
    private lateinit var uniqueKey: String
    private lateinit var category: String
    private lateinit var profilePic: ImageView
    private lateinit var imageUrl: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private val REQ = 1
    private  var bitmap: Bitmap? = null
    private var downloadUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_teacher)

        binding = ActivityUpdateTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Faculty")
        storageReference = FirebaseStorage.getInstance().reference.child("Faculty")
        progressDialog = ProgressDialog(this)

//        name = binding.utName.text.toString()
//        email = binding.utEmail.text.toString()
//        post = binding.utPost.text.toString()
//        profilePic = binding.utProfilePic
        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()
        post = intent.getStringExtra("post").toString()
        phNumber = intent.getStringExtra("phNumber").toString()
        uniqueKey = intent.getStringExtra("uniqueKey").toString()
        imageUrl = intent.getStringExtra("imageUrl").toString()
        category = intent.getStringExtra("category").toString()


        try {
            Glide.with(this).load(imageUrl).into(binding.utProfilePic)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.utName.setText(name)
        binding.utEmail.setText(email)
        binding.utPost.setText(post)
        binding.utContactNo.setText(phNumber)
        binding.utProfilePic.setOnClickListener {
            openGallery()
        }
        binding.utBtnDelete.setOnClickListener {
            deleteData()
        }
        binding.utBtnUpdate.setOnClickListener {
            name = binding.utName.text.toString()
            email = binding.utEmail.text.toString()
            post = binding.utPost.text.toString()
            phNumber = binding.utContactNo.text.toString()
            checkValidation()
        }
    }

    private fun checkValidation() {
        if (name.isEmpty()) {
            binding.utName.apply {
                setError("Empty")
                requestFocus()
            }
        }else if (email.isEmpty()) {
            binding.utEmail.apply {
                error = "Empty"
                requestFocus()
            }
        }else if (post.isEmpty()) {
            binding.utPost.apply {
                error = "Empty"
                requestFocus()
            }
        }else if (phNumber.isEmpty()) {
            binding.utContactNo.apply {
                error = "Empty"
                requestFocus()
            }
        }else if(bitmap == null) {
            updateData(imageUrl)
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)

        val finalImage = baos.toByteArray()
        val filePath = storageReference.child("Profile Pic").child("$finalImage jpeg")
        val uploadTask: UploadTask = filePath.putBytes(finalImage)
        uploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                uploadTask.addOnSuccessListener {
                    downloadUrl = filePath.downloadUrl.toString()
                    updateData(downloadUrl)
                }
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateData(url: String) {
        val hp = HashMap<String, String>()
        hp["name"] = name
        hp["email"] = email
        hp["post"] = post
        hp["imageUrl"] = url
        hp["phNumber"] = phNumber

        databaseReference.child(category).child(uniqueKey).updateChildren(hp as Map<String, Any>).addOnSuccessListener {

            Toast.makeText(this, "Teacher Data Updated Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this, UpdateFacultyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteData() {
        databaseReference.child(category).child(uniqueKey).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Teacher Data Deleted Successfully", Toast.LENGTH_LONG).show()
            val intent = Intent(this, UpdateFacultyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show()
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
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            binding.utProfilePic.setImageBitmap(bitmap)
        }
    }
}