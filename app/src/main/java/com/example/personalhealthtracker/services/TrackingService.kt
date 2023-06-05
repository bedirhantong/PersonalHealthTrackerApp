package com.example.personalhealthtracker.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.other.Constants.ACTION_PAUSE_SERVICE
import com.example.personalhealthtracker.other.Constants.ACTION_SHOW_TRACK_RUNNING_FRAGMENT
import com.example.personalhealthtracker.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.personalhealthtracker.other.Constants.ACTION_STOP_SERVICE
import com.example.personalhealthtracker.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.personalhealthtracker.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.personalhealthtracker.other.Constants.NOTIFICATION_ID
import com.example.personalhealthtracker.ui.MainActivity
import timber.log.Timber

class TrackingService :  LifecycleService(){

    var isFirstRun :Boolean = true

    companion object {
        /*
        A life data object
        - Whenever the tracking state changes we will pass the data
         */
        val isTracking = MutableLiveData<Boolean>()

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        Timber.d("Resuming Service")
                    }
                }ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    // to start our foreground service
    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*
        What is really Notification Manager
        - It is a system service of Android framework and we need it whenever we
        want to show our notification
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        // we always want to be the notification to be active
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.baseline_directions_run_24)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }


    /*
    Whenever you touch the notification you will directed to the running activity that is currently
    on progress
     */
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACK_RUNNING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )



    /*
    Just because NotificationChannel can only be created above
    Android Orea or Android Oreo
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }
}