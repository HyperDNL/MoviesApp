package com.prueba.moviestest.utils

import android.app.Activity
import android.content.Intent

object NavigationUtil {
    fun redirectToDestination(activity: Activity, destination: Class<*>) {
        val intent = Intent(activity, destination)
        activity.startActivity(intent)
        activity.finishAffinity()
    }
}