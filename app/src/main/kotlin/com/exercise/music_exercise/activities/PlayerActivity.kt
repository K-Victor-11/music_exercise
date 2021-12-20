package com.exercise.music_exercise.activities

import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.data_models.PlayReportDataModel
import com.exercise.music_exercise.utils.DateUtils
import com.exercise.music_exercise.utils.DialogUtils
import com.exercise.music_exercise.viewmodels.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File


class PlayerActivity : BaseActivity(), View.OnClickListener{

    val playerViewModel by lazy {
        ViewModelProvider(this, PlayerViewModel.Factory(this.application)).get(PlayerViewModel::class.java)
    }

    private val HANDLER_WHAT_SETTING = 0
    private val HANDLER_WHAT_PLAYING = 1
    private val HANDLER_WHAT_NEXT = 2
    private val HANDLER_WHAT_COMPLETE = 3
    private val HANDLER_WHAT_PROGRESS = 4

    private var mAfd: AssetFileDescriptor? = null
    private var mFile: File? = null

    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null

    lateinit var tvTitle:TextView
    lateinit var ivPlayer:ImageView
    lateinit var seekVolume : SeekBar
    lateinit var btnTimeSetting: Button
    lateinit var pgPlayTime:ProgressBar
    lateinit var tvRunningTime : TextView
    lateinit var chkLoop:CheckBox

    var isPlaying:Boolean = false

    val onCompletionListener: MediaPlayer.OnCompletionListener = MediaPlayer.OnCompletionListener {
        it.release()
        mCurrentPlayer = mNextPlayer
        createNextMediaPlayer()
    }

    private var isStarting = false
    lateinit var audioManager:AudioManager
    var volume_level:Int = 0
    var volume_max_level:Int = 0
    var playTime_millisecond:Int = 1000 * 60 /** 1분 **/
    var runningTime:Int = 0 /** 실제 음악이 나온 시간 **/

    val handler:Handler by lazy {
        object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                /** groupType에 따라서 설정되는 시간값을 확인 한다. **/
                /** 사용자 리스트도 시간 설정을 해야 하는가?? **/
                if(msg.what == 0){
                    if(isPlaying){
                        /** 초기화 **/
                        runningTime = 0

                        if(!chkLoop.isChecked) {
                            pgPlayTime.progress = 0
                            tvRunningTime.text = "${timeFormat(runningTime)} / ${timeFormat(playTime_millisecond)}"
                        } else {
                            tvRunningTime.text = "${timeFormat(runningTime)} / -"
                        }

                        handler.sendEmptyMessageDelayed(HANDLER_WHAT_PLAYING, 1000)
                    }
                } else if(msg.what == 1) {
                    if(isPlaying){
                        runningTime += 1000
                        if(chkLoop.isChecked){
                            if(runningTime == 60000) {
                                var item = playerViewModel.playList.value!![playerViewModel.selectPos]
                                saveExercise(item)
                            }
                            tvRunningTime.text = "${timeFormat(runningTime)} / -"
                            handler.sendEmptyMessageDelayed(HANDLER_WHAT_PLAYING, 1000)
                        } else {
                            pgPlayTime.progress = runningTime
                            tvRunningTime.text = "${timeFormat(runningTime)} / ${timeFormat(playTime_millisecond)}"
                            if (runningTime >= playTime_millisecond) {

                                stop()
                                var item = playerViewModel.playList.value!![playerViewModel.selectPos]
                                saveExercise(item)

                                if (playerViewModel.groupType == "C") {
                                    /** 음원 Next **/
                                    Toast.makeText(this@PlayerActivity, "다음 음원 변경!!!", Toast.LENGTH_SHORT).show()
                                    handler.sendEmptyMessage(HANDLER_WHAT_NEXT)
                                } else if (playerViewModel.groupType == "D") {
                                    Toast.makeText(this@PlayerActivity, "음원종료!!!", Toast.LENGTH_SHORT).show()
                                    handler.sendEmptyMessage(HANDLER_WHAT_COMPLETE)
                                }
                            } else {
                                handler.sendEmptyMessageDelayed(HANDLER_WHAT_PLAYING, 1000)
                            }
                        }
                    }
                } else if(msg.what == 2){
                    playerViewModel.selectPos ++
                    if(playerViewModel.selectPos >= playerViewModel.playList.value!!.size){
                        handler.sendEmptyMessage(HANDLER_WHAT_COMPLETE)
                    } else {
                        var item = playerViewModel.playList.value!![playerViewModel.selectPos]
                        onPlay(item.musicTitle_kor, item.hertz)
                        handler.sendEmptyMessage(HANDLER_WHAT_SETTING)
                    }
                } else if(msg.what == 3){
                    /** 음원리스트 모두 플레이 완료 **/
                }

            }
        }
    }

    fun timeFormat(milliseconds:Int):String {
        var second : Int = 0
        var min : Int = 0

        var playTimeFormat : String = ""

        /** 1분 = 1000 * 60 **/
        second = milliseconds / 1000
        min = second/60
        var mod_seconds = second % 60

        playTimeFormat = "${String.format("%02d", min)}:${String.format("%02d", mod_seconds)}"

        return playTimeFormat
    }

    fun saveExercise(playListItem:List_ItemsDataModel){
        Toast.makeText(this, "음원 Report 저장!!", Toast.LENGTH_SHORT).show()
        var reportData : PlayReportDataModel = PlayReportDataModel(playListItem.musicTitle_kor, playListItem.musicCode, playListItem.idx, playListItem.hertz, DateUtils.getNowDate("yyyyMMdd"))
        playerViewModel.saveExercise(reportData)
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /** onBackPressed 만으로 작동이 안되서 키 이멘트 추가 **/
            onBackPressed()
        }
        return super.onKeyUp(keyCode, event)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volume_level = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volume_max_level = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        ivPlayer = findViewById<ImageView>(R.id.ivPlay_Button)
        tvTitle = findViewById(R.id.tvPlay_Title)
        seekVolume = findViewById(R.id.skPlay_Sound)
        seekVolume.max = volume_max_level
        seekVolume.setProgress(volume_level)

        btnTimeSetting = findViewById(R.id.btnPlay_TimeSetting)
        btnTimeSetting.setOnClickListener(this)

        pgPlayTime = findViewById(R.id.pbPlay_PlayTime)
        tvRunningTime = findViewById(R.id.tvPlay_RunningTime)

        chkLoop = findViewById(R.id.cbnPlay_loop)
        chkLoop.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                stop()
                runningTime = 0
                if(isChecked) {
                    btnTimeSetting.background = ContextCompat.getDrawable(this@PlayerActivity, R.drawable.bg_radius3_line_e5e5e5)
                    btnTimeSetting.setTextColor(ContextCompat.getColor(this@PlayerActivity, R.color.color_979797))
                    pgPlayTime.progress = 1
                    pgPlayTime.max = 1
                } else {
                    btnTimeSetting.background = ContextCompat.getDrawable(this@PlayerActivity, R.drawable.bg_radius3_line_999999)
                    btnTimeSetting.setTextColor(ContextCompat.getColor(this@PlayerActivity, R.color.color_font_black))
                    pgPlayTime.progress = 0
                    pgPlayTime.max = playerViewModel.playList.value!!.get(playerViewModel.selectPos).playTime * 60 * 1000
                }
            }

        })

        seekVolume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, position: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, position, AudioManager.FLAG_SHOW_UI)
                volume_level = position
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        ivPlayer.setOnClickListener(this)

        initIntent()
        initCurrentPlayer()

        playerViewModel.playList.observe(this, Observer {
//            if (mCurrentPlayer != null) {
//                mCurrentPlayer.set()
//            }
            getPlayTitle(it.get(playerViewModel.selectPos).musicTitle_kor, it.get(playerViewModel.selectPos).hertz)
        })
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
        {
            getVolumeLevel(false)
            return true
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            getVolumeLevel(true)
            return true
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun getVolumeLevel(isUp: Boolean){
        if(isUp) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
            volume_level ++

            if(volume_max_level <= volume_level)
                volume_level = volume_max_level
        }else {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)

            volume_level --

            if(volume_level <= 0)
                volume_level = 0
        }
//        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume_level, 0)
        seekVolume.setProgress(volume_level)
    }

    fun initIntent() {
        playerViewModel.groupType = if(TextUtils.isEmpty(intent.getStringExtra(AppContents.INTENT_DATA_GROUP_TYPE))) "D" else intent.getStringExtra(AppContents.INTENT_DATA_GROUP_TYPE).toString()
        playerViewModel.selectPos = intent.getIntExtra(AppContents.INTENT_DATA_LIST_POSITION, 0)
        playerViewModel.setPlayList(intent.getSerializableExtra(AppContents.INTENT_DATA_PLAY_LIST) as ArrayList<List_ItemsDataModel>)
    }

    fun initCurrentPlayer() {
        mCurrentPlayer = MediaPlayer()
        mCurrentPlayer?.let {
            it.isLooping = false
            it.setOnPreparedListener {
                isStarting = true
                it.start()
                it.setOnCompletionListener(onCompletionListener)
            }
        }
    }


    private fun createNextMediaPlayer() {
        Log.d("LoopMediaPlayer", "[test]createNextMediaPlayer 1 ----------> ")
        mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
        try {
            if (mAfd != null) {
                mNextPlayer = MediaPlayer()
                mNextPlayer?.let {
                    it.setDataSource(mAfd!!.getFileDescriptor(), mAfd!!.getStartOffset(), mAfd!!.getLength())
                    it.prepareAsync()
                    it.setAudioAttributes(
                            AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build())
                    it.setOnPreparedListener(OnPreparedListener { mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer) })
                }

            } else if (mFile != null) {
                mNextPlayer = MediaPlayer()
                mNextPlayer?.let {
                    it.setDataSource(mFile!!.getAbsolutePath())
                    it.prepareAsync()
                    it.setAudioAttributes(
                            AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build())
                    it.setOnPreparedListener(OnPreparedListener { mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer) })
                }

            } else {
                var titleName = playerViewModel.playList.value!!.get(playerViewModel.selectPos).musicTitle_kor
                var hertz = playerViewModel.playList.value!!.get(playerViewModel.selectPos).hertz

                if(!chkLoop.isChecked) {
                    playTime_millisecond = 1000 * 60 * playerViewModel.playList.value!!.get(playerViewModel.selectPos).playTime
                    pgPlayTime.max = playTime_millisecond
                }

                var rawId = resources.getIdentifier(getRawName(titleName, hertz), "raw", "com.exercise.music_exercise")
                mNextPlayer = MediaPlayer.create(this, rawId)
                mNextPlayer!!.setOnPreparedListener { mCurrentPlayer!!.setNextMediaPlayer(mNextPlayer) }

                mNextPlayer!!.setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
            }
        } catch (e: Exception) {
        }
    }

    fun getRawName(title: String, hertz: Int): String {
        var strReturn = ""
        when (title) {
            "개울소리1" -> {
                strReturn = "water_01"
            }

            "개울소리2" -> {
                strReturn = "water_02"
            }

            "개울소리3" -> {
                strReturn = "water_03"
            }

            "개울소리4" -> {
                strReturn = "water_04"
            }

            "개울소리5" -> {
                strReturn = "water_05"
            }

            "개울소리6" -> {
                strReturn = "water_06"
            }

            "개울소리7" -> {
                strReturn = "water_07"
            }

            "바람소리1" -> {
                strReturn = "wind_01"
            }

            "바람소리4" -> {
                strReturn = "wind_04"
            }

            "파도소리1" -> {
                strReturn = "wave_01"
            }

            "파도소리2" -> {
                strReturn = "wave_02"
            }

            "음악1" -> {
                strReturn = "music_01"
            }

            "음악2" -> {
                strReturn = "music_02"
            }

            "음악3" -> {
                strReturn = "music_03"
            }

            "비소리1" -> {
                strReturn = "rain_01"
            }

            "비소리2" -> {
                strReturn = "rain_02"
            }

            "비소리3" -> {
                strReturn = "rain_03"
            }

            "비소리4" -> {
                strReturn = "rain_04"
            }

            "비소리5" -> {
                strReturn = "rain_05"
            }

            "화이트노이즈" -> {
                strReturn = "white_noise"
            }

            "핑크노이즈" -> {
                strReturn = "pink_noise"
            }
        }

        if (hertz == 0) {
            strReturn += "_origin"
        } else if (hertz == 2) {
            strReturn += "_2k_cut"
        } else if (hertz == 4) {
            strReturn += "_4k_cut"
        } else if (hertz == 8) {
            strReturn += "_8k_cut"
        }

        return strReturn
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivPlay_Button -> {
                if (isPlaying)
                    onMusicPause()
                else {
                    var item = playerViewModel.playList.value!![playerViewModel.selectPos]
                    onPlay(item.musicTitle_kor, item.hertz)
                    handler.sendEmptyMessage(HANDLER_WHAT_SETTING)
                }
            }

            R.id.btnPlay_TimeSetting -> {
                if(chkLoop.isChecked){
                    Toast.makeText(this, "무한반복이 설정 되어 있을 경우 시간 설정이 불가능합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    var bottomDialogItem: LinkedHashMap<String, String> = LinkedHashMap()
                    bottomDialogItem.put("1분", "1")
                    bottomDialogItem.put("3분", "3")
                    bottomDialogItem.put("5분", "5")
                    bottomDialogItem.put("10분", "10")
                    bottomDialogItem.put("15분", "15")
                    bottomDialogItem.put("30분", "30")
                    bottomDialogItem.put("60분", "60")

                    stop()
                    DialogUtils.showBottomSheetDialog(
                        this,
                        bottomDialogItem,
                        "닫기",
                        R.color.color_font_black,
                        true,
                        object : DialogUtils.OnBottomSheetSelectedListener {
                            override fun onSelected(index: Int, text: String, value: String) {
                                /** delete **/
                                Toast.makeText(this@PlayerActivity, value, Toast.LENGTH_SHORT)
                                    .show()
                                playerViewModel.changePlayTime(value.toInt())
                                setMilliseconds(value.toInt())
                                runningTime = 0
                            }

                        })
                }
            }
        }
    }

    fun setMilliseconds(minute:Int){
        playTime_millisecond = 1000 * 60 * minute
    }

    fun onMusicPause() {
        var item = playerViewModel.playList.value!!.get(playerViewModel.selectPos)

        pause()
        ivPlayer.setImageResource(R.drawable.ic_play)
        isPlaying = false
        getPlayTitle(item.musicTitle_kor, item.hertz)
    }

    fun onPlay(title: String, hertz: Int) {
        if (isStarting) {
            stop()
        }

        var rawID : Int = resources.getIdentifier(getRawName(title, hertz), "raw", "com.exercise.music_exercise")

        mCurrentPlayer = MediaPlayer.create(this, rawID)
        mCurrentPlayer!!.setLooping(false)
        mCurrentPlayer!!.setOnPreparedListener(object : OnPreparedListener {
            override fun onPrepared(mediaPlayer: MediaPlayer) {
                Log.d("LoopMediaPlayer", "[test]isStarting 3 ----------> ")
                isStarting = true
                mCurrentPlayer!!.start()
                mCurrentPlayer!!.setOnCompletionListener(onCompletionListener)
                createNextMediaPlayer()
            }
        })

        mCurrentPlayer!!.setAudioAttributes(
                AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())

        var item = playerViewModel.playList.value!!.get(playerViewModel.selectPos)

        ivPlayer.setImageResource(R.drawable.ic_pause)
        isPlaying = true
        getPlayTitle(item.musicTitle_kor, item.hertz)

    }

    @Throws(IllegalStateException::class)
    fun stop() {
        isStarting = false
        isPlaying = false
        ivPlayer.setImageResource(R.drawable.ic_play)
        if(mCurrentPlayer != null)
            mCurrentPlayer!!.stop()

        if(mNextPlayer != null)
            mNextPlayer!!.stop()
    }

    @Throws(java.lang.IllegalStateException::class)
    fun pause() {
        if(mCurrentPlayer != null)
            mCurrentPlayer!!.pause()
        if(mNextPlayer != null)
            mNextPlayer!!.pause()
    }

    fun getPlayTitle(title:String, hertz:Int){
        var strHertz:String = ""
        when(hertz){
            0 -> {
                strHertz = "원본"
            }
            else ->{
                strHertz = "${hertz}KHz"
            }
        }
        tvTitle.text = "${title}(${strHertz})"
    }

    override fun onDestroy() {
        super.onDestroy()

        stop()
    }

}