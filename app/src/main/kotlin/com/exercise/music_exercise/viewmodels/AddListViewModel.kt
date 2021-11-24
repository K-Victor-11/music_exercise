package com.exercise.music_exercise.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.database.AppRepository

class AddListViewModel(application:Application):AndroidViewModel(application) {
    val appRepository by lazy {
        AppRepository(application)
    }

    var topTitle:String = ""
    var addTitle:String = ""
    var itemList = ArrayList<List_ItemsDataModel>()
    var step:Int = 1;

    fun getGroupInfo(index:Int):List_HeaderDataModel?{
        var list = appRepository.getMusicGroupList(index)

        if(list != null && list.value != null && list.value!!.size > 0){
            return list.value!!.get(0)
        } else
            return null
    }


    fun setGroupTitle(title:String){
        appRepository.setGroupTitle(title, "C")
    }
    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddListViewModel(application) as T
        }
    }
}