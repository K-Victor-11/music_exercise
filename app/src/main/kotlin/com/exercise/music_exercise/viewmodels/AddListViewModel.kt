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

    /** 선택 메뉴 타이틀 **/
    var topTitle:String = ""
    /** 저장 타이틀 **/
    var addTitle:String = ""
    var itemList = ArrayList<List_ItemsDataModel>()
    var selectItemList : LinkedHashMap<Int, List_ItemsDataModel> = LinkedHashMap()
    private var step:Int = 0

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

    fun setMusicItem(parentIdx:Int, data:List_ItemsDataModel){
        appRepository.setMusicDetail(parentIdx, data)
    }

    fun getGroupLastIndex():Int{
        return appRepository.getGroupLastIndex()
    }

    fun checkSelectList(idx:Int, data:List_ItemsDataModel, isCheck:Boolean){
        if(selectItemList != null){
            if(isCheck && !selectItemList.containsKey(idx)){
                selectItemList.put(idx, data)
            } else {
                if(!isCheck && selectItemList.containsKey(idx))
                    selectItemList.remove(idx)
            }

            var order:Int = 0
            for (key in selectItemList.keys) {
                selectItemList.get(key)!!.sortOrder = order
                order ++
            }
        }
    }

    fun setStep(step:Int){
        this.step = step
    }

    fun getStep():Int{
        return this.step
    }

    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddListViewModel(application) as T
        }
    }
}