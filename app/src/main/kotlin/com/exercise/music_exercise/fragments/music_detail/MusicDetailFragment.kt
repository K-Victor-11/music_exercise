package com.exercise.music_exercise.fragments.music_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.adapters.MusicListDetailAdapter
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.utils.decorations.GridItemDecoration
import com.exercise.music_exercise.viewmodels.HomeViewModel
import com.exercise.music_exercise.viewmodels.MusicDetailViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class MusicDetailFragment:BaseFragment(),MusicListDetailAdapter.onMusicListDetailListener {

    companion object{
        @JvmStatic
        fun newInstance(index:Int, viewType:String) : MusicDetailFragment {
            var fragment: MusicDetailFragment = MusicDetailFragment()
            var bundle : Bundle = Bundle()
            bundle.putInt(AppContents.INTENT_DATA_LIST_INDEX, index)
            bundle.putString(AppContents.INTENT_DATA_VIEW_TYPE, viewType)

            fragment.arguments = bundle

            return fragment
        }

        @JvmStatic
        fun newInstance(viewType:String) : MusicDetailFragment {
            var fragment: MusicDetailFragment = MusicDetailFragment()
            var bundle : Bundle = Bundle()
            bundle.putString(AppContents.INTENT_DATA_VIEW_TYPE, viewType)

            fragment.arguments = bundle

            return fragment
        }

    }

    var adapter : MusicListDetailAdapter? = null
    var idx:Int = 0
    var viewType:String = "view"

    private val detailViewModel: MusicDetailViewModel by lazy {
        ViewModelProvider(
            this,
            MusicDetailViewModel.Factory(MusicApplication.currentActivity!!.application)
        ).get(
            MusicDetailViewModel::class.java
        )
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
        var rootView:View = inflater.inflate(R.layout.fragment_home, container, false)

        if(arguments != null) {
            idx = requireArguments().getInt(AppContents.INTENT_DATA_LIST_INDEX, -1)
            viewType = requireArguments().getString(AppContents.INTENT_DATA_VIEW_TYPE, "view")
        }


        detailViewModel.getDetailList(idx)?.observe(viewLifecycleOwner, Observer {
            if(adapter == null){
                adapter = MusicListDetailAdapter(mContext!!, this, viewType)
                listHome.adapter = adapter
                listHome.layoutManager = GridLayoutManager(mContext, 2)
                listHome.addItemDecoration(GridItemDecoration(mContext!!))
            }

            adapter!!.updateList(it)
        })

//        detailViewModel.getCustomAllList(idx)?.observe(viewLifecycleOwner, Observer {
//            if (adapter == null) {
//                adapter = ExerciseDetailAdapter(mContext!!, this)
//                listHome.adapter = adapter
//                listHome.layoutManager = GridLayoutManager(mContext, 2)
//                listHome.addItemDecoration(gridItemDecoration(mContext!!))
//            }
////
//////            var addData: HealthListData = HealthListData(-1, "Add your own workout", "A")
////
////            Toast.makeText(context, "Size ::: ${it.size}", Toast.LENGTH_SHORT).show()
//
//            adapter!!.updateList(it)
//        })

        return rootView
    }

    override fun onItemSelect(data: List_ItemsDataModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemChecked(data: List_ItemsDataModel, position: Int) {
        if(baseActivity is ListAddActivity){
            (baseActivity as ListAddActivity).addListViewModel.itemList.add(data)
        }
    }
}