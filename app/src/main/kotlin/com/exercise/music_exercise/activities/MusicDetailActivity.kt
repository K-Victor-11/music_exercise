package com.exercise.music_exercise.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.MusicApplication
import com.exercise.music_exercise.R
import com.exercise.music_exercise.fragments.music_detail.MusicDetailFragment
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

//        btnMusicDetail_Start.setOnClickListener {
//            var exerciseStartItent : Intent = Intent(this@MusicDetailActivity, ExerciseActivity::class.java)
//            exerciseStartItent.putExtra(AppContents.INTENT_DATA_LIST_INDEX, longExerciseIndex)
//            startActivity(exerciseStartItent)
//        }
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