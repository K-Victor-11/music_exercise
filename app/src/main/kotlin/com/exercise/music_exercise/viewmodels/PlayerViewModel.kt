package com.exercise.music_exercise.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel

class PlayerViewModel : ViewModel() {

    var title : String = "음원 타이틀"
    var selectPos : Int = 0

    var _playList : MutableLiveData<ArrayList<List_ItemsDataModel>> = MutableLiveData()
    val playList : LiveData<ArrayList<List_ItemsDataModel>>
        get() = _playList

    fun setPlayList(list:ArrayList<List_ItemsDataModel>){
        _playList.postValue(list)
    }


}