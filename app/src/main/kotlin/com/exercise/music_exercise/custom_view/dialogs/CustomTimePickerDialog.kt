package com.exercise.music_exercise.custom_view.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.exercise.music_exercise.R

class CustomTimePickerDialog : DialogFragment() {

    lateinit var listener: onTimePickerListener

    val maxMinute:Int = 10
    val minMinute:Int = 1

    val maxSecond:Int = 59
    val minSecond:Int = 0

    lateinit var minPicker:NumberPicker
    lateinit var secPicker:NumberPicker

    lateinit var tvCancelText: TextView
    lateinit var tvOKText:TextView

    lateinit var llCancel:LinearLayout
    lateinit var llOK:LinearLayout

    interface onTimePickerListener {
        fun onTimePickerCallback(hour: Int, minute: Int, second: Int)
    }

    companion object {
        const val ARG_CANCELABLE = "ARG_CANCELABLE"
        const val ARG_CANCEL_TEXT = "ARG_CANCEL_TEXT"
        const val ARG_OK_TEXT = "ARG_OK_TEXT"
        const val ARG_MINUTE = "ARG_MINUTE"
        const val ARG_SECOND = "ARG_SECOND"

        @JvmStatic
        fun newInstance(
            cancelable: Boolean,
            cancelText:String,
            okText:String,
            minute:Int,
            second:Int,
            listener: onTimePickerListener
        ): CustomTimePickerDialog {
            var fragment: CustomTimePickerDialog = CustomTimePickerDialog()
            val args = Bundle()
            args.putBoolean(ARG_CANCELABLE, cancelable)
            args.putString(ARG_CANCEL_TEXT, cancelText)
            args.putString(ARG_OK_TEXT, okText)
            args.putInt(ARG_MINUTE, minute)
            args.putInt(ARG_SECOND, second)

            fragment.arguments = args
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val rootView: View = inflater.inflate(R.layout.dialog_timepicker, null)

        var cancelable:Boolean = requireArguments().getBoolean(ARG_CANCELABLE)
        var cancelText: String? = requireArguments().getString(ARG_CANCEL_TEXT)
        var okText: String? = requireArguments().getString(ARG_OK_TEXT)

        var minute:Int = requireArguments().getInt(ARG_MINUTE)
        var second:Int = requireArguments().getInt(ARG_SECOND)


        minPicker = rootView.findViewById(R.id.npTimepicker_minute)
        secPicker = rootView.findViewById(R.id.npTimepicker_second)

        tvCancelText = rootView.findViewById(R.id.tvDialogNegative)
        tvOKText = rootView.findViewById(R.id.tvDialogPositive)

        tvCancelText.text = cancelText
        tvOKText.text = okText

        llCancel = rootView.findViewById(R.id.llDialogNegative)
        llOK = rootView.findViewById(R.id.llDialogPositive)

        llCancel.setOnClickListener {
            dismiss()
        }

        llOK.setOnClickListener {
            listener.onTimePickerCallback(0, minPicker.value, secPicker.value)
            dismiss()
        }

//        val minArrayList = arrayOfNulls<String>(maxMinute+1)
//        var secArrayList = arrayOfNulls<String>(maxSecond+1)
//        for(i in 0 .. maxMinute){
//            minArrayList[i] = i.toString()
//        }
//
//        for(i in 0 .. maxSecond){
//            secArrayList[i] = i.toString()
//        }
//
//
//        secPicker.wrapSelectorWheel = false
//        secPicker.displayedValues = secArrayList
//
//        minPicker.wrapSelectorWheel = false
//        minPicker.displayedValues = minArrayList


        minPicker.minValue = 1
        minPicker.maxValue = maxMinute
        if(minute != -1) {
            minPicker.value = minute
        } else {
            minPicker.value = 1
        }

        secPicker.minValue = 0
        secPicker.maxValue = maxSecond
        if(second != -1){
            secPicker.value = second
        } else {
            secPicker.value = 0
        }



        minPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            if(newVal == 10){
                secPicker.value = 0
                secPicker.isEnabled = false
            } else {
                secPicker.isEnabled = true
            }
        }

        secPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            if(oldVal == 59 && newVal == 0){
                if(minPicker.value+1 >= maxMinute){
                    minPicker.value = maxMinute
                } else {
                    minPicker.value = minPicker.value + 1
                }
            } else if(oldVal == 0 && newVal == 59){
                if(minPicker.value -1 <= minSecond) {
                    minPicker.value = minMinute
                } else {
                    minPicker.value = minPicker.value -1
                }
            }
        }


        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(rootView)

        val dialog = builder.create()
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setCancelable(cancelable)

        return dialog
    }


    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.e("ILLEGAL", "Exception", e)
        }
    }
}