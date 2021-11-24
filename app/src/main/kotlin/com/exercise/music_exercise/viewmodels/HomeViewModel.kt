package com.exercise.music_exercise.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.database.AppDataBase
import com.exercise.music_exercise.database.AppRepository
import com.exercise.music_exercise.database.dao.MusicListDao

class HomeViewModel(application: Application): AndroidViewModel(application) {

    var _musicList = MutableLiveData<List<List_HeaderDataModel>>()
    var musicList : LiveData<List<List_HeaderDataModel>> ?= null

    val appRepository by lazy {
        AppRepository(application)
    }

    fun getMusicList(context:Context) : LiveData<List<List_HeaderDataModel>>{
        return appRepository.getMusicGroupList()
    }

    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }
}