package com.exercise.music_exercise.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.List_DefaultItemDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.utils.ViewUtils
import kotlinx.android.synthetic.main.holder_musiclist_detail.view.*

class MusicDetailItemHolder(itemView: View, var listener : onDetailItemListener): RecyclerView.ViewHolder(itemView) {

    interface onDetailItemListener{
        fun onItemSelect(data: List_DefaultItemDataModel, position:Int)
        fun onItemCheck(data:List_DefaultItemDataModel, position:Int)
    }

    fun setDetailItem(itemData:List_DefaultItemDataModel, position:Int, type:String){
        with(itemView){
            tvMusicDetailItem_Title.text = String.format("%s\n(%s)", itemData.musicTitle_kor, if(itemData.hertz == 0)  "원본" else "${itemData.hertz}KHz".toString())
            ViewUtils.loadImage(itemData.image_path, null).into(ivMusicDetailItem_Image)
            clMusicDetailItem_Root.setTag(R.id.list_data, itemData)
            clMusicDetailItem_Root.setTag(R.id.list_position, position)

            if(type == "view") {
                cbMusicDetailItem_Check.visibility = View.GONE
                clMusicDetailItem_Root.setOnClickListener {
                    var exerciseItem: List_DefaultItemDataModel =
                        it.getTag(R.id.list_data) as List_DefaultItemDataModel
                    var pos: Int = it.getTag(R.id.list_position).toString().toInt()

                    listener.onItemSelect(exerciseItem, pos)
                }
            } else if(type == "add"){
                cbMusicDetailItem_Check.visibility = View.VISIBLE
                cbMusicDetailItem_Check.isChecked = itemData.checked

                clMusicDetailItem_Root.setOnClickListener {
                    var exerciseItem: List_DefaultItemDataModel =
                        it.getTag(R.id.list_data) as List_DefaultItemDataModel
                    var pos: Int = it.getTag(R.id.list_position).toString().toInt()

                    listener.onItemCheck(exerciseItem, pos)
                }
            }
        }
    }
}