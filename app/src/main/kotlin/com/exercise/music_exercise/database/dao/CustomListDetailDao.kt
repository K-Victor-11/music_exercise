package com.exercise.music_exercise.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.exercise.music_exercise.data_models.CustomList_ItemDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel

@Dao
interface CustomListDetailDao:BaseDao<CustomList_ItemDataModel> {
    @Query("SELECT list_items.* FROM custom_list_item " +
            "INNER JOIN list_items ON list_items.idx = custom_list_item.item_idx " +
            "WHERE custom_list_item.header_idx = :header_idx")
    fun getCustomListDetail(header_idx:Int) : LiveData<List<List_ItemsDataModel>>

    @Query("DELETE FROM custom_list_item WHERE header_idx = :header_idx")
    fun deleteCustomListDetail(header_idx: Int)
}