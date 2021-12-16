package com.exercise.music_exercise.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exercise.health_exercise.adapters.viewHolders.completeList.CompleteHeaderHolder
import com.exercise.health_exercise.adapters.viewHolders.completeList.CompleteItemHolder
import com.exercise.music_exercise.AppContents
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.CommonListDataModel
import com.exercise.music_exercise.data_models.PlayReportDataModel

class CompleteListAdapter(var context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list:ArrayList<CommonListDataModel> ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        var itemView : View ?= null
        var holder : RecyclerView.ViewHolder ?= null
        if(viewType == AppContents.VIEWTYPE_DATEHADER) {
            itemView = inflater.inflate(R.layout.holder_complete_header, parent, false)
            holder = CompleteHeaderHolder(itemView)
        } else if(viewType == AppContents.VIEWTYPE_PLAYITEM){
            itemView = inflater.inflate(R.layout.holder_complete_item, parent, false)
            holder = CompleteItemHolder(itemView)
        }
        return holder!!
    }

    override fun getItemCount(): Int {
        if(list != null && list!!.size > 0)
            return list!!.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewType:Int = list!!.get(position).view_type

        if(viewType == AppContents.VIEWTYPE_DATEHADER){
            var headerDate : String = list!!.get(position).view_item.toString()
            if(!TextUtils.isEmpty(headerDate)) {
                var strDate: String = headerDate.substring(0, 4) + "-" + headerDate.substring(4, 6) + "-" + headerDate.substring(6, 8)
                (holder as CompleteHeaderHolder).setHeaderData(strDate)
            }
        } else if(viewType == AppContents.VIEWTYPE_PLAYITEM){
            var itemData:PlayReportDataModel = list!!.get(position).view_item as PlayReportDataModel
            (holder as CompleteItemHolder).setItem(itemData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType:Int = list!!.get(position).view_type
        return viewType
    }

    fun update(list:ArrayList<CommonListDataModel>?){
        this.list = list
        notifyDataSetChanged()
    }
}