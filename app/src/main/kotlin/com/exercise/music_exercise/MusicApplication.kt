package com.exercise.music_exercise

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDexApplication

class MusicApplication: MultiDexApplication() {
    companion object{
        @JvmField
        var instance:MusicApplication ?= null

        @JvmField
        var currentActivity: Activity?=null

        @JvmStatic
        fun getInstance():MusicApplication?{
            return instance
        }

        @JvmStatic
        fun setCurrentActivity(activity: Activity){
            currentActivity = activity
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        /** DB 생성 **/
//        AppDataBase.getInstance(this, object:RoomDatabase.Callback(){
//
//        })

        registerActivityLifecycleCallbacks(ActivityLifeCyclerCallbacks())
    }


    class ActivityLifeCyclerCallbacks : ActivityLifecycleCallbacks{
        override fun onActivityCreated(activity: Activity, p1: Bundle?) {
            setCurrentActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            setCurrentActivity(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            setCurrentActivity(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            setCurrentActivity(activity)
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

    }
}