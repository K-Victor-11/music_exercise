package com.exercise.music_exercise.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.database.AppDataBase
import com.exercise.music_exercise.fragments.add_list.CustomList_AddGroupListFragment
import com.exercise.music_exercise.fragments.add_list.CustomList_AddMenuFragment
import com.exercise.music_exercise.fragments.add_list.CustomList_AddSettingFragment
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
            addListViewModel.isEditMode = intent.getBooleanExtra(AppContents.INTENT_DATA_EDIT_MODE, false)
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
        if(addListViewModel.isEditMode) {
            var bundle: Bundle = Bundle()
            bundle.putBoolean(AppContents.INTENT_DATA_EDIT_MODE, addListViewModel.isEditMode)
            bundle.putInt(AppContents.INTENT_DATA_LIST_INDEX, selectIndex)
            titleFragment.arguments = bundle

            addListViewModel.getMusicItem(selectIndex).observe(this, Observer {
                it.forEach { defaultItem ->
                    addListViewModel.checkSelectList(defaultItem.idx, defaultItem, true)
                }
            })
        }
        titleFragment.baseActivity = this
        pushFragment(R.id.layout_fragment, titleFragment)

    }

    override fun onBackPressed() {
        btn_Next.text = getString(R.string.btn_next)
        if(currentFragment() is CustomList_AddTitleFragment){
            llMenuTitle.visibility = View.GONE

            if(btn_Next.visibility == View.GONE)
                llBottomArea.visibility = View.GONE
            btn_Pre.visibility = View.GONE

            addListViewModel.setStep(1)
        } else if(currentFragment() is CustomList_AddMenuFragment){
            if(isGroupSelect)
                finish()
            else
                addListViewModel.setStep(1)

            btn_Pre.visibility = View.GONE

        } else if(currentFragment() is CustomList_AddSettingFragment){
            addListViewModel.setStep(3)
        } else {
            addListViewModel.setStep(2)
        }

        super.onBackPressed()

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
                btn_Pre.visibility = View.VISIBLE
                llBottomArea.visibility = View.VISIBLE

                var menuFragment = CustomList_AddMenuFragment()
                menuFragment.baseActivity = this
                pushFragment(R.id.layout_fragment, menuFragment, "add_menu")
            } else if(addListViewModel.getStep() == 2){
                if(addListViewModel.itemList != null && addListViewModel.itemList.size > 0){
                    /** 세부 셋팅으로 이동 **/
                    var settingFragment = CustomList_AddSettingFragment()
                    settingFragment.baseActivity = this
                    pushFragment(R.id.layout_fragment, settingFragment, "setting")
                } else{
                    Toast.makeText(this, "음원이 선택 되지 않았습니다.\n음원을 선택해 주세요.", Toast.LENGTH_SHORT).show()
                }
            } else if(addListViewModel.getStep() == 3){
                var musicDao = AppDataBase.getInstance(this, callback).musicListDao()
                var musicDetailDao = AppDataBase.getInstance(this, callback).musicListDetailDao()

                var parentIdx : Int = 0
                if(addListViewModel.isEditMode){
                    addListViewModel.setGroupTitle(addListViewModel.addTitle, selectIndex)
//                    addListViewModel.deleteMusicDetailList(selectIndex)
                    parentIdx = selectIndex
                } else {
                    addListViewModel.setGroupTitle(addListViewModel.addTitle)
                    parentIdx = addListViewModel.getGroupLastIndex()
                }

                addListViewModel.itemList.forEach {
                    addListViewModel.setMusicItem(parentIdx, it)
                }


                setResult(RESULT_OK)
                finish()

            }
        }
    }

    var callback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //DO AS NEEDED
            Log.d("kamuel", "onCreate!!!!")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            //DO AS NEEDED
            Log.d("kamuel", "onOpen!!!!")
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Log.d("kamuel", "onDestructiveMigration!!!!")
        }
    }
}