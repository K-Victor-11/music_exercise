package com.exercise.health_exercise.adapters.viewHolders.completeList

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.data_models.PlayReportDataModel
import kotlinx.android.synthetic.main.holder_complete_item.view.*

class CompleteItemHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    fun setItem(data : PlayReportDataModel){
        with(itemView){
            var strHertz:String = ""
            when(data.hertz){
                0 ->{
                    strHertz = "(0KHz)"
                }
                else ->{
                    strHertz = "(${data.hertz}KHz)"
                }
            }
            tvCompleteItem_Title.text = if(TextUtils.isEmpty(data.musicTitle)) "" else "${data.musicTitle}${strHertz}"
            tvCompleteItem_PlayCount.text = " - ${data.playCount} 회"
        }
    }
}