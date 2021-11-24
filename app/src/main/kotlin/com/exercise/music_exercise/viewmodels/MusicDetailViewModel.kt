package com.exercise.music_exercise.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.database.AppRepository

class MusicDetailViewModel(application:Application):AndroidViewModel(application) {
    val appRepository by lazy {
        AppRepository(application)
    }

    fun getDetailList(parentIdx:Int): LiveData<List<List_ItemsDataModel>>{
        if(parentIdx == -1)
            return appRepository.getMusicDetailList()
        else
            return appRepository.getMusicDetailList(parentIdx)
    }

    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MusicDetailViewModel(application) as T
        }
    }
}