package com.exercise.music_exercise.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel

class AddListSettingViewModel:ViewModel() {
    var _setList = MutableLiveData<ArrayList<List_DefaultItemDataModel>>()
    val setList : LiveData<ArrayList<List_DefaultItemDataModel>>
        get() = _setList


    fun setSettingList(list:ArrayList<List_DefaultItemDataModel>){
        _setList.postValue(list)
    }
}