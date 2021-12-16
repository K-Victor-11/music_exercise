package com.exercise.health_exercise.adapters.viewHolders.completeList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.holder_complete_header.view.*

class CompleteHeaderHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    fun setHeaderData(date:String){
        with(itemView){
            tvCompleteHeader_title.text = date
        }
    }
}