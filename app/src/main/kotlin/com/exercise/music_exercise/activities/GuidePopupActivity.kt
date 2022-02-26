package com.exercise.music_exercise.activities

import android.os.Bundle
import android.widget.ImageView
import com.exercise.music_exercise.R

class GuidePopupActivity : BaseActivity() {

    lateinit var ivClose:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_popup)

        /** 닫기만 해뒀어요 레이아웃 구성하셔서 넣으면 됩니다. **/
        initView()
    }

    fun initView(){
        ivClose = findViewById(R.id.ivGuide_Close)
        ivClose.setOnClickListener {
            this.finish()
        }
    }
}