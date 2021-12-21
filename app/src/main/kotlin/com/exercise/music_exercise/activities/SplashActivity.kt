package com.exercise.music_exercise.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.database.AppDataBase
import com.exercise.music_exercise.database.dao.MusicListDao
import com.exercise.music_exercise.utils.PreferenceUtils

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()
        startMain()
    }

    var callback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //DO AS NEEDED
            Log.d("kamuel", "onCreate!!!!")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            //DO AS NEEDED
            Log.d("kamuel", "onOpen!!!!")
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Log.d("kamuel", "onDestructiveMigration!!!!")
        }
    }

    fun startMain(){
        Handler(Looper.getMainLooper()).postDelayed({
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    fun init(){
        var prefUtils: PreferenceUtils = PreferenceUtils.getInstance(this)
        if (!prefUtils.getBoolean("default_data", false)) {
            var groupList:ArrayList<List_HeaderDataModel> = arrayListOf()
            groupList.add(List_HeaderDataModel("개울소리1","개울소리1","water_01","D"))
            groupList.add(List_HeaderDataModel("개울소리2","개울소리2","water_02","D"))
            groupList.add(List_HeaderDataModel("개울소리3","개울소리3","water_03","D"))
            groupList.add(List_HeaderDataModel("개울소리4","개울소리4","water_01", "D"))
            groupList.add(List_HeaderDataModel("개울소리5","개울소리5","water_02","D"))
            groupList.add(List_HeaderDataModel("개울소리6","개울소리6","water_03","D"))
            groupList.add(List_HeaderDataModel("개울소리7","개울소리7","water_01","D"))

            var musicDao = AppDataBase.getInstance(this, callback).musicListDao()
            var musicDetailDao = AppDataBase.getInstance(this, callback).musicListDetailDao()

            groupList.forEachIndexed { index, musicListDataModel ->
                musicDao.insert(musicListDataModel)
            }

            musicDao.getGroupList()?.observe(this, Observer {
                it.forEach {
                    var parentIdx = it.idx
                    var parentTitle_kor = it.listTitle_kor
                    var parentTitle_eng = it.listTitle_eng
                    var parentImage = it.image_path

                    musicDetailDao.insert(List_ItemsDataModel(parentIdx, "", parentTitle_kor, parentTitle_eng,"${parentImage}_01",0))
                    musicDetailDao.insert(List_ItemsDataModel(parentIdx, "", parentTitle_kor, parentTitle_eng,"${parentImage}_02",2))
                    musicDetailDao.insert(List_ItemsDataModel(parentIdx, "", parentTitle_kor, parentTitle_eng,"${parentImage}_03",4))
                    musicDetailDao.insert(List_ItemsDataModel(parentIdx, "", parentTitle_kor, parentTitle_eng,"${parentImage}_04",8))
                }
            })

            prefUtils.setBoolean("default_data", true)
        }
    }
}