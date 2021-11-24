package com.exercise.music_exercise.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.exercise.music_exercise.activities.BaseActivity

open class BaseFragment : Fragment() {
    var mContext:Context ?= null
    var baseActivity : BaseActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseActivity = activity as BaseActivity?
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}