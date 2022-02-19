package com.exercise.music_exercise.data_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "list_default_item")
data class List_DefaultItemDataModel(
    @PrimaryKey(autoGenerate = true)
    val idx:Int = 0,
    val musicCode:String,
    val musicTitle_kor:String,
    val musicTitle_eng:String,
    val image_path:String,
    val hertz:Int,
    var playTime:Int = 1,
    var sortOrder:Int = -1
):Serializable{
    var checked:Boolean = false

}