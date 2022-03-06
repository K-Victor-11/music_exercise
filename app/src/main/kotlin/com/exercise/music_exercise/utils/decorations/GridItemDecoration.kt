package com.exercise.music_exercise.utils.decorations

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exercise.music_exercise.utils.ViewUtils

class GridItemDecoration: RecyclerView.ItemDecoration {
    var context : Context?= null

    constructor(context: Context){
        this.context = context
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        //상하 설정

        //상하 설정
        if (position == 0 || position == 1) {
            // 첫번 째 줄 아이템
            outRect.top = ViewUtils.dp2px(context!!, 5f)
            outRect.bottom = ViewUtils.dp2px(context!!, 10f)
        } else {
            outRect.top = ViewUtils.dp2px(context!!, 5f)
            outRect.bottom = ViewUtils.dp2px(context!!, 10f)
        }

        // spanIndex = 0 -> 왼쪽
        // spanIndex = 1 -> 오른쪽

        // spanIndex = 0 -> 왼쪽
        // spanIndex = 1 -> 오른쪽
        val lp =
            view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex

        if (spanIndex == 0) {
            //왼쪽 아이템
            outRect.left = ViewUtils.dp2px(context!!, 10f)
            outRect.right = ViewUtils.dp2px(context!!, 5f)
        } else if (spanIndex == 1) {
            //오른쪽 아이템
            outRect.left = ViewUtils.dp2px(context!!, 5f)
            outRect.right = ViewUtils.dp2px(context!!, 10f)
        }
    }
}