package com.exercise.music_exercise.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.activities.BaseActivity
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.activities.MainActivity
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.database.AppRepository
import com.exercise.music_exercise.fragments.music_detail.MusicDetailFragment

class MusicDetailViewModel(application:Application):AndroidViewModel(application) {
    val appRepository by lazy {
        AppRepository(application)
    }

    var selectPos : Int = 0
    var isLoading : Boolean = true
    var _customType : MutableLiveData<String> = MutableLiveData()
    val customType : LiveData<String>
        get() = _customType

    var _detailItemList : MutableLiveData<List<List_ItemsDataModel>> = MutableLiveData()
    val detailItemList : LiveData<List<List_ItemsDataModel>>
        get() = _detailItemList

    fun getDetailList(owner: LifecycleOwner, parentIdx:Int){
        if(parentIdx == -1){
            appRepository.getMusicDetailList().observe(owner, Observer {
                _detailItemList.postValue(it)
        })} else{
            appRepository.getMusicDetailList(parentIdx).observe(owner, Observer {
                _detailItemList.postValue(it)
        })}
    }

    fun getDetailListForListAdd(parentIdx:Int){
        if(parentIdx == -1){
            appRepository.getMusicDetailList().observe(MusicApplication.currentActivity as ListAddActivity, Observer {
                _detailItemList.postValue(it)
            })} else{
            appRepository.getMusicDetailList(parentIdx).observe(MusicApplication.currentActivity as ListAddActivity, Observer {
                _detailItemList.postValue(it)
            })}
    }

    fun checkDetailItem(baseActivity: BaseActivity, position: Int, data:List_ItemsDataModel, isCheck: Boolean){
        if (baseActivity is ListAddActivity) {
            val addActivity:ListAddActivity = baseActivity as ListAddActivity

            addActivity.addListViewModel.checkSelectList(data.idx, data, isCheck)

            if(_detailItemList != null && _detailItemList!!.value != null){
                run check@{
                    _detailItemList!!.value!!.forEachIndexed { index, listItemsdatamodel ->
                        if(listItemsdatamodel.idx == data.idx){
                            listItemsdatamodel.checked = isCheck
                        }
                    }
                }
            }

            _detailItemList.value = _detailItemList.value
        }
    }

    fun getGroupType(parentIdx:Int){
        appRepository.getMusicGroupList(parentIdx).observe(MusicApplication.currentActivity as BaseActivity, Observer {
            if(it.size > 0)
                _customType.postValue(it[0].customType)

        })
    }

    class Factory(val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MusicDetailViewModel(application) as T
        }
    }
}