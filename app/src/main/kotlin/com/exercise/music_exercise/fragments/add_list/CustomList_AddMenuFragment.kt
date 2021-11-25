package com.exercise.music_exercise.fragments.add_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.fragments.BaseFragment

class CustomList_AddMenuFragment:BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_custom_add_menu, container, false)

        val llList: LinearLayout = root.findViewById(R.id.llMenu_List)
        val llAll: LinearLayout = root.findViewById(R.id.llMenu_All)

        llList.setOnClickListener {
            if(baseActivity is ListAddActivity)
                (baseActivity as ListAddActivity).setListFragment()
        }
        llAll.setOnClickListener {
            if(baseActivity is ListAddActivity)
                (baseActivity as ListAddActivity).setAllFragment()
        }

        if (baseActivity is ListAddActivity) {
            (baseActivity as ListAddActivity).addListViewModel.setStep(2)
        }
        return root
    }
}