package com.exercise.music_exercise.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.database.AppDataBase
import com.exercise.music_exercise.utils.PreferenceUtils

class SplashActivity : AppCompatActivity() {
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

    fun startMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    fun init() {
        var prefUtils: PreferenceUtils = PreferenceUtils.getInstance(this)
        if (!prefUtils.getBoolean("default_data", false)) {
            var groupList: ArrayList<List_HeaderDataModel> = arrayListOf()
            groupList.add(List_HeaderDataModel("개울1", "개울1", "water_01", "D"))
            groupList.add(List_HeaderDataModel("개울2", "개울2", "water_02", "D"))
            groupList.add(List_HeaderDataModel("개울3", "개울3", "water_03", "D"))
            groupList.add(List_HeaderDataModel("개울4", "개울4", "water_04", "D"))
            groupList.add(List_HeaderDataModel("개울5", "개울5", "water_05", "D"))
            groupList.add(List_HeaderDataModel("비1", "비1", "rain_01", "D"))
            groupList.add(List_HeaderDataModel("비2", "비2", "rain_02", "D"))
            groupList.add(List_HeaderDataModel("비3", "비3", "rain_03", "D"))
            groupList.add(List_HeaderDataModel("바람1", "바람1", "wind_01", "D"))
            groupList.add(List_HeaderDataModel("바람2", "바람2", "wind_02", "D"))
            groupList.add(List_HeaderDataModel("파도1", "파도1", "wave_01", "D"))
            groupList.add(List_HeaderDataModel("파도2", "파도2", "wave_02", "D"))
            groupList.add(List_HeaderDataModel("화이트노이즈1", "화이트노이즈1", "pink_noise", "D"))
            groupList.add(List_HeaderDataModel("화이트노이즈2", "화이트노이즈2", "white_noise", "D"))
            groupList.add(List_HeaderDataModel("치료음악1", "치료음악1", "music_01", "D"))
            groupList.add(List_HeaderDataModel("치료음악2", "치료음악2", "music_02", "D"))
            groupList.add(List_HeaderDataModel("치료음악3", "치료음악3", "music_03", "D"))
            groupList.add(List_HeaderDataModel("원본", "원본", "", "CD"))
            groupList.add(List_HeaderDataModel("2KHz", "2KHz", "", "CD"))
            groupList.add(List_HeaderDataModel("4KHz", "4KHz", "", "CD"))
            groupList.add(List_HeaderDataModel("8KHz", "8KHz", "", "CD"))

            var musicDao = AppDataBase.getInstance(this, callback).musicListDao()
            var musicDefault = AppDataBase.getInstance(this, callback).musicListDefaultDetailDao()
            var musicDetailDao = AppDataBase.getInstance(this, callback).musicListDetailDao()


            groupList.forEachIndexed { index, musicListDataModel ->
                musicDao.insert(musicListDataModel)

                if(musicListDataModel.customType == "D") {
                    musicDefault.insert(
                        List_DefaultItemDataModel(
                            0,
                            "",
                            musicListDataModel.listTitle_kor,
                            musicListDataModel.listTitle_eng,
                            "${musicListDataModel.image_path}_01",
                            0
                        )
                    )
                    musicDefault.insert(
                        List_DefaultItemDataModel(
                            0,
                            "",
                            musicListDataModel.listTitle_kor,
                            musicListDataModel.listTitle_eng,
                            "${musicListDataModel.image_path}_02",
                            2
                        )
                    )
                    musicDefault.insert(
                        List_DefaultItemDataModel(
                            0,
                            "",
                            musicListDataModel.listTitle_kor,
                            musicListDataModel.listTitle_eng,
                            "${musicListDataModel.image_path}_03",
                            4
                        )
                    )
                    musicDefault.insert(
                        List_DefaultItemDataModel(
                            0,
                            "",
                            musicListDataModel.listTitle_kor,
                            musicListDataModel.listTitle_eng,
                            "${musicListDataModel.image_path}_04",
                            8
                        )
                    )
                }
            }

            musicDao.getGroupAllList()?.observe(this, Observer {
                it.forEach {
                    var parentIdx = it.idx
                    var parentTitle_kor = it.listTitle_kor
                    var parentTitle_eng = it.listTitle_eng
                    var parentImage = it.image_path
                    var parentType = it.customType

                    if (parentType == "D") {
                        Log.d("kamuel", "DefaultTitleName ::: ${parentTitle_kor}")
                        musicDefault.getDefaultItemList(parentTitle_kor).observe(this, Observer {
                            Log.d("kamuel", "DefaultItemListSize ::: ${it.size}")
                            it.forEachIndexed { index, listDefaultitemdatamodel ->
                                musicDetailDao.insert(
                                    List_ItemsDataModel(
                                        0,
                                        parentIdx,
                                        listDefaultitemdatamodel.idx,
                                        1,
                                        index
                                    )
                                )
                            }
                        })
                    } else{
                        musicDao.getGroupListForCustom(parentTitle_kor)?.observe(this, Observer {
                            it.forEach {
                                var parentIdx = it.idx
                                var parentTitle_kor = it.listTitle_kor
                                var parentTitle_eng = it.listTitle_eng
                                var parentImage = it.image_path

                                var hertz = 0
                                if (parentTitle_kor == "원본") {
                                    hertz = 0
                                } else if (parentTitle_kor == "2KHz") {
                                    hertz = 2
                                } else if (parentTitle_kor == "4KHz") {
                                    hertz = 4
                                } else if (parentTitle_kor == "8KHz") {
                                    hertz = 8
                                }

                                musicDefault.getDefaultHertzList(hertz).observe(this, Observer {
                                    it.forEachIndexed { index, listDefaultitemdatamodel ->
                                        musicDetailDao.insert(
                                            List_ItemsDataModel(
                                                0,
                                                parentIdx,
                                                listDefaultitemdatamodel.idx,
                                                1,
                                                index
                                            )
                                        )
                                    }
                                })
                            }
                        })
                    }


                }
            })

            prefUtils.setBoolean("default_data", true)
        }
    }
}