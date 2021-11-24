package com.exercise.music_exercise.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity:T):Long

    @Update
    fun update(entity:T):Int

    @Delete
    fun delete(entity:T):Int
}