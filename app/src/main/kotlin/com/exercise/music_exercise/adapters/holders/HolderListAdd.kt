package com.exercise.music_exercise.adapters.holders

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R

class HolderListAdd(val itemView:View, var listener:HolderListAdd.onAddHolderListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    interface onAddHolderListener{
        fun onAddClick()
    }

    init{
        itemView.findViewById<ConstraintLayout>(R.id.clListAdd_Root).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.clListAdd_Root ->{
                listener.onAddClick()
            }
        }
    }

}