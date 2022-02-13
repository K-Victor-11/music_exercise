package com.exercise.music_exercise.adapters.holders

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.utils.ViewUtils
import kotlinx.android.synthetic.main.holder_setting_list.view.*

class HolderSettingItem(var context:Context, itemView:View, var listener:HolderSettingItem.onSelectExerciseItemListener):RecyclerView.ViewHolder(itemView) {
    interface onSelectExerciseItemListener{
        fun onCountUp(data:List_DefaultItemDataModel, position: Int)
        fun onCountDown(data:List_DefaultItemDataModel, position:Int)
        fun onSortUp(data:List_DefaultItemDataModel, position:Int)
        fun onSortDown(data:List_DefaultItemDataModel, position:Int)
    }
    fun setSelectExercise(data: List_DefaultItemDataModel, position:Int, max:Int){
        with(itemView){
            var musicHertz = "원본"

            if(data.hertz != 0)
                musicHertz = "${data.hertz}KHz"

            tvSettingItem_Title.text = String.format("%s(%s)", data.musicTitle_kor, musicHertz)

            ivSettingItem_CountLeft.setTag(R.id.list_data, data)
            ivSettingItem_CountLeft.setTag(R.id.list_position, position)

            ivSettingItem_CountRight.setTag(R.id.list_data, data)
            ivSettingItem_CountRight.setTag(R.id.list_position, position)

            ivSettingItem_SortUp.setTag(R.id.list_data, data)
            ivSettingItem_SortUp.setTag(R.id.list_position, position)

            ivSettingItem_SortDown.setTag(R.id.list_data, data)
            ivSettingItem_SortDown.setTag(R.id.list_position, position)

            ivSettingItem_CountLeft.setOnClickListener {
                var data:List_DefaultItemDataModel = it.getTag(R.id.list_data) as List_DefaultItemDataModel
                var pos:Int = it.getTag(R.id.list_position).toString().toInt()

                listener.onCountDown(data, pos)
            }

            ivSettingItem_CountRight.setOnClickListener {
                var data:List_DefaultItemDataModel = it.getTag(R.id.list_data) as List_DefaultItemDataModel
                var pos:Int = it.getTag(R.id.list_position).toString().toInt()

                listener.onCountUp(data, pos)
            }

            ivSettingItem_SortUp.setOnClickListener {
                var data:List_DefaultItemDataModel = it.getTag(R.id.list_data) as List_DefaultItemDataModel
                var pos:Int = it.getTag(R.id.list_position).toString().toInt()

                if(pos == 0)
                    Toast.makeText(context, "더 이상 올릴 수 없습니다.", Toast.LENGTH_SHORT).show()
                else
                    listener.onSortUp(data, pos)
            }

            ivSettingItem_SortDown.setOnClickListener {
                var data:List_DefaultItemDataModel = it.getTag(R.id.list_data) as List_DefaultItemDataModel
                var pos:Int = it.getTag(R.id.list_position).toString().toInt()

                if(pos >= max-1)
                    Toast.makeText(context, "더 이상 내릴 수 없습니다.", Toast.LENGTH_SHORT).show()
                else
                    listener.onSortDown(data, pos)
            }

            tvSettingItem_Count.text = data.playTime.toString()
        }
    }
}