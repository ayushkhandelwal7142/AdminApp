package com.akstudios.adminapp

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akstudios.adminapp.dataClasses.GalleryImageData
import com.akstudios.adminapp.databinding.ActivityUploadImageBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class UploadImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadImageBinding
    private lateinit var category: String
    private val REQ = 1
    private var bitmap: Bitmap? = null
    private lateinit var selectedCategory: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var downloadUrl: String = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var items: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        items = arrayListOf("Select Category", "Independence Day", "Republic Day", "Other Events")
        databaseReference = FirebaseDatabase.getInstance().reference.child("Gallery Images")
        storageReference = FirebaseStorage.getInstance().reference.child("Gallery Images")
        progressDialog = ProgressDialog(this)

        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addImage.setOnClickListener {
            openGallery()
        }
        binding.btnDropDown.adapter = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, items)
        binding.btnDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Please choose the category", Toast.LENGTH_LONG).show()
            }
        }


        binding.btnUploadGalleryImage.setOnClickListener {
            if (bitmap == null) {
                Toast.makeText(this, "Please upload image", Toast.LENGTH_LONG).show()
            } else if (selectedCategory == "Select Category") {
                Toast.makeText(this, "Please select category", Toast.LENGTH_LONG).show()
            } else {
                uploadGalleryImage()
            }
        }
    }

    private fun openGallery() {
        val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImage, REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ && resultCode == RESULT_OK) {
            val uri = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.imgGalleryImage.setImageBitmap(bitmap)
        }
    }

    private fun uploadGalleryImage() {
        progressDialog.setMessage("Uploading...")
        progressDialog.show()
        uploadImage()
    }

    private fun uploadImage() {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)

        val finalImage = baos.toByteArray()
        val filePath = storageReference.child(selectedCategory).child("$finalImage jpeg")
        val uploadTask: UploadTask = filePath.putBytes(finalImage)
        uploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                uploadTask.addOnSuccessListener {
                    downloadUrl = filePath.downloadUrl.toString()
                    uploadData()
                }
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadData() {
        databaseReference = databaseReference.child(selectedCategory)
        val uniqueKey = databaseReference.push().key

        // Get date and time
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        val date = currentDate.format(Date())
        val currentTime = SimpleDateFormat("hh:mm a", Locale.UK)
        val time = currentTime.format(Date())
        val galleryImageData = GalleryImageData(uniqueKey, downloadUrl, date, time, selectedCategory)

        if (uniqueKey != null) {
            databaseReference.child(uniqueKey).setValue(galleryImageData).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Could Not Upload Image. PLease try again later", Toast.LENGTH_LONG).show()
            }
        } else {
            progressDialog.dismiss()
            Toast.makeText(this, "Error. Could not found unique key", Toast.LENGTH_LONG).show()
        }
    }
}