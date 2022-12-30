package com.akstudios.adminapp.services

import android.content.Context
import android.widget.Toast
import com.akstudios.adminapp.api.ApiUtilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Notification {
    fun sendNotification(context: Context, notification: PushNotifications) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(object:
            Callback<PushNotifications> {
            override fun onResponse(
                call: Call<PushNotifications>,
                response: Response<PushNotifications>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Notification sent successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Notification sending unsuccessful", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<PushNotifications>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}