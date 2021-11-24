package com.exercise.music_exercise.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.exercise.music_exercise.MusicApplication


class ViewUtils {
    companion object{
        @JvmStatic
        fun loadGifImage(url: String, requestOptions: RequestOptions?) : RequestBuilder<GifDrawable> {

            var options:RequestOptions ?= requestOptions

            if (options == null) {
                options = RequestOptions()
            }
            var application : MusicApplication = MusicApplication.getInstance()!!
            var context:Context = application.applicationContext

            var pName:String = application.packageName
            var id:String = url
            var resourceID : Int = application.resources.getIdentifier(id, "raw", pName)

            return Glide.with(context).asGif().load(resourceID).apply(options)

        }

        @JvmStatic
        fun loadImage(url: String, requestOptions: RequestOptions?) : RequestBuilder<Drawable>{

            var options:RequestOptions ?= requestOptions

            if (options == null) {
                options = RequestOptions()
            }
            var application : MusicApplication = MusicApplication.getInstance()!!
            var context:Context = application.applicationContext

            return Glide.with(context)
                .load(url)
                .apply(options)

        }

        @JvmStatic
        fun dp2px(context: Context, dp: Float): Int {
            val mat = context.resources.displayMetrics
            val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mat)
            return px.toInt()
        }

        @JvmStatic
        fun px2dp(context: Context, px: Float): Float {
            val mat = context.resources.displayMetrics
            return px / (mat.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

    }
}