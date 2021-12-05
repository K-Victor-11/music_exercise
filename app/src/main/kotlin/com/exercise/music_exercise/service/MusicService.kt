package com.exercise.music_exercise.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MusicService : BroadcastReceiver()  {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null && intent != null) {
            context!!.sendBroadcast(Intent("TRACKS_TRACKS")
                    .putExtra("actionname", intent!!.action))
        }
    }
}