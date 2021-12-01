package com.exercise.music_exercise.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.databinding.ActivityPlayerBinding
import com.exercise.music_exercise.viewmodels.PlayerViewModel

class PlayerActivity:BaseActivity(), View.OnClickListener{

    lateinit var binding:ActivityPlayerBinding
    private val playerViewModel : PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)

        with(binding){
            lifecycleOwner = this@PlayerActivity
            onclickListener = this@PlayerActivity
            vm = playerViewModel
        }

        playerViewModel.playList.observe(this, Observer {

        })

        initIntent()
    }

    fun initIntent(){
        playerViewModel.selectPos = intent.getIntExtra(AppContents.INTENT_DATA_LIST_POSITION, 0)
        playerViewModel.setPlayList(intent.getSerializableExtra(AppContents.INTENT_DATA_PLAY_LIST) as ArrayList<List_ItemsDataModel>)
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}