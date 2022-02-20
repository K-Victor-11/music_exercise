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

    @Query("SELECT item_default.checked, item_default.idx, item_default.musicCode, item_default.musicTitle_kor, item_default.musicTitle_eng, item_default.image_path, item_default.hertz, items.playTime, items.sortOrder FROM list_default_item as item_default " +
            "INNER JOIN list_items as items ON items.item_idx = item_default.idx " +
            "WHERE items.header_idx =:parentIdx")
    fun getDefaultParentList(parentIdx:Int):LiveData<List<List_DefaultItemDataModel>>
}