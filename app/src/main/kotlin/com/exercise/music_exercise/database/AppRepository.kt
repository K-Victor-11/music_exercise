package com.exercise.music_exercise.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.database.dao.MusicListDao
import com.exercise.music_exercise.database.dao.MusicListDetailDao

class AppRepository(application: Application) {
    private var musicListDao : MusicListDao
    private var musicDetailListDao : MusicListDetailDao

    init {
        val database = AppDataBase.getInstance(application)!!
        musicListDao = (database as AppDataBase).musicListDao()
        musicDetailListDao = (database as AppDataBase).musicListDetailDao()
    }

    fun getMusicGroupList():LiveData<List<List_HeaderDataModel>>{
        return musicListDao.getGroupList()
    }

    fun getMusicGroupList(index:Int):LiveData<List<List_HeaderDataModel>>{
        return musicListDao.getGroupList(index)
    }

    fun getMusicDetailList(parentIdx:Int):LiveData<List<List_ItemsDataModel>>{
        return musicDetailListDao.getDetailList(parentIdx)
    }

    fun getMusicDetailList():LiveData<List<List_ItemsDataModel>>{
        return musicDetailListDao.getDetailList()
    }

    fun setGroupTitle(title:String){
        musicListDao.insert(List_HeaderDataModel(title, title, "C"))
    }

    fun setGroupTitle(title:String, type:String){
        musicListDao.insert(List_HeaderDataModel(title, title, "C"))
    }
}