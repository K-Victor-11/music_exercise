package com.exercise.music_exercise.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.exercise.music_exercise.R
import com.exercise.music_exercise.custom_view.dialogs.CustomTimePickerDialog
import com.exercise.music_exercise.fragments.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogUtils {
    interface OnBottomSheetSelectedListener {
        fun onSelected(index : Int, text : String, value : String)
    }

    companion object{
        /**
         * @param items String 리스트
         * @param tintColor 마지막 아이템 tintcolor 없는경우 -1
         * @param cancelable 백키, dim 화면 클릭시 종료 유무
         */
        @JvmStatic
        fun showBottomSheetDialog(context: Context, items: HashMap<String, String>, cancelMessage: String?, tintColor: Int, cancelable: Boolean, onBottomSheetSelectedListener: OnBottomSheetSelectedListener): BottomSheetDialog {
            val bottomSheetDialog = BottomSheetDialog(context, R.style.AppTheme_BottomSheetDialog).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCanceledOnTouchOutside(cancelable)
                setCancelable(cancelable)
                setOnShowListener(object : DialogInterface.OnShowListener{
                    override fun onShow(dialog: DialogInterface?) {
                        if(dialog is BottomSheetDialog){
                            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
                            bottomSheet?.let {
                                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }
                    }

                })
            }

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                marginStart = ViewUtils.dp2px(context, 10F)
                marginEnd = ViewUtils.dp2px(context, 10F)
                bottomMargin = ViewUtils.dp2px(context, 10F)
            }

            val container = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setLayoutParams(layoutParams)
            }

            val itemContainer = LinearLayout(context).apply {
                setLayoutParams(LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(R.drawable.bg_border_dialog)
            }


            items.keys.forEachIndexed { index, key ->

                val view = inflater.inflate(R.layout.item_bottom_sheet_dialog_text, null)
                val vLine = view.findViewById<View>(R.id.vTextLine)
                val tvText = view.findViewById<TextView>(R.id.tvBottomSheetText).apply {
                    text = key
                }
                if (index == items.size - 1) {
                    vLine.visibility = View.GONE
                    if (tintColor != -1) {
                        tvText.setTextColor(ContextCompat.getColor(context, tintColor))
                    }
                } else {
                    vLine.visibility = View.VISIBLE
                }
                view.setOnClickListener { v ->
                    onBottomSheetSelectedListener.onSelected(index, key, items.get(key)!!)
                    bottomSheetDialog.dismiss()
                }

                itemContainer.addView(view)
            }
            itemContainer.isMotionEventSplittingEnabled = false
            container.addView(itemContainer)
            cancelMessage?.let {
                val cancelParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    topMargin = ViewUtils.dp2px(context, 12F)
                    bottomMargin = ViewUtils.dp2px(context, 9F)
                }
                val view = inflater.inflate(R.layout.item_bottom_sheet_dialog_text, null).apply {
                    setLayoutParams(cancelParams)
                    setBackgroundResource(R.drawable.bg_border_dialog)
                    setOnClickListener { v -> bottomSheetDialog.dismiss() }
                }
                view.findViewById<View>(R.id.vTextLine).apply {
                    visibility = View.GONE
                }
                view.findViewById<TextView>(R.id.tvBottomSheetText).apply {
                    text = it
                    setTextColor(ContextCompat.getColor(context, R.color.font_color_black))
                }
                container.addView(view)
            }

            container.isMotionEventSplittingEnabled = false
            bottomSheetDialog.setContentView(container)
            bottomSheetDialog.show()

            return bottomSheetDialog
        }

        @JvmStatic
        fun showMessageDialog(fm: FragmentManager, title:String, desc:String, message:String, black:String, orange:String, listener: DialogFragment.ConfirmDialogListener){
            val dialog: DialogFragment = DialogFragment.newInstance(false, 0, title, desc, message, black, orange, null, listener)
            dialog.show(fm, DialogFragment::class.java.getName())
        }

        @JvmStatic
        fun showTimePicker(fm:FragmentManager, cancelText:String, okText:String, listener:CustomTimePickerDialog.onTimePickerListener){
            val dialog:CustomTimePickerDialog = CustomTimePickerDialog.newInstance(false, cancelText, okText, -1, -1, listener)
            dialog.show(fm, CustomTimePickerDialog::class.java.name)
        }

        @JvmStatic
        fun showTimePicker(fm:FragmentManager, minute:Int, second:Int, cancelText:String, okText:String, listener:CustomTimePickerDialog.onTimePickerListener){
            val dialog:CustomTimePickerDialog = CustomTimePickerDialog.newInstance(false, cancelText, okText, minute, second, listener)
            dialog.show(fm, CustomTimePickerDialog::class.java.name)
        }
    }
}