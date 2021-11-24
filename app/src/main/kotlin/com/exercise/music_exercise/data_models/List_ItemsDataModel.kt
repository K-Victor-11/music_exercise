package com.exercise.music_exercise.data_models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "list_items",
    foreignKeys = arrayOf(
            ForeignKey(
                entity = List_HeaderDataModel::class,
                parentColumns = arrayOf("idx"),
                childColumns = arrayOf("header_idx"),
                onDelete = ForeignKey.CASCADE
            )
    ),
    indices = [
        Index("idx"),
        Index("header_idx")
    ]
)
data class List_ItemsDataModel(
    val header_idx:Int,
    val musicCode:String,
    val musicTitle_kor:String,
    val musicTitle_eng:String,
    val hertz:Int,
    val playTime:Int,
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var idx:Int = 0
    var checked:Boolean = false
    var sortOrder:Int = -1
}
