package com.exercise.music_exercise.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query
import com.exercise.music_exercise.data_models.List_HeaderDataModel

@Dao
interface MusicListDao:BaseDao<List_HeaderDataModel> {

    @Query("SELECT * FROM list_header")
    fun getGroupList() : LiveData<List<List_HeaderDataModel>>

    @Query("SELECT * FROM list_header WHERE idx = :index")
    fun getGroupList(index:Int) : LiveData<List<List_HeaderDataModel>>

    @Query("SELECT * FROM list_header ORDER BY idx ASC LIMIT 1")
    fun getLastGroupList() : LiveData<List<List_HeaderDataModel>>

}