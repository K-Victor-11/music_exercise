package com.exercise.music_exercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.adapters.holders.HolderSettingItem
import com.exercise.music_exercise.data_models.List_ItemsDataModel

class CustomListSettingAdapter(val context: Context, val listener:CustomListSettingAdapter.onSettingListener): RecyclerView.Adapter<RecyclerView.ViewHolder>(), HolderSettingItem.onSelectExerciseItemListener {

    var musicList : ArrayList<List_ItemsDataModel> = arrayListOf()

    interface onSettingListener{
        fun onCountUp(data:List_ItemsDataModel, position:Int)
        fun onCountDown(data:List_ItemsDataModel, position:Int)
        fun onSortUp(data:List_ItemsDataModel, position:Int)
        fun onSortDown(data:List_ItemsDataModel, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView: View = inflater.inflate(R.layout.holder_setting_list, parent, false)
        var holder : RecyclerView.ViewHolder = HolderSettingItem(context, itemView, this)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HolderSettingItem).setSelectExercise(musicList!!.get(position), position, musicList!!.size)
    }

    override fun getItemCount(): Int {
        if(musicList != null && musicList.size > 0)
            return musicList.size
        else return 0
    }

    fun updateList(list:ArrayList<List_ItemsDataModel>){
        this.musicList = list
        notifyDataSetChanged()
    }

    override fun onCountUp(data: List_ItemsDataModel, position: Int) {
        listener.onCountUp(data, position)
    }

    override fun onCountDown(data: List_ItemsDataModel, position: Int) {
        listener.onCountDown(data, position)
    }

    override fun onSortUp(data: List_ItemsDataModel, position: Int) {
        listener.onSortUp(data, position)
    }

    override fun onSortDown(data: List_ItemsDataModel, position: Int) {
        listener.onSortDown(data, position)
    }
}