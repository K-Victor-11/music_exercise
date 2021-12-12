package com.exercise.music_exercise.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.PlayReportDataModel

@Dao
interface PlayReportDao:BaseDao<PlayReportDataModel> {
    @Query("SELECT * FROM play_report")
    fun getPlayReportData() : LiveData<List<PlayReportDataModel>>
}