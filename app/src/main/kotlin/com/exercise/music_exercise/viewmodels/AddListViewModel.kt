package com.exercise.music_exercise.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.activities.BaseActivity
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
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
    var itemList = ArrayList<List_DefaultItemDataModel>()
    var selectItemList : LinkedHashMap<Int, List_DefaultItemDataModel> = LinkedHashMap()
    private var step:Int = 0
    var isEditMode:Boolean = false

    var _headerDataModel : MutableLiveData<List<List_HeaderDataModel>> = MutableLiveData()
    val headerDataModel : LiveData<List<List_HeaderDataModel>>
        get() = _headerDataModel

    fun getGroupInfo(index:Int){
        appRepository.getMusicGroupList(index).observe(MusicApplication.currentActivity as BaseActivity, Observer {
            if (it.size > 0)
                _headerDataModel.postValue(it)
        })
    }

    fun setGroupTitle(title:String){
        appRepository.setGroupTitle(title, "C")
    }

    fun setGroupTitle(title:String, idx:Int){
        appRepository.setGroupTitle(title, idx)
    }

    fun setMusicItem(parentIdx:Int, data:List_DefaultItemDataModel){
        appRepository.setMusicDetail(parentIdx, data)
    }

    fun deleteMusicItem(parentIdx:Int){
        appRepository.delCustomListHeader(parentIdx)
    }

    fun getMusicItem(parentIdx:Int):LiveData<List<List_DefaultItemDataModel>>{
        return appRepository.getMusicDetailList(parentIdx)
    }

    fun getMusicDefaultItem(idx:Int):LiveData<List<List_DefaultItemDataModel>>{
        return appRepository.getMusicDefaultDetailList(idx)
    }

//    fun getCustomMusicDetail(parentIdx: Int):LiveData<List<List_ItemsDataModel>>{
//        return appRepository.getCustomMusicDetail(parentIdx)
//    }

    fun getGroupLastIndex():Int{
        return appRepository.getGroupLastIndex()
    }

    fun checkSelectList(idx:Int, data:List_DefaultItemDataModel, isCheck:Boolean){
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

//    fun deleteMusicDetailList(headerIdx:Int){
//        appRepository.deleteMusicDetail(headerIdx)
//    }

    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddListViewModel(application) as T
        }
    }
}