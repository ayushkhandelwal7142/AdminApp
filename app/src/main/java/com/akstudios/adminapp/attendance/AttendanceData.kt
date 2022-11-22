package com.akstudios.adminapp.attendance

data class AttendanceData(val studentName: String = "", val rollNo: String = "", val gender:String = "", val sClass: String = "", var attendance: Boolean = false, var uniqueKey: String = "")