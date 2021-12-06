package com.exercise.music_exercise.activities

import android.content.res.AssetFileDescriptor
import android.database.ContentObservable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.viewmodels.PlayerViewModel
import java.io.File


class PlayerActivity : BaseActivity(), View.OnClickListener{

    val playerViewModel by lazy {
        ViewModelProvider(this, PlayerViewModel.Factory(this.application)).get(PlayerViewModel::class.java)
    }

    private var mAfd: AssetFileDescriptor? = null
    private var mFile: File? = null

    private var mCurrentPlayer: MediaPlayer? = null
    private var mNextPlayer: MediaPlayer? = null

    lateinit var tvTitle:TextView
    lateinit var ivPlayer:ImageView
    lateinit var seekVolume : SeekBar
    lateinit var btnTimeSetting: Button

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

        seekVolume.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, position: Int, fromUser: Boolean) {
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
            tvTitle.text = it.get(playerViewModel.selectPos).musicTitle_kor
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

    fun getVolumeLevel(isUp:Boolean){
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
        playerViewModel.selectPos = intent.getIntExtra(AppContents.INTENT_DATA_LIST_POSITION, 0)
        playerViewModel.setPlayList(intent.getSerializableExtra(AppContents.INTENT_DATA_PLAY_LIST) as ArrayList<List_ItemsDataModel>)
    }

    fun initCurrentPlayer() {
        mCurrentPlayer = MediaPlayer()
        mCurrentPlayer?.let {
            it.isLooping = false
            it.setOnPreparedListener {
                isStarting = true;
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
                }
            }

            R.id.btnPlay_TimeSetting -> {

            }
        }
    }

    fun onMusicPause() {
        var item = playerViewModel.playList.value!!.get(playerViewModel.selectPos)

        pause()
        ivPlayer.setImageResource(R.drawable.ic_play)
        isPlaying = false
        tvTitle.text = item.musicTitle_kor
    }

    fun onPlay(title: String, hertz: Int) {
        if (isStarting == true) {
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
        tvTitle.text = item.musicTitle_kor
    }

    @Throws(IllegalStateException::class)
    fun stop() {
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

    override fun onDestroy() {
        super.onDestroy()

        stop()
    }

}