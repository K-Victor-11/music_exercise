package com.exercise.music_exercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.adapters.holders.MusicDetailItemHolder
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel

class MusicListDetailAdapter(val context: Context, val listener:MusicListDetailAdapter.onMusicListDetailListener, val viewType:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
MusicDetailItemHolder.onDetailItemListener{

    var list : List<List_DefaultItemDataModel> ?= null

    interface onMusicListDetailListener{
        fun onItemSelect(data:List_DefaultItemDataModel, position:Int)
        fun onItemChecked(data:List_DefaultItemDataModel, position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView: View = inflater.inflate(R.layout.holder_musiclist_detail, parent, false)
        var holder : RecyclerView.ViewHolder = MusicDetailItemHolder(itemView, this)
        return holder

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MusicDetailItemHolder).setDetailItem(list!!.get(position), position, viewType)
    }

    override fun getItemCount(): Int {
        if(list != null && list!!.size > 0)
            return list!!.size
        else
            return 0
    }

    override fun onItemSelect(data: List_DefaultItemDataModel, position: Int) {
        listener.onItemSelect(data, position)
    }

    override fun onItemCheck(data: List_DefaultItemDataModel, position: Int) {
        listener.onItemChecked(data, position)
    }

    fun updateList(list:List<List_DefaultItemDataModel>){
        this.list = list
        notifyDataSetChanged()
    }
}