package com.exercise.music_exercise.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.exercise.music_exercise.data_models.*
import com.exercise.music_exercise.database.dao.*

class AppRepository(application: Application) {
    private var musicListDao : MusicListDao
    private var musicDetailListDao : MusicListDetailDao
    private var musicDefaultDao : MusicListDefaultDetailDao
    private var playReportDao:PlayReportDao
//    private var customListDao : CustomListDetailDao

    init {
        val database = AppDataBase.getInstance(application)!!
        musicListDao = (database as AppDataBase).musicListDao()
        musicDetailListDao = (database as AppDataBase).musicListDetailDao()
        musicDefaultDao = (database as AppDataBase).musicListDefaultDetailDao()
        playReportDao = (database as AppDataBase).playReportDao()
//        customListDao = (database as AppDataBase).customListDetailDao()
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

    fun getMusicDefaultDetailList(idx:Int):LiveData<List<List_DefaultItemDataModel>>{
        return musicDefaultDao.getDefaultItem(idx)
    }

    fun getMusicDefaultDetailAllList():LiveData<List<List_DefaultItemDataModel>>{
        return musicDefaultDao.getDefaultAllList()
    }

    fun getMusicDefaultParentDetailList(parentIdx:Int):LiveData<List<List_DefaultItemDataModel>>{
        return musicDefaultDao.getDefaultParentList(parentIdx)
    }

    fun setGroupTitle(title:String){
        musicListDao.insert(List_HeaderDataModel(title, title, "", "C"))
    }

    fun setGroupTitle(title:String, type:String){
        musicListDao.insert(List_HeaderDataModel(title, title, "", "C"))
    }

    fun setGroupTitle(title:String, idx:Int){
        var updateData : List_HeaderDataModel = List_HeaderDataModel(title, title, "", "C")
        updateData.idx = idx
        musicListDao.update(updateData)
    }

    fun setMusicDetail(parentIndex:Int, data : List_DefaultItemDataModel){
        musicDetailListDao.insert(List_ItemsDataModel(0, parentIndex, data.idx, data.playTime, data.sortOrder))
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

    fun delCustomListHeader(idx:Int){
        musicListDao.delCustomGroup(idx)
        musicDetailListDao.deleteDetailList(idx)
    }
}