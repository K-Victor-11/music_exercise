package com.exercise.music_exercise.fragments.add_list

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.activities.ListAddActivity
import com.exercise.music_exercise.fragments.BaseFragment
import com.exercise.music_exercise.viewmodels.AddListViewModel
import kotlinx.android.synthetic.main.fragment_custom_add_title.*

class CustomList_AddTitleFragment:BaseFragment() {

    var isEditMode = false
    var selectIndex:Int = -1

    companion object{
        @JvmStatic
        fun newInstance():CustomList_AddTitleFragment{
            var fragment : CustomList_AddTitleFragment = CustomList_AddTitleFragment()

            return fragment
        }
    }

    val customListAddViewModel by lazy {
        ViewModelProvider(this, AddListViewModel.Factory(MusicApplication.currentActivity!!.application)).get(AddListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_custom_add_title, container, false)

        if(arguments != null ) {
            isEditMode = requireArguments().getBoolean(AppContents.INTENT_DATA_EDIT_MODE, false)
            selectIndex = requireArguments().getInt(AppContents.INTENT_DATA_LIST_INDEX, -1)
        }

        if(isEditMode) {
            var itemData = customListAddViewModel.getGroupInfo(selectIndex)

            if( itemData != null ){
                tvCustom_Title.setText(itemData.listTitle_kor)

//                (ExerciseApplication.currentActivity as ListAddActivity).listType =
//                    it.get(0).list_type
            }
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCustom_Title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (MusicApplication.currentActivity is ListAddActivity) {
                    if(TextUtils.isEmpty(s.toString())) {
                        (MusicApplication.currentActivity as ListAddActivity).setButtonEnable(false)
                    } else {
                        (MusicApplication.currentActivity as ListAddActivity).setButtonEnable(true)
                        (MusicApplication.currentActivity as ListAddActivity).addListViewModel.addTitle = s.toString()
                    }
                }
            }

        })

        if (MusicApplication.currentActivity is ListAddActivity) {
            (MusicApplication.currentActivity as ListAddActivity).setButtonEnable(false)
            (MusicApplication.currentActivity as ListAddActivity).addListViewModel.setStep(1)
        }
    }

}