package com.exercise.music_exercise.fragments.main

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.activities.MusicDetailActivity
import com.exercise.music_exercise.adapters.MusicListAdapter
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.fragments.music_detail.MusicDetailFragment
import com.exercise.music_exercise.utils.DialogUtils
import com.exercise.music_exercise.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), MusicListAdapter.onMusicListListener, View.OnClickListener {

    interface onHomeFragmentListener {
        fun onListMore(position: Int, data: List_HeaderDataModel)
    }

    var adapter: MusicListAdapter? = null
    private var listener: onHomeFragmentListener? = null

    private var bottomDialog: BottomSheetDialog? = null
    var selectData: List_HeaderDataModel? = null

    var checkList: ArrayList<String> = ArrayList<String>()
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            HomeViewModel.Factory(MusicApplication.currentActivity!!.application)
        ).get(
            HomeViewModel::class.java
        )
    }

    var addResultLauncher: ActivityResultLauncher<Intent>? = null

    private lateinit var rootView: View
    private lateinit var listView: RecyclerView

    companion object {
        @JvmStatic
        fun newInstance(listener: onHomeFragmentListener?): HomeFragment {
            var fragment = HomeFragment()
            fragment.listener = listener

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
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        initView()
        initObservers()
        initResultLauncher()

        return rootView
    }

    fun initView() {
        adapter = MusicListAdapter(mContext!!, this@HomeFragment)
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

    fun initResultLauncher(){
        addResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {

            }
        }
    }

    override fun onAdd() {
        var intent : Intent = Intent(MusicApplication.currentActivity, ListAddActivity::class.java)

        if(checkList != null && checkList.size > 0)
            intent.putExtra(AppContents.INTENT_DATA_CHECK_GROUP_INDEX, checkList)

        addResultLauncher!!.launch(intent)
    }

    override fun onSelectItem(data: List_HeaderDataModel, position: Int) {
        var intent: Intent =
            Intent(MusicApplication.currentActivity, MusicDetailActivity::class.java)
        intent.putExtra(AppContents.INTENT_DATA_LIST_INDEX, data.idx)
        intent.putExtra(AppContents.INTENT_DATA_LIST_TITLE, data.listTitle_kor)

        addResultLauncher!!.launch(intent)
    }

    override fun onMore(data: List_HeaderDataModel, position: Int) {
        selectData = data
        showBottomSheetDialog()
    }

    override fun onChecked(data: List_HeaderDataModel, position: Int) {
        TODO("Not yet implemented")
    }

    /**
     * 하단 BottomSheetDialog Show
     */
    private fun showBottomSheetDialog() {
        if (bottomDialog != null && bottomDialog!!.isShowing()) {
            bottomDialog!!.dismiss()
            bottomDialog = null
        }

        var hsMenu: LinkedHashMap<String, String> = LinkedHashMap<String, String>()

        hsMenu.put("Edit", "edit")
        hsMenu.put("Delete", "delete")
        hsMenu.put("Cancel", "cancel")

        DialogUtils.showBottomSheetDialog(
            baseActivity!!,
            hsMenu,
            null,
            R.color.design_default_color_secondary,
            true,
            object : DialogUtils.OnBottomSheetSelectedListener {
                override fun onSelected(index: Int, text: String, value: String) {
//                if(value == "edit"){
//                    Log.d("kamuel", "selectData!!.idx :: "+selectData!!.idx)
//                    var intent : Intent = Intent(baseActivity!!, ListAddActivity::class.java)
//                    intent.putExtra(AppContents.INTENT_DATA_EDIT_MODE, true)
//                    intent.putExtra(AppContents.INTENT_DATA_LIST_INDEX, selectData!!.idx)
//                    Log.d("kamuel", "selectData!!.idx 2 :: "+selectData!!.idx)
//
//                    baseActivity!!.startActivityForResult(intent, AppContents.REQUEST_CODE_ADDLIST)
//                } else if(value == "delete"){
////                    Toast.makeText(this@MainActivity, "작업 중 입니다. ㅠㅠ", Toast.LENGTH_SHORT).show()
//                    homeViewModel.healthListDelete(selectData!!.idx)
//                }
                }
            })
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}