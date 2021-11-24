package com.exercise.music_exercise.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.adapters.holders.MusicListItemHolder
import com.exercise.music_exercise.data_models.List_HeaderDataModel

class CustomMusicListAdapter(val context: Context, val listener:CustomMusicListAdapter.onCustomMusicListListener)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    MusicListItemHolder.onMusicListItemHolderListener {

    interface onCustomMusicListListener{
        fun onSelectItem(data: List_HeaderDataModel, position:Int)
    }

    var musicList: List<List_HeaderDataModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView: View = inflater.inflate(R.layout.holder_musiclist, parent, false)
        var holder: RecyclerView.ViewHolder = MusicListItemHolder(context, itemView, this)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MusicListItemHolder).setMusicListItem(
            musicList!!.get(position),
            position
        )
    }

    override fun getItemCount(): Int {
        if (musicList != null && musicList!!.size > 0)
            return musicList!!.size
        else
            return 0
    }

    fun updateList(list: List<List_HeaderDataModel>) {
        musicList = list
        notifyDataSetChanged()
    }

    override fun onSelectItem(data: List_HeaderDataModel, position: Int) {
        listener.onSelectItem(data, position)
    }

    override fun onChecked(data: List_HeaderDataModel, position: Int) {
        /** 사용하지 않음 **/
    }

    override fun onMore(data: List_HeaderDataModel, position: Int) {
        /** 사용하지 않음 **/
    }
}