package com.akstudios.adminapp.attendance

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akstudios.adminapp.R

class AttendanceAdapter(val context: Context, private val list:List<AttendanceData>): RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rollNo: TextView = itemView.findViewById(R.id.rollNo)
        val gender: TextView = itemView.findViewById(R.id.gender)
        val studentName: TextView = itemView.findViewById(R.id.studentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.attendence_list, parent, false, )
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        holder.rollNo.text = list[position].rollNo
        holder.gender.text = list[position].gender
        holder.studentName.text = list[position].studentName
    }

    override fun getItemCount(): Int {
        return list.size
    }
}