package com.akstudios.adminapp.faculty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.R
import com.akstudios.adminapp.databinding.ActivityUpdateFacultyBinding
import com.google.firebase.database.*

class UpdateFacultyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateFacultyBinding
    private lateinit var cseDept: RecyclerView
    private lateinit var eceDept: RecyclerView
    private lateinit var mechDept: RecyclerView
    private lateinit var cseNoData: LinearLayout
    private lateinit var eceNoData: LinearLayout
    private lateinit var mechNoData: LinearLayout
    private lateinit var list1: ArrayList<FacultyData>
    private lateinit var list2: ArrayList<FacultyData>
    private lateinit var list3: ArrayList<FacultyData>
    private lateinit var list4: ArrayList<FacultyData>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dbRef: DatabaseReference
    private lateinit var fAdapter: FacultyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_faculty)

        binding = ActivityUpdateFacultyBinding.inflate(layoutInflater)
        setContentView(binding.root)

          list1 = arrayListOf()
          list2 = arrayListOf()
          list3 = arrayListOf()
          list4 = arrayListOf()

        binding.addFaculty.setOnClickListener {
            val intent = Intent(this, AddNewFacultyActivity::class.java)
            startActivity(intent)
        }
        databaseReference = FirebaseDatabase.getInstance().reference.child("Faculty")

        englishDept()
        scienceDept()
        mathsDept()
    }

    private fun englishDept() {
        dbRef = databaseReference.child("English")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    binding.engNoData.visibility = View.VISIBLE
                    binding.engRV.visibility = View.GONE
                } else {
                    binding.engNoData.visibility = View.GONE
                    binding.engRV.visibility = View.VISIBLE
                    for (i in snapshot.children) {
                        val data = i.getValue(FacultyData::class.java)
                        if (data != null) {
                            list1.add(data)
                        }
                        fAdapter = FacultyAdapter(list1, "English", this@UpdateFacultyActivity)
                        binding.engRV.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this@UpdateFacultyActivity)
                            adapter = fAdapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun scienceDept() {
        dbRef = databaseReference.child("Science")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    binding.sciNoData.visibility = View.VISIBLE
                    binding.sciRV.visibility = View.GONE
                } else {
                    binding.sciNoData.visibility = View.GONE
                    binding.sciRV.visibility = View.VISIBLE
                    for (i in snapshot.children) {
                        val data = i.getValue(FacultyData::class.java)
                        if (data != null) {
                            list2.add(data)
                        }
                        fAdapter = FacultyAdapter(list2, "Science",  this@UpdateFacultyActivity)
                        binding.sciRV.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this@UpdateFacultyActivity)
                            adapter = fAdapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun mathsDept() {
        dbRef = databaseReference.child("Maths")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    binding.mathsNoData.visibility = View.VISIBLE
                    binding.mathsRV.visibility = View.GONE
                } else {
                    binding.mathsNoData.visibility = View.GONE
                    binding.mathsRV.visibility = View.VISIBLE
                    for (i in snapshot.children) {
                        val data = i.getValue(FacultyData::class.java)
                        if (data != null) {
                            list3.add(data)
                        }
                        fAdapter = FacultyAdapter(list3, "Maths", this@UpdateFacultyActivity)
                        binding.mathsRV.apply {
                            setHasFixedSize(true)
                            layoutManager = LinearLayoutManager(this@UpdateFacultyActivity)
                            adapter = fAdapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}