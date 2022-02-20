package com.exercise.music_exercise.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.exercise.music_exercise.data_models.List_HeaderDataModel

@Dao
interface MusicListDao:BaseDao<List_HeaderDataModel> {

    @Query("SELECT * FROM list_header WHERE customType='D'")
    fun getGroupList() : LiveData<List<List_HeaderDataModel>>

    @Query("SELECT * FROM list_header WHERE idx = :index")
    fun getGroupList(index:Int) : LiveData<List<List_HeaderDataModel>>

    @Query("SELECT * FROM list_header WHERE customType = 'C'")
    fun getGroupListForCustom() : LiveData<List<List_HeaderDataModel>>

    @Query("SELECT * FROM list_header WHERE customType = 'C' AND listTitle_kor=:title")
    fun getGroupListForCustom(title:String) : LiveData<List<List_HeaderDataModel>>

    @Query("SELECT idx FROM list_header ORDER BY idx desc LIMIT 1")
    fun getLastGroupList() : Int

    @Query("DELETE FROM list_header WHERE idx = :index")
    fun delCustomGroup(index:Int)

}