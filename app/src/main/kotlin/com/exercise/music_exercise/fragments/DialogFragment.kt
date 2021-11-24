package com.exercise.music_exercise.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.exercise.music_exercise.R

class DialogFragment : AppCompatDialogFragment(), View.OnClickListener {
    private var txtTitle: TextView? = null
    private var txtDesc: TextView? = null
    private var txtMessage: TextView? = null
    private var btnDetail: Button? = null
    private var btnConfirm: Button? = null

    private var listener: ConfirmDialogListener? = null

    interface ConfirmDialogListener {
        fun onConfirmDialogCallback(isOk: Boolean, data: String?)
    }
    companion object{
        const val ARG_CANCELABLE = "ARG_CANCELABLE"
        const val ARG_ICON = "ARG_ICON"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_MESSAGE = "ARG_MESSAGE"
        const val ARG_DESCRIPTION = "ARG_DESCRIPTION"
        const val ARG_TEL_NO = "ARG_TEL_NO"
        const val ARG_DESCRIPTION_SIZE = "ARG_DESCRIPTION_SIZE"

        const val ARG_NO = "ARG_NO"
        const val ARG_YES = "ARG_YES"
        @JvmStatic
        fun newInstance(cancelable: Boolean, @DrawableRes iconResId: Int,
                        title: String?, description: String?, message: String?, no: String?, yes: String?, data: String?, listener: ConfirmDialogListener?): DialogFragment {
            val fragment: DialogFragment = DialogFragment()
            val args = Bundle()
            args.putBoolean(ARG_CANCELABLE, cancelable)
            args.putInt(ARG_ICON, iconResId)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_NO, no)
            args.putString(ARG_YES, yes)
            fragment.setArguments(args)
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater: LayoutInflater = requireActivity().getLayoutInflater()
        val rootView: View = inflater.inflate(R.layout.dialog_message, null)
        txtTitle = rootView.findViewById(R.id.txtTitle)
        txtDesc = rootView.findViewById(R.id.txtDesc)
        btnDetail = rootView.findViewById(R.id.btnDetail)
        btnConfirm = rootView.findViewById(R.id.btnConfirm)
        txtMessage = rootView.findViewById(R.id.txtMessage)
        val cancelable: Boolean = requireArguments().getBoolean(ARG_CANCELABLE)
        val iconResId: Int = requireArguments().getInt(ARG_ICON, 0)
        val title: String? = requireArguments().getString(ARG_TITLE)
        val message: String? = requireArguments().getString(ARG_MESSAGE)
        val description: String? = requireArguments().getString(ARG_DESCRIPTION)
        val no: String? = requireArguments().getString(ARG_NO)
        val yes: String? = requireArguments().getString(ARG_YES)
        setCancelable(cancelable)
        if (TextUtils.isEmpty(title)) {
            /** 타이틀 숨김  */
            txtTitle!!.setVisibility(View.GONE)
            txtDesc!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17f)
            txtDesc!!.setTextColor(this.getResources().getColor(R.color.color_font_black))
        } else {
            /** 타이틀 있음  */
            txtTitle!!.setText(fromHtml(title))
            txtTitle!!.setVisibility(View.VISIBLE)
            txtDesc!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            txtDesc!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_font_black))
        }
        txtDesc!!.setVisibility(if (TextUtils.isEmpty(description)) View.GONE else View.VISIBLE)
        txtDesc!!.setText(fromHtml(description))
        txtMessage!!.setVisibility(if (TextUtils.isEmpty(message)) View.GONE else View.VISIBLE)
        txtMessage!!.setText(fromHtml(message))

        if (!TextUtils.isEmpty(no)) btnDetail!!.setText(no)
        btnDetail!!.setVisibility(if (TextUtils.isEmpty(no)) View.GONE else View.VISIBLE)
        btnDetail!!.setOnClickListener(if (TextUtils.isEmpty(no)) null else this)
        if (!TextUtils.isEmpty(yes)) btnConfirm!!.setText(yes)
        btnConfirm!!.setVisibility(if (TextUtils.isEmpty(yes)) View.GONE else View.VISIBLE)
        btnConfirm!!.setOnClickListener(if (TextUtils.isEmpty(yes)) null else this)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(rootView)
        val dialog = builder.create()
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onClick(v: View) {
        // 앞에 호출해서 java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState 방지
        dismissAllowingStateLoss()
        val data = ""
        if (v === btnDetail) {
            listener?.onConfirmDialogCallback(false, data)
        } else if (v === btnConfirm) {
            listener?.onConfirmDialogCallback(true, data)
        }
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

    fun fromHtml(str: String?): Spanned? {
        var str = str ?: return Html.fromHtml("")
        str = str.replace("\\n".toRegex(), "<br />")
        return Html.fromHtml(str)
    }

}