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
        ),
        ForeignKey(
            entity = List_DefaultItemDataModel::class,
            parentColumns = arrayOf("idx"),
            childColumns = arrayOf("item_idx"),
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = [
        Index("idx"),
        Index("header_idx"),
        Index("item_idx")
    ]
)
data class List_ItemsDataModel(
    @PrimaryKey(autoGenerate = true) val idx:Int = 0,
    var header_idx:Int,
    var item_idx:Int,
    var playTime:Int = 1,
    var sortOrder:Int = -1,
):Serializable{
    var checked:Boolean = false
}
