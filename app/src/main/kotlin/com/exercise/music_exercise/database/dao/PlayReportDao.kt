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

    @Query("SELECT COUNT(musicTitle) AS playCount, * FROM play_report WHERE substr(playDate, 0,7)=:month GROUP BY playDate, musicTitle, hertz")
    fun getPlayReportItem(month:String) : LiveData<List<PlayReportDataModel>>

    @Query("SELECT * FROM play_report WHERE substr(playDate, 0, 7) = :month GROUP BY playDate")
    fun getPlayReportDate(month:String) : LiveData<List<PlayReportDataModel>>
}