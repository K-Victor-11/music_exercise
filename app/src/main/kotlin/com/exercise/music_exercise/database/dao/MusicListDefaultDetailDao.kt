package com.exercise.music_exercise.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_HeaderDataModel

@Dao
interface MusicListDefaultDetailDao:BaseDao<List_DefaultItemDataModel> {
    @Query("SELECT * FROM list_default_item WHERE musicTitle_kor=:title OR musicTitle_eng=:title")
    fun getDefaultItemList(title:String) : LiveData<List<List_DefaultItemDataModel>>

    @Query("SELECT * FROM list_default_item WHERE idx=:index")
    fun getDefaultItem(index:Int):LiveData<List<List_DefaultItemDataModel>>

    @Query("SELECT * FROM list_default_item")
    fun getDefaultAllList():LiveData<List<List_DefaultItemDataModel>>

    @Query("SELECT * FROM list_default_item WHERE idx in (SELECT item_idx FROM list_items WHERE header_idx=:parentIdx)")
    fun getDefaultParentList(parentIdx:Int):LiveData<List<List_DefaultItemDataModel>>
}