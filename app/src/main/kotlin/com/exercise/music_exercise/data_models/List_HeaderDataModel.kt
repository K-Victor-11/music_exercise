package com.exercise.music_exercise.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "list_header")
data class List_HeaderDataModel(
        val listTitle_kor:String,
        val listTitle_eng:String,
        val image_path:String,
        val customType:String/** D:Default, C:Custom **/
):Serializable{
        @PrimaryKey(autoGenerate = true)
        var idx:Int = 0
}