package com.exercise.music_exercise.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.service.MusicService

class NotificationUtil {
    companion object{
        const val CHANNEL_ID : String = "channel_exercise"
        const val ACTIONPREVIUOS : String = "actionprevious"
        const val ACTIONPLAY : String = "actionplay"
        const val ACTIONNEXT : String = "actionnext"

        @JvmField
        var mediaNotification : Notification ?= null

        @JvmStatic
        open fun createNotification(context: Context, itemData:List_ItemsDataModel, playButton:Int, pos:Int, size:Int){
            var notificationManagerCompat = NotificationManagerCompat.from(context)
            var notificationManager :NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var mediaSessionCompat: MediaSessionCompat = MediaSessionCompat(context, "tag")

            var pendingPre : PendingIntent? = null
            var drw_previous : Int = 0

            if(pos == 0){
                pendingPre = null
                drw_previous = 0
            } else {
                var intentPre = Intent(context, MusicService::class.java).setAction(ACTIONPREVIUOS)
                pendingPre = PendingIntent.getBroadcast(context, 0, intentPre, PendingIntent.FLAG_UPDATE_CURRENT)
                drw_previous = R.drawable.ic_arrow_left
            }
            var pending: PendingIntent? = null

            var intent = Intent(context, MusicService::class.java)
                    .setAction(ACTIONPLAY)
            pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            var pendingNext : PendingIntent? = null
            var drw_Next : Int = 0

            if(pos == size){
                pendingNext = null
                drw_Next = 0
            } else {
                var intentNext = Intent(context, MusicService::class.java).setAction(ACTIONNEXT)
                pendingNext = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT)
                drw_Next = R.drawable.ic_arrow_right
            }


            mediaNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(itemData.musicTitle_kor)
                    .setContentText("")
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(drw_previous, "Previous", pendingPre)
                    .addAction(playButton, "play", pending)
                    .addAction(drw_Next, "next", pendingNext)
                    .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.sessionToken))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()

            notificationManager.notify(1, mediaNotification!!)
        }
    }
}