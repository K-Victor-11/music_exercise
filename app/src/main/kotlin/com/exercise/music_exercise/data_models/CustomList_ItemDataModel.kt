package com.exercise.music_exercise.data_models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
        tableName = "custom_list_item",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = List_HeaderDataModel::class,
                        parentColumns = arrayOf("idx"),
                        childColumns = arrayOf("header_idx"),
                        onDelete = ForeignKey.CASCADE
                ),
                ForeignKey(
                        entity = List_ItemsDataModel::class,
                        parentColumns = arrayOf("idx"),
                        childColumns = arrayOf("item_idx")
                )
        ),
        indices = [
            Index("idx"),
            Index("header_idx"),
            Index("item_idx")
        ]
)
data class CustomList_ItemDataModel(
        val header_idx:Int,
        val item_idx:Int,
        var playTime:Int = 1,
        var sortOrder:Int = -1
): Serializable{
    @PrimaryKey(autoGenerate = true)
    var idx : Int = 0
    var checked:Boolean = false
}
