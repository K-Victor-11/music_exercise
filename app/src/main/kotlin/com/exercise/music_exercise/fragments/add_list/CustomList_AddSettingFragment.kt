package com.exercise.music_exercise.fragments.add_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.adapters.CustomListSettingAdapter
import com.exercise.music_exercise.adapters.CustomMusicListAdapter
import com.exercise.music_exercise.custom_view.dialogs.CustomTimePickerDialog
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.utils.DialogUtils
import com.exercise.music_exercise.viewmodels.AddListSettingViewModel

class CustomList_AddSettingFragment : BaseFragment(), CustomListSettingAdapter.onSettingListener {

    var adapter: CustomListSettingAdapter? = null
    val settingViewModel: AddListSettingViewModel by viewModels()

    private lateinit var rootView: View
    private lateinit var listView: RecyclerView

    companion object {
        @JvmStatic
        fun newInstance(): CustomList_AddSettingFragment {
            var fragment: CustomList_AddSettingFragment = CustomList_AddSettingFragment()
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        if (baseActivity is ListAddActivity) {
            (baseActivity as ListAddActivity).addListViewModel.setStep(3)

            var selectList: ArrayList<List_DefaultItemDataModel> = arrayListOf()
            (baseActivity as ListAddActivity).addListViewModel.selectItemList.forEach { i, listItemsdatamodel ->
                selectList.add(listItemsdatamodel)
            }

            settingViewModel.setSettingList(selectList)
        }

        initView()
        initObserve()
        return rootView
    }

    fun initView() {
        adapter = CustomListSettingAdapter(mContext!!, this)
        listView = rootView.findViewById(R.id.listHome)
        listView.adapter = adapter
        listView.itemAnimator = null
        listView.layoutManager =
                LinearLayoutManager(mContext!!, LinearLayoutManager.VERTICAL, false)
    }

    fun initObserve() {
        settingViewModel.setList.observe(viewLifecycleOwner, Observer {
            if (adapter != null) {
                adapter!!.updateList(it)
            }

            if(baseActivity is ListAddActivity) {
                (baseActivity as ListAddActivity).addListViewModel.itemList = it
            }
        })
    }

//    override fun onCountUp(data: List_DefaultItemDataModel, position: Int) {
//        var time = settingViewModel._setList!!.value!!.get(position).playTime
//        var isChange: Boolean = true
//
//        if (time == 1) {
//            time = 3
//            isChange = true
//        } else if (time == 3) {
//            time = 5
//            isChange = true
//        } else if (time == 5) {
//            time = 10
//            isChange = true
//        } else if (time == 10) {
//            time = 15
//            isChange = true
//        } else if (time == 15) {
//            time = 30
//            isChange = true
//        } else if (time == 30) {
//            time = 60
//            isChange = true
//        } else if (time == 60) {
//            Toast.makeText(context, "더 이상 늘릴 수 없습니다.", Toast.LENGTH_SHORT).show()
//            isChange = false
//        }
//
//        if(isChange) {
//            settingViewModel._setList!!.value!!.get(position).playTime = time
//            settingViewModel.setSettingList(settingViewModel._setList!!.value!!)
//        }
//
//    }
//
//    override fun onCountDown(data: List_DefaultItemDataModel, position: Int) {
//        var time = settingViewModel._setList!!.value!!.get(position).playTime
//        var isChange: Boolean = true
//
//        if(time == 60){
//            time = 30
//            isChange = true
//        } else if(time == 30){
//            time = 15
//            isChange = true
//        } else if(time == 15){
//            time = 10
//            isChange = true
//        } else if(time == 10){
//            time = 5
//            isChange = true
//        } else if(time == 5){
//            time = 3
//            isChange = true
//        } else if(time == 3){
//            time = 1
//            isChange = true
//        } else if(time == 1){
//            Toast.makeText(context, "더 이상 줄일 수 없습니다.", Toast.LENGTH_SHORT).show()
//            isChange = false
//        }
//
//        if(isChange) {
//            settingViewModel._setList!!.value!!.get(position).playTime = time
//            settingViewModel.setSettingList(settingViewModel._setList!!.value!!)
//        }
//    }

    override fun onTimeChange(data: List_DefaultItemDataModel, position: Int) {

        DialogUtils.showTimePicker(childFragmentManager, "취소", "확인", object :CustomTimePickerDialog.onTimePickerListener{
            override fun onTimePickerCallback(hour: Int, minute: Int, second: Int) {
                var playTime = (minute * 60) + second
                settingViewModel._setList!!.value!!.get(position).playTime = playTime
                settingViewModel.setSettingList(settingViewModel._setList!!.value!!)
            }

        })
    }

    override fun onSortUp(data: List_DefaultItemDataModel, position: Int) {
        var insertPosition = position
        var removePosition = position

        if(insertPosition < 2)
            insertPosition = 0
        else
            insertPosition = position -1

        settingViewModel._setList!!.value!!.removeAt(removePosition)
        settingViewModel._setList!!.value!!.add(insertPosition, data)

        settingViewModel.setSettingList( settingViewModel._setList!!.value!!)
    }

    override fun onSortDown(data: List_DefaultItemDataModel, position: Int) {
        settingViewModel._setList!!.value!!.add(position+2, data)
        settingViewModel._setList!!.value!!.removeAt(position)

        settingViewModel.setSettingList( settingViewModel._setList!!.value!!)
    }
}