package com.exercise.music_exercise.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun pushFragment(layoutID: Int, fragment: Fragment){
        var manager: FragmentManager = supportFragmentManager
        var transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(layoutID, fragment, "fragment")
        transaction.commitAllowingStateLoss()
    }

    fun pushFragment(layoutID: Int, fragment: Fragment, tag:String){
        Log.d("kamuel", "tag :: $tag")
        var manager: FragmentManager = supportFragmentManager
        var transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(layoutID, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    protected fun pushFragment(layoutID: Int, fragment: Fragment, tag:String, backStackTag:String){
        Log.d("kamuel", "tag :: $tag")
        Log.d("kamuel", "backStackTag :: $backStackTag")
        var manager: FragmentManager = supportFragmentManager
        var transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(layoutID, fragment, tag)
        transaction.addToBackStack(backStackTag)
        transaction.commitAllowingStateLoss()
    }

    protected fun currentFragment(): Fragment?{
        if(supportFragmentManager.fragments != null && supportFragmentManager.fragments.size > 0) {
            var fragmentList: ArrayList<Fragment>? = supportFragmentManager.fragments as ArrayList<Fragment>

            return supportFragmentManager.fragments.get(fragmentList!!.size - 1)
        } else
            return null
    }

    protected fun popFragment(fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val trans: FragmentTransaction = manager.beginTransaction()
        trans.remove(fragment)
        trans.commit()
        manager.popBackStack()
    }
}