package com.akstudios.adminapp.attendance

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.database.FirebaseDatabase

class AttendanceAdapter(val context: Context, private val list:List<AttendanceData>): RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rollNo: TextView = itemView.findViewById(R.id.rollNo)
        val gender: TextView = itemView.findViewById(R.id.gender)
        val studentName: TextView = itemView.findViewById(R.id.studentName)
        val studentCardView: MaterialCardView = itemView.findViewById(R.id.studentCardView)
        val checkAttendance: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.attendence_list, parent, false,)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.rollNo.text = list[position].rollNo
        holder.gender.text = list[position].gender
        holder.studentName.text = list[position].studentName
        if (position % 2 == 0) {
            holder.studentCardView.setBackgroundColor(Color.parseColor("#FFBAB1B1"))
        } else {
            holder.studentCardView.setBackgroundColor(Color.parseColor("#FF99CCD3"))
        }
       /* AttendanceActivity().btnAddNewStudent.setOnClickListener {
            val currentData = AttendanceActivity().date
            val alertDialog = android.app.AlertDialog.Builder(context)
            alertDialog.apply {
                setTitle("Mark Attendance")
                setMessage("Do you want to mark or update attendance for $currentData ?")
                setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val reference = FirebaseDatabase.getInstance().reference.child("StudentData")
                        val hp = HashMap<String, String>()
                        if (holder.checkAttendance.isChecked) {
                            hp["attendance"] = true.toString()
                        } else {
                            hp["attendance"] = false.toString()
                        }
                        reference.child(list[position].uniqueKey).updateChildren(hp as Map<String, Any>).addOnSuccessListener {
                            Toast.makeText(context, "Attendance marked successfully", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                    }

                })
            }
        } */
    }

    override fun getItemCount(): Int {
        return list.size
    }
}