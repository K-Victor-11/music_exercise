package com.exercise.music_exercise.fragments.add_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.adapters.CustomListSettingAdapter
import com.exercise.music_exercise.adapters.CustomMusicListAdapter
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.viewmodels.AddListSettingViewModel

class CustomList_AddSettingFragment : BaseFragment() {

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
            (baseActivity as ListAddActivity).addListViewModel.setStep(2)

            var selectList: ArrayList<List_ItemsDataModel> = arrayListOf()
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
        adapter = CustomListSettingAdapter(mContext!!)
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
        })
    }
}