package com.exercise.music_exercise.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.data_models.PlayReportDataModel
import com.exercise.music_exercise.database.dao.MusicListDao
import com.exercise.music_exercise.database.dao.MusicListDetailDao
import com.exercise.music_exercise.database.dao.PlayReportDao

class AppRepository(application: Application) {
    private var musicListDao : MusicListDao
    private var musicDetailListDao : MusicListDetailDao
    private var playReportDao:PlayReportDao

    init {
        val database = AppDataBase.getInstance(application)!!
        musicListDao = (database as AppDataBase).musicListDao()
        musicDetailListDao = (database as AppDataBase).musicListDetailDao()
        playReportDao = (database as AppDataBase).playReportDao()
    }

    fun getMusicGroupList():LiveData<List<List_HeaderDataModel>>{
        return musicListDao.getGroupList()
    }

    fun getMusicGroupListForCustom():LiveData<List<List_HeaderDataModel>>{
        return musicListDao.getGroupListForCustom()
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
        musicListDao.insert(List_HeaderDataModel(title, title, "", "C"))
    }

    fun setGroupTitle(title:String, type:String){
        musicListDao.insert(List_HeaderDataModel(title, title, "", "C"))
    }

    fun setMusicDetail(parentIndex:Int, data : List_ItemsDataModel){
        musicDetailListDao.insert(List_ItemsDataModel(parentIndex, data.musicCode, data.musicTitle_kor, data.musicTitle_eng, data.image_path, data.hertz, data.playTime, data.sortOrder))
    }

    fun getGroupLastIndex():Int{
        return musicListDao.getLastGroupList()
    }

    fun setPlayReport(playReportData:PlayReportDataModel){
        playReportDao.insert(playReportData)
    }

    fun getPlayReportGroup(date:String):LiveData<List<PlayReportDataModel>>{
        return playReportDao.getPlayReportDate(date)
    }

    fun getPlayReportItem(date:String):LiveData<List<PlayReportDataModel>>{
        return playReportDao.getPlayReportItem(date)
    }
}