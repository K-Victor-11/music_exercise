package com.exercise.music_exercise.fragments.add_list

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.adapters.CustomMusicListAdapter
import com.exercise.music_exercise.adapters.MusicListAdapter
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.fragments.music_detail.MusicDetailFragment
import com.exercise.music_exercise.viewmodels.HomeViewModel

class CustomList_AddGroupListFragment : BaseFragment(),
    CustomMusicListAdapter.onCustomMusicListListener {

    val homeViewModel: HomeViewModel by viewModels()

    private lateinit var rootView: View
    private lateinit var listView: RecyclerView

    var adapter: CustomMusicListAdapter? = null

    companion object{
        @JvmStatic
        fun newInstance(): CustomList_AddGroupListFragment {
            var fragment = CustomList_AddGroupListFragment()

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

        initView()
        initObservers()

        return rootView
    }

    fun initView() {
        adapter = CustomMusicListAdapter(mContext!!, this@CustomList_AddGroupListFragment)
        listView = rootView.findViewById(R.id.listHome)
        listView.adapter = adapter
        listView.itemAnimator = null
        listView.layoutManager =
            LinearLayoutManager(mContext!!, LinearLayoutManager.VERTICAL, false)
    }

    fun initObservers() {
        homeViewModel.getMusicList(mContext!!).observe(viewLifecycleOwner, Observer {
            adapter!!.updateList(it)
        })
    }

    override fun onSelectItem(data: List_HeaderDataModel, position: Int) {
        var fragment:MusicDetailFragment = MusicDetailFragment.newInstance(data.idx, "add")
        fragment.baseActivity = baseActivity
        baseActivity?.pushFragment(R.id.layout_fragment, fragment, "group_list_detail")
    }
}