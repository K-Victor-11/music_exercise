package com.exercise.music_exercise.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.fragments.music_detail.MusicDetailFragment
import com.exercise.music_exercise.viewmodels.MusicDetailViewModel
import kotlinx.android.synthetic.main.activity_musicdetail.*

class MusicDetailActivity:BaseActivity() {
    var longExerciseIndex : Int = 0
    var strTitle : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicdetail)
        setSupportActionBar(toolbar)

        MusicApplication.currentActivity = this

        longExerciseIndex = intent.getIntExtra(AppContents.INTENT_DATA_LIST_INDEX, 0)
        strTitle = intent.getStringExtra(AppContents.INTENT_DATA_LIST_TITLE)!!

        btnMusicDetail_Start.setOnClickListener {

            var detailViewmodel : MusicDetailViewModel? = null
            if( supportFragmentManager.fragments[0] is MusicDetailFragment){
                detailViewmodel = (supportFragmentManager.fragments[0] as MusicDetailFragment).detailViewModel
            }

            if(detailViewmodel != null){
                detailViewmodel.getGroupType(longExerciseIndex)

//                var playIntent = Intent(this@MusicDetailActivity, PlayerActivity::class.java)
//
//                playIntent.putExtra(AppContents.INTENT_DATA_LIST_POSITION, detailViewmodel!!.selectPos)
//                playIntent.putExtra(AppContents.INTENT_DATA_PLAY_LIST, detailViewmodel!!.detailItemList.value as ArrayList<List_ItemsDataModel>)
//                playIntent.putExtra(AppContents.INTENT_DATA_GROUP_TYPE, detailViewmodel!!.customType.value)
//
//                startActivity(playIntent)
            }
        }
//
//        btnMusicDetail_Detail.setOnClickListener {
//            var nextFragment:ExerciseItemDetailFragment = ExerciseItemDetailFragment.newInstance(longExerciseIndex,0)
//            pushFragment(R.id.flContent, nextFragment, nextFragment::class.simpleName.toString(), ExerciseDetailFragment::class.simpleName.toString())
//            btnExerciseDetail_Detail.visibility = View.GONE
//            btnExerciseDetail_Start.visibility = View.GONE
//        }

        var musicDetailFragment: MusicDetailFragment = MusicDetailFragment.newInstance(longExerciseIndex, "view")
        pushFragment(R.id.flContent, musicDetailFragment, MusicDetailFragment::class.simpleName.toString())
    }

    override fun onBackPressed() {

        var countFragment : Int = supportFragmentManager.backStackEntryCount
        if(countFragment == 1) {
            finish()
        }else{
            super.onBackPressed()
//            if (currentFragment() is ExerciseDetailFragment) {
//                toolbar.title = strTitle
//                btnExerciseDetail_Detail.visibility = View.VISIBLE
//                btnExerciseDetail_Start.visibility = View.VISIBLE
//            }
        }
    }
}