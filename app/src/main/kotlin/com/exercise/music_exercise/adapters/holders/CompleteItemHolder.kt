package com.exercise.health_exercise.adapters.viewHolders.completeList

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.exercise.health_exercise.data.playExerciseItem.PlayExerciseItemHeaderData
import kotlinx.android.synthetic.main.holder_complete_item.view.*

class CompleteItemHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    fun setItem(data : PlayExerciseItemHeaderData){
        with(itemView){
            tvCompleteItem_Title.text = if(TextUtils.isEmpty(data.title)) "" else data.title
            tvCompleteItem_PlayCount.text = data.completeCount.toString()
            tvCompleteItem_TotalCount.text = String.format("/%s", data.playTotalCount.toString())

            var playSec : Int = (data.playTime / 1000L).toInt()

            var playMin : Int = playSec / 60
            playSec = playSec % 60

            var playStr : String = String.format("%02d:%02d", playMin, playSec)
            tvCompleteItem_PlayTime.text = playStr
        }
    }
}