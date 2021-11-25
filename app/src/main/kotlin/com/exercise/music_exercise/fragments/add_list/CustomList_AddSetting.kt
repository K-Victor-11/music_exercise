package com.exercise.music_exercise.fragments.add_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.adapters.CustomListSettingAdapter
import com.exercise.music_exercise.fragments.BaseFragment

class CustomList_AddSetting:BaseFragment() {

    var adapter:CustomListSettingAdapter ?= null

    companion object{
        @JvmStatic
        fun newInstance():CustomList_AddSetting{
            var fragment:CustomList_AddSetting = CustomList_AddSetting()
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
        var root = inflater.inflate(R.layout.fragment_home, container, false)

        if(baseActivity is ListAddActivity)
            (baseActivity as ListAddActivity).addListViewModel.selectItemList

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}