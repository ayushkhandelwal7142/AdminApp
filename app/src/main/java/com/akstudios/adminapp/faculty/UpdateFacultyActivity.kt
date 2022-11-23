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
        hindiDept()
        mathsDept()
    }

    private fun englishDept() {
        dbRef = databaseReference.child("English")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    binding.cseNoData.visibility = View.VISIBLE
                    binding.cseRV.visibility = View.GONE
                } else {
                    binding.cseNoData.visibility = View.GONE
                    binding.cseRV.visibility = View.VISIBLE
                    for (i in snapshot.children) {
                        val data = i.getValue(FacultyData::class.java)
                        if (data != null) {
                            list1.add(data)
                        }
                        fAdapter = FacultyAdapter(list1, "English", this@UpdateFacultyActivity)
                        binding.cseRV.apply {
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
    private fun hindiDept() {
        dbRef = databaseReference.child("Hindi")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    binding.eceNoData.visibility = View.VISIBLE
                    binding.eceRV.visibility = View.GONE
                } else {
                    binding.eceNoData.visibility = View.GONE
                    binding.eceRV.visibility = View.VISIBLE
                    for (i in snapshot.children) {
                        val data = i.getValue(FacultyData::class.java)
                        if (data != null) {
                            list2.add(data)
                        }
                        fAdapter = FacultyAdapter(list2, "Hindi",  this@UpdateFacultyActivity)
                        binding.eceRV.apply {
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
                    binding.meNoData.visibility = View.VISIBLE
                    binding.meRV.visibility = View.GONE
                } else {
                    binding.meNoData.visibility = View.GONE
                    binding.meRV.visibility = View.VISIBLE
                    for (i in snapshot.children) {
                        val data = i.getValue(FacultyData::class.java)
                        if (data != null) {
                            list3.add(data)
                        }
                        fAdapter = FacultyAdapter(list3, "Maths", this@UpdateFacultyActivity)
                        binding.meRV.apply {
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