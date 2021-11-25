package com.exercise.music_exercise.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.fragments.add_list.CustomList_AddGroupListFragment
import com.exercise.music_exercise.fragments.add_list.CustomList_AddMenuFragment
import com.exercise.music_exercise.fragments.add_list.CustomList_AddTitleFragment
import com.exercise.music_exercise.fragments.music_detail.MusicDetailFragment
import com.exercise.music_exercise.viewmodels.AddListViewModel
import kotlinx.android.synthetic.main.activity_add_list.*

class ListAddActivity:BaseActivity(),View.OnClickListener {
    var title: String = ""
    var isEdit: Boolean = false
    var selectIndex: Int = 0
    var listType: String = "C"

    var isGroupSelect :Boolean = false

    val addListViewModel:AddListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_list)

        if (intent.hasExtra(AppContents.INTENT_DATA_EDIT_MODE)) {
            isEdit = intent.getBooleanExtra(AppContents.INTENT_DATA_EDIT_MODE, false)
        }

        if (intent.hasExtra(AppContents.INTENT_DATA_LIST_INDEX)) {
            selectIndex = intent.getIntExtra(AppContents.INTENT_DATA_LIST_INDEX, -1)
        }

        btn_Next.visibility = View.VISIBLE
        btn_Pre.visibility = View.GONE
        btn_Next.setOnClickListener(this)
        btn_Pre.setOnClickListener { onBackPressed() }

        if(intent.hasExtra(AppContents.INTENT_DATA_CHECK_GROUP_INDEX)){
            var selectGroupIndex:ArrayList<String>? = intent.getSerializableExtra(AppContents.INTENT_DATA_CHECK_GROUP_INDEX) as ArrayList<String>
            isGroupSelect = true
//            listViewModel.setGroupSelectList(selectGroupIndex!!)
            return
        }

        var titleFragment : CustomList_AddTitleFragment = CustomList_AddTitleFragment()
        titleFragment.baseActivity = this
        pushFragment(R.id.layout_fragment, titleFragment)

    }


    fun setButtonEnable(isEnabled: Boolean) {

        btn_Next.isEnabled = isEnabled
        if (isEnabled) {
            btn_Next.background = ContextCompat.getDrawable(this, R.drawable.bg_radius3_999999)
//            btn_Next.setTextColor(ContextCompat.getColor(this, R.color.font_color_black))
        } else {
            btn_Next.setBackgroundColor(ContextCompat.getColor(this, R.color.color_e5e5e5))
//            btn_Next.setTextColor(ContextCompat.getColor(this, R.color.font_color_black))
        }
    }

    fun setListFragment(){
        addListViewModel.topTitle = getString(R.string.select_menu_list)
        btn_Pre.visibility = View.VISIBLE
        llBottomArea.visibility = View.VISIBLE

        var groupListFragment: CustomList_AddGroupListFragment = CustomList_AddGroupListFragment.newInstance()
        groupListFragment.baseActivity = this
        pushFragment(R.id.layout_fragment, groupListFragment, "group_list")
    }

    fun setAllFragment() {
        addListViewModel.topTitle = getString(R.string.select_menu_all)
        btn_Pre.visibility = View.VISIBLE
        llBottomArea.visibility = View.VISIBLE
        var detailFragment: MusicDetailFragment = MusicDetailFragment.newInstance("add")
        detailFragment.baseActivity = this
        pushFragment(R.id.layout_fragment, detailFragment, "all_list")

    }


    override fun onClick(v: View) {
        if (v == btn_Next) {
            if(addListViewModel.getStep() == 1){
                /** title -> menu 리스트 선택 **/
                var menuFragment = CustomList_AddMenuFragment()
                menuFragment.baseActivity = this
                pushFragment(R.id.layout_fragment, menuFragment, "add_menu")
            } else if(addListViewModel.getStep() == 2){
                /** 세부 셋팅으로 이동 **/
            }
//            if (listViewModel.getStep() == 1) {
//                /** 운동리스트의 세부 셋팅으로 이동 **/
//                if (listViewModel.getSelectList().size > 0) {
//                    btn_Pre.visibility = View.VISIBLE
//
//                    var nextFragment: CustomExerciseFragment =
//                        CustomExerciseFragment.newInstance(listViewModel.getSelectList())
//                    nextFragment.baseActivity = this;
//                    pushFragment(R.id.layout_fragment, nextFragment, "list_detail")
//                } else {
//                    Toast.makeText(this, "운동을 1개 이상 선택 해주세요.", Toast.LENGTH_SHORT).show()
//                }
//
//            } else if (listViewModel.getStep() == 2) {
//                /** 운동리스트의 Title을 입력 하는 화면으로 이동 **/
//                btn_Pre.visibility = View.VISIBLE
//                var nextFragment: CustomTitleFragment = CustomTitleFragment()
//                var bundle: Bundle = Bundle()
//                bundle.putBoolean(AppContents.INTENT_DATA_EDIT_MODE, isEdit)
//                bundle.putLong(AppContents.INTENT_DATA_LIST_INDEX, selectIndex)
//
//                nextFragment.arguments = bundle
//                nextFragment.baseActivity = this;
//                pushFragment(R.id.layout_fragment, nextFragment, "list_title")
//
//                btn_Next.text = getString(R.string.btn_confirm)
//            } else if (listViewModel.getStep() == 3) {
//                var listData: HealthListData = HealthListData(selectIndex, title, listType)
//
//                var intentData: Intent = Intent()
//                intentData.putExtra(AppContents.RESULT_DATA_LISTDATA, listData)
//                intentData.putExtra(AppContents.RESULT_DATA_HEALTHLIST, listViewModel.getSelectList())
//                intentData.putExtra(AppContents.INTENT_DATA_EDIT_MODE, isEdit)
//                intentData.putExtra(AppContents.INTENT_DATA_LIST_INDEX, selectIndex)
//                setResult(RESULT_OK, intentData)
//                finish()
//            }
        }
    }
}