package com.exercise.music_exercise.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.data_models.List_ItemsDataModel

class PlayerViewModel(val application: Application) : ViewModel() {

    var title : String = "음원 타이틀"
    var selectPos : Int = 0
    var groupType : String = "D"

    var _playList : MutableLiveData<ArrayList<List_ItemsDataModel>> = MutableLiveData()
    val playList : LiveData<ArrayList<List_ItemsDataModel>>
        get() = _playList

    fun setPlayList(list:ArrayList<List_ItemsDataModel>){
        _playList.postValue(list)
    }

    fun changePlayTime(playTime:Int){
        _playList.value?.get(selectPos)!!.playTime = playTime
    }


    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PlayerViewModel(application) as T
        }
    }
}