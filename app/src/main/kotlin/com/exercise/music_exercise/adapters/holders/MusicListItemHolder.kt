package com.exercise.music_exercise.adapters.holders

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_HeaderDataModel

class MusicListItemHolder(val context : Context,
                          var itemView:View,
                          val listener:MusicListItemHolder.onMusicListItemHolderListener,
                          val viewType:String):RecyclerView.ViewHolder(itemView), View.OnClickListener {
    interface onMusicListItemHolderListener{
        fun onSelectItem(data:List_HeaderDataModel, position:Int)
        fun onChecked(data:List_HeaderDataModel, position:Int)
        fun onMore(data:List_HeaderDataModel, position:Int)
    }

    lateinit var clListRoot:ConstraintLayout
    lateinit var tvListTitle:TextView
    lateinit var ivListMenu:ImageView
    lateinit var chkList:CheckBox

    init {
        clListRoot = itemView.findViewById(R.id.clList_Root)
        clListRoot.setOnClickListener(this)

        tvListTitle = itemView.findViewById(R.id.tvList_Title)
        ivListMenu = itemView.findViewById(R.id.ivListMenu)
        chkList = itemView.findViewById(R.id.chkList)
    }

    fun setMusicListItem(data:List_HeaderDataModel, position:Int){
        clListRoot.setTag(R.id.list_data, data)
        clListRoot.setTag(R.id.list_position, position)
        clListRoot.background = ContextCompat.getDrawable(context,R.drawable.bg_radius3_e5e5e5)


        tvListTitle.text = data.listTitle_kor

        ivListMenu.visibility = View.INVISIBLE

        if(viewType == "add")
            chkList.visibility = View.INVISIBLE
        else chkList.visibility = View.VISIBLE

    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.clList_Root -> {
                var listData : List_HeaderDataModel = v.getTag(R.id.list_data) as List_HeaderDataModel
                var pos : Int = v.getTag(R.id.list_position).toString().toInt()
                listener.onSelectItem(listData, pos)
            }
        }
    }
}