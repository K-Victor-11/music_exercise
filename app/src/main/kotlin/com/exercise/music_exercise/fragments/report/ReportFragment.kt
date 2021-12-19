package com.exercise.music_exercise.fragments.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.adapters.CompleteListAdapter
import com.exercise.music_exercise.custom_view.CalendarView
import com.exercise.music_exercise.data_models.CommonListDataModel
import com.exercise.music_exercise.data_models.PlayReportDataModel
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.fragments.main.HomeFragment
import com.exercise.music_exercise.viewmodels.CalendarViewModel
import com.exercise.music_exercise.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_play_report.*

class ReportFragment : BaseFragment() {

    private val viewModel: CalendarViewModel by lazy {
        ViewModelProvider(
            this,
            CalendarViewModel.Factory(MusicApplication.currentActivity!!.application)
        ).get(
            CalendarViewModel::class.java
        )
    }

    var adapter:CompleteListAdapter ?= null

    companion object {
        @JvmStatic
        fun newInstance(): ReportFragment {
            var fragment = ReportFragment()
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
        var view:View = inflater.inflate(R.layout.fragment_play_report, container, false)

        initObserve()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var month:String = view.findViewById<CalendarView>(R.id.calComplete_Calendar).getMonth()
        var year:String = view.findViewById<CalendarView>(R.id.calComplete_Calendar).getYear()
        setData("${year}${month}")
    }

    fun initObserve(){
        viewModel.musicReportList.observe(viewLifecycleOwner, Observer {
            var list:ArrayList<PlayReportDataModel> = ArrayList<PlayReportDataModel>()
            list.addAll(it)
            with(view){
                if(view != null)
                    requireView().findViewById<CalendarView>(R.id.calComplete_Calendar).setReservationData(list)
            }
        })
    }

    fun setData(date:String){

        viewModel.getPlayReportGroup(viewLifecycleOwner, date)
        viewModel.getPlayReportItem(date).observe(viewLifecycleOwner, Observer {
            var list:ArrayList<CommonListDataModel> = ArrayList<CommonListDataModel>()
            var playDate:String = ""
            if(it != null && it.size > 0) {
                it.forEachIndexed { index, playExerciseItemHeaderData ->
                    if (playDate == "" || playDate != playExerciseItemHeaderData.playDate) {
                        playDate = playExerciseItemHeaderData.playDate
                        list.add(CommonListDataModel(AppContents.VIEWTYPE_DATEHADER, playDate))
                    }

                    list.add(
                        CommonListDataModel(
                            AppContents.VIEWTYPE_PLAYITEM,
                            playExerciseItemHeaderData
                        )
                    )
                }

                adapter = CompleteListAdapter(mContext!!)
                rvComplete_list.adapter = adapter
                rvComplete_list.layoutManager =
                    LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

                adapter!!.update(list)
            }
        })
//        viewModel.getPlayItemExercise(date)?.observe(baseActivity!!, Observer {
//            var list:ArrayList<PlayExerciseListData> = ArrayList<PlayExerciseListData>()
//            var playDate : String = ""
//
//            if(it != null && it.size > 0){
//                it.forEachIndexed { index, playExerciseItemHeaderData ->
//                    if(playDate == "" || playDate != playExerciseItemHeaderData.playDate){
//                        playDate = playExerciseItemHeaderData.playDate
//                        list.add(PlayExerciseListData(AppContents.VIEWTYPE_DATEHADER, playDate))
//                    }
//
//                    list.add(PlayExerciseListData(AppContents.VIEWTYPE_PLAYITEM, playExerciseItemHeaderData))
//                }
//
//                adapter = CompleteListAdapter(mContext!!)
//                rvComplete_list.adapter = adapter
//                rvComplete_list.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
//
//                adapter!!.update(list)
//            }
//
//        })

//        if(list != null && list.size > 0){
//            var detailList : ArrayList<PlayExerciseListData> = ArrayList<PlayExerciseListData>()
//            list.forEachIndexed { index, playExerciseData ->
//                detailList.add(PlayExerciseListData(AppContents.VIEWTYPE_DATEHADER, playExerciseData))
//
//                viewModel.getPlayList(playExerciseData.strDate)?.observe(baseActivity!!, Observer {
//                    if(it != null && it.size > 0) {
//                        Log.d("kamuel", "groupList.value.size ::: " + it.size)
//                        it.forEachIndexed { index, playExerciseData ->
//                            var itemList : LiveData<List<PlayExerciseItemHeaderData>> ?= viewModel.playExerciseItemRepository.getItemList(playExerciseData.idx.toInt())
//                            if(itemList != null && itemList.value != null && itemList.value!!.size > 0){
//                                itemList.value!!.forEachIndexed { index, playExerciseItemHeaderData ->
//                                    detailList.add(PlayExerciseListData(AppContents.VIEWTYPE_PLAYITEM, playExerciseItemHeaderData))
//                                }
//                            }
//                        }
//
//                        adapter = CompleteListAdapter(mContext!!)
//                        rvComplete_list.adapter = adapter
//                        rvComplete_list.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
//
//                        adapter!!.update(detailList)
//
//                    }else
//                        Log.d("kamuel", "groupList.value.size ::: nul!!!!")
//                })
//            }
//        }

    }

}