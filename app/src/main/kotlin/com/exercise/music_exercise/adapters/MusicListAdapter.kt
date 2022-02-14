package com.exercise.music_exercise.adapters

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.adapters.holders.HolderListAdd
import com.exercise.music_exercise.adapters.holders.HolderListAdd.onAddHolderListener
import com.exercise.music_exercise.adapters.holders.MusicListItemHolder
import com.exercise.music_exercise.adapters.holders.MusicListItemHolder.onMusicListItemHolderListener
import com.exercise.music_exercise.data_models.List_HeaderDataModel

class MusicListAdapter(val context: Context, var listener:MusicListAdapter.onMusicListListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(),
onMusicListItemHolderListener,
onAddHolderListener {

    interface onMusicListListener{
        fun onAdd()
        fun onSelectItem(data: List_HeaderDataModel, position:Int)
        fun onMore(data:List_HeaderDataModel, position:Int)
        fun onChecked(data:List_HeaderDataModel, position:Int)
    }

    var musicList: List<List_HeaderDataModel>? = arrayListOf()

    val VIEWTYPE_ITME: Int = 0
    val VIEWTYPE_ADD: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        if (viewType == VIEWTYPE_ITME) {
            val itemView: View = inflater.inflate(R.layout.holder_musiclist, parent, false)
            var holder: RecyclerView.ViewHolder = MusicListItemHolder(context, itemView, this, "view")
            return holder
        } else {
            val itemView: View = inflater.inflate(R.layout.holder_addlist, parent, false)
            var holder: RecyclerView.ViewHolder = HolderListAdd(itemView, this)
            return holder
        }
    }


    fun updateList(list: List<List_HeaderDataModel>) {
        musicList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MusicListItemHolder){
            (holder as MusicListItemHolder).setMusicListItem(
                musicList!!.get(position),
                position
            )
        }
    }

    override fun getItemCount(): Int {
        if (musicList != null && musicList!!.size > 0)
            return musicList!!.size + 1
        else
            return 1
    }

    override fun getItemViewType(position: Int): Int {
        if(position == musicList!!.size){
            return VIEWTYPE_ADD
        } else {
            return VIEWTYPE_ITME
        }
    }

    override fun onSelectItem(data: List_HeaderDataModel, position: Int) {
        listener.onSelectItem(data, position)
    }

    override fun onChecked(data: List_HeaderDataModel, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onMore(data: List_HeaderDataModel, position: Int) {
        listener.onMore(data,position)
    }

    override fun onAddClick() {
        listener.onAdd()
    }
}