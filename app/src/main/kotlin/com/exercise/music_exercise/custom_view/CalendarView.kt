package com.exercise.music_exercise.custom_view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.exercise.music_exercise.R
import com.exercise.music_exercise.data_models.PlayReportDataModel
import com.exercise.music_exercise.utils.DateUtils
import com.exercise.music_exercise.utils.FormatUtils
import com.exercise.music_exercise.utils.ViewUtils
import kotlinx.android.synthetic.main.view_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*

class CalendarView : LinearLayout, View.OnClickListener {

    interface onCalendarListener {
        fun onSelectDate(year: String, month: String, day: String)
        fun onPreMonthSelecte(year: String, month: String)
        fun onNextMonthSelecte(year: String, month: String)
    }

    companion object {
        const val LIMITED_DATE: Int = 30
    }

    private var width: Float = 0f
    private var height: Float = 0f

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    private var isCalendarCreate = false
    private var daysTextView: HashMap<String, TextView> = HashMap<String, TextView>()
    private var selectDay: Int = 0
    private var selectDate: String = ""

    private var listener: onCalendarListener? = null

    private var reservationItemData: ArrayList<PlayReportDataModel> = ArrayList<PlayReportDataModel>()

    private var textViewListener: View.OnClickListener = View.OnClickListener {
        var selectTextView: TextView = it as TextView

        if (!TextUtils.isEmpty(selectTextView.text)) {
            try {
                selectDay = (it as TextView).text.toString().toInt()
                if (daysTextView != null && daysTextView.size > 0) {
                    daysTextView.keys.forEachIndexed { index, s ->
                        var tvDays: TextView = daysTextView.get(s)!!
                        setDateTextView(tvDays, s)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        var infService: String = Context.LAYOUT_INFLATER_SERVICE
        var inflater: LayoutInflater = getContext().getSystemService(infService) as LayoutInflater
        var rootView: View = inflater.inflate(R.layout.view_calendar, this, false)
        addView(rootView)

        var array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        var date: Calendar = Calendar.getInstance()

        ivCalendar_PreMonth.setOnClickListener(this)
        ivCalendar_NextMonth.setOnClickListener(this)

        year = array.getInt(R.styleable.CalendarView_selectYear, date.get(Calendar.YEAR))
        month = array.getInt(R.styleable.CalendarView_selectMonth, date.get(Calendar.MONTH))
        day = array.getInt(R.styleable.CalendarView_selectDay, date.get(Calendar.DATE))

//        if (month == date.get(Calendar.MONTH)) {
//            ivCalendar_PreMonth.isEnabled = false
//        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var w = 0
        var h = 0
        val minw = paddingLeft + paddingRight + widthMeasureSpec
        val minh = paddingTop + paddingBottom + heightMeasureSpec
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        when (widthMode) {
            MeasureSpec.AT_MOST ->                 //wrap_content
                w = minw
            MeasureSpec.UNSPECIFIED ->                 //unspecified
                w = minw
            MeasureSpec.EXACTLY ->                 //match_parent
                w = parentWidth
        }
        when (heightMode) {
            MeasureSpec.AT_MOST ->                 //wrap_content
                h = minh
            MeasureSpec.UNSPECIFIED ->                 //unspecified
                h = minh
            MeasureSpec.EXACTLY ->                 //match_parent
                h = parentHeight
        }
        width = w.toFloat()
        height = h.toFloat()
        Log.e("kamuel", "onMeasure")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        Log.e("kamuel", "onLayout")
        if (!isCalendarCreate) {
            isCalendarCreate = true
            getCalendarItems()

            if (daysTextView != null && daysTextView.size > 0) {
                daysTextView.keys.forEachIndexed { index, s ->
                    var tvDays: TextView = daysTextView.get(s)!!
                    setDateTextView(tvDays, s)
                }
            }
        }
    }

    fun setOnCalendarListener(listener: onCalendarListener) {
        this.listener = listener
    }

    private fun getCalendarItems() {

        if (llCalendar_WeekHeader.childCount > 0)
            llCalendar_WeekHeader.removeAllViews()

        if (llCalendar_Area.childCount > 0)
            llCalendar_Area.removeAllViews()


        var textWidth: Float = ViewUtils.dp2px(context, 34f).toFloat()
        var textHeight: Float = ViewUtils.dp2px(context, 32f).toFloat()
        val scale: Float = 32.toFloat() / 34.toFloat()

        textWidth = (width - ViewUtils.dp2px(context, 84f).toFloat()) / 6.0f
        textHeight = textWidth * scale

        /** Header 생성 **/
        for (week_count in 0..6) {
            var textParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(textWidth.toInt(), textHeight.toInt())
            var dayTextView: TextView = getTextView("", week_count)

            dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            dayTextView.setTextColor(ContextCompat.getColor(context, R.color.font_color_black))
            dayTextView.setTypeface(dayTextView.getTypeface(), Typeface.BOLD)

            when (week_count) {
                0 -> dayTextView.text = "일"
                1 -> dayTextView.text = "월"
                2 -> dayTextView.text = "화"
                3 -> dayTextView.text = "수"
                4 -> dayTextView.text = "목"
                5 -> dayTextView.text = "금"
                6 -> dayTextView.text = "토"
            }
            llCalendar_WeekHeader.addView(dayTextView)
            dayTextView.layoutParams = textParam
        }

        /** 시작일 체크 **/
        var dayOfWeek = DateUtils.getFirstDateOfWeek(year, month)
        var lastDay: Int = DateUtils.getLastDateOfMonth(year, month)

        var rowCalendarArea: LinearLayout? = null
        var dayCount: Int = 0

        if (llCalendar_Area.childCount == 0)
            rowCalendarArea = getRowLinearLayout(context)

        /** 첫번째 주에 비어 있는 Item **/
        if (dayOfWeek - 1 > 0) {
            for (emptyCount in 1..dayOfWeek - 1) {
                var textDays: TextView = getTextView("", dayCount)
                var textDaysLayoutParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(textWidth.toInt(), textHeight.toInt())

                rowCalendarArea!!.addView(textDays)
                textDays.layoutParams = textDaysLayoutParam
                dayCount++

                if (dayCount == 7) {
                    llCalendar_Area.addView(rowCalendarArea)
                    dayCount = 0
                    rowCalendarArea = getRowLinearLayout(context)
                }
            }
        }

        for (count in 1..lastDay) {
            var textDays: TextView = getTextView(count.toString(), dayCount)
            var textDaysLayoutParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(textWidth.toInt(), textHeight.toInt())

            rowCalendarArea!!.addView(textDays)
            textDays.layoutParams = textDaysLayoutParam

            daysTextView.put(count.toString(), textDays)

            dayCount++

            if (dayCount == 7) {
                llCalendar_Area.addView(rowCalendarArea)
                if (count != lastDay)
                    dayCount = 0

                rowCalendarArea = getRowLinearLayout(context)
            }
        }

        if (dayCount != 7) {
            for (emptyCount in dayCount..7) {
                var textDays: TextView = getTextView("", dayCount)
                var textDaysLayoutParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(textWidth.toInt(), textHeight.toInt())

                rowCalendarArea!!.addView(textDays)
                textDays.layoutParams = textDaysLayoutParam
                dayCount++

                if (dayCount == 7) {
                    llCalendar_Area.addView(rowCalendarArea)
                    dayCount = 0
                    rowCalendarArea = getRowLinearLayout(context)
                }
            }
        }


        var strMonth: String = (month + 1).toString()
        if (month + 1 < 10)
            strMonth = "0" + (month + 1)

        tvCalendar_Header.text = year.toString() + "." + strMonth

        if (reservationItemData != null && reservationItemData.size > 0) {
            reservationItemData.forEachIndexed { index, reservationItemData ->
                setReservtionData(reservationItemData.playDate, true)
            }
        }
    }

    private fun getRowLinearLayout(context: Context): LinearLayout {
        var rowCalendarArea: LinearLayout = LinearLayout(context)
        var rowParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        rowParam.setMargins(0, ViewUtils.dp2px(context, 5f), 0, 0)
        rowCalendarArea.layoutParams = rowParam
        rowCalendarArea.gravity = Gravity.CENTER

        return rowCalendarArea
    }

    private fun getTextView(text: String, weekPos: Int): TextView {
        var textView: TextView = TextView(context)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.setTag(R.id.item_data, false)
        textView.setTag(R.id.item_position, weekPos)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_979797))
        textView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white))

        return textView
    }

    private fun setDateTextView(tvDays: TextView, s: String) {
        var isReservation: Boolean = if(tvDays.getTag(R.id.item_data) == null) false else tvDays.getTag(R.id.item_data) as Boolean
        var weekPos: Int = tvDays.getTag(R.id.item_position).toString().toInt()


        var calendar: Calendar = Calendar.getInstance()

        var day: Int = calendar.get(Calendar.DATE)
        var month: Int = calendar.get(Calendar.MONTH)
        var today: Int = DateUtils.getNowDay()

        Log.e("kamuel", "setDateTextView ::: " + isReservation)

        if (tvDays != null) {
            if (month == (this as CalendarView).month) {
                Log.e("kamuel", "s ::: " + s)
                Log.e("kamuel", "today ::: " + today)

                if (s != selectDay.toString()) {
                    if (!isReservation) {
                        Log.e("kamuel", "선택 불가!!! ::: ")
                        tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_979797))
                        tvDays.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white))
                    } else {
                        Log.e("kamuel", "선택 가능!!! ::: ")
                        if (weekPos == 6)
                            tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_0091ff))
                        else if (weekPos == 0)
                            tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_ff0000))
                        else
                            tvDays.setTextColor(ContextCompat.getColor(context, R.color.font_color_black))

                        tvDays.setBackgroundResource(R.drawable.bg_calendar_99ccff)
                    }

                    tvDays.setTag(R.id.item_checked, false)

                } else {
                    /** 선택 배경 변경 **/
                    Log.e("kamuel", "check 되라!!! ::: ")
//                        tvDays.setTag(R.id.item_checked, true)
//                        tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_white))
//                        tvDays.setBackgroundColor(ContextCompat.getColor(context, R.color.color_ff6c2d))
//                        if (listener != null) {
//                            var monthInt: Int = (this as CalendarView).month + 1
//                            var monthString = monthInt.toString()
//                            var dayString: String = s
//
//                            if (monthInt < 10)
//                                monthString = String.format("0%s", monthInt.toString())
//
//                            if (s.toInt() < 10)
//                                dayString = String.format("0%s", s)
//
//
//                            selectDate = String.format("%s-%s-%s", (this as CalendarView).year.toString(), monthString, dayString)
//                            listener!!.onSelectDate((this as CalendarView).year.toString(), monthString, dayString)
//                        }
                }
            } else {
                if (s != selectDay.toString()) {
                    if (!isReservation) {
                        tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_979797))
                        tvDays.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white))
                    } else {
                        if (weekPos == 6)
                            tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_0091ff))
                        else if (weekPos == 0)
                            tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_ff0000))
                        else
                            tvDays.setTextColor(ContextCompat.getColor(context, R.color.font_color_black))

                        tvDays.setBackgroundResource(R.drawable.bg_calendar_99ccff)
                    }

                    tvDays.setTag(R.id.item_checked, false)
                } else {
                    /** 선택 배경 변경 **/
//                    tvDays.setTag(R.id.item_checked, true)
//                    tvDays.setTextColor(ContextCompat.getColor(context, R.color.color_white))
//                    tvDays.setBackgroundColor(ContextCompat.getColor(context, R.color.color_ff6c2d))
//                    if (listener != null) {
//                        var monthInt: Int = (this as CalendarView).month + 1
//                        var monthString = monthInt.toString()
//                        var dayString: String = s
//
//                        if (monthInt < 10)
//                            monthString = String.format("0%s", monthInt.toString())
//
//                        if (s.toInt() < 10)
//                            dayString = String.format("0%s", s)
//
//
//                        selectDate = String.format("%s-%s-%s", (this as CalendarView).year.toString(), monthString, dayString)
//                        listener!!.onSelectDate((this as CalendarView).year.toString(), monthString, dayString)
//                    }
                }
            }
        }
    }

    fun setReservtionData(dateString: String, isReservation: Boolean) {
        if (daysTextView != null && daysTextView.size > 0) {
            var format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            var calendar: Calendar = Calendar.getInstance()
            var dateChangeString = dateString.substring(0, 4)+"-"+dateString.substring(4,6)+"-"+dateString.substring(6,8)
            var temp:Date ?= FormatUtils.string2date(dateChangeString, "yyyy-MM-dd")
            calendar.time = FormatUtils.string2date(dateChangeString, "yyyy-MM-dd")

            var day: Int = calendar.get(Calendar.DATE)
            var month: Int = calendar.get(Calendar.MONTH)
            var today: Int = DateUtils.getNowDay()


            if (month == this.month) {
                var dayTextView: TextView = daysTextView.get(day.toString())!!

                if (dayTextView != null) {
                    dayTextView.setTag(R.id.item_data, isReservation)
                    dayTextView.setOnClickListener(textViewListener)
                    setDateTextView(dayTextView, day.toString())

//                    dayTextView.setTag(R.id.item_checked, false)
                }
            }

        }
    }

    fun setReservationData(reservationArray: ArrayList<PlayReportDataModel>) {
        this.reservationItemData = reservationArray

        Handler().postDelayed({
            if (llCalendar_WeekHeader.childCount > 0)
                llCalendar_WeekHeader.removeAllViews()

            if (llCalendar_Area.childCount > 0)
                llCalendar_Area.removeAllViews()

            getCalendarItems()

            if (daysTextView != null && daysTextView.size > 0) {
                daysTextView.keys.forEachIndexed { index, s ->
                    var tvDays: TextView = daysTextView.get(s)!!
                    setDateTextView(tvDays, s)
                }
            }
        }, 300)

    }

    fun setReservationClear() {
        selectDay = 0
        selectDate = ""
        this.reservationItemData.clear()
    }

    fun setSelectClear() {
        selectDay = 0
        selectDate = ""

        if (daysTextView != null && daysTextView.size > 0) {
            run check_clear@{
                daysTextView.keys.forEachIndexed { index, s ->
                    var tvDay: TextView? = daysTextView.get(s)

                    if (tvDay != null) {
                        if (tvDay.getTag(R.id.item_checked) != null) {
                            tvDay.setTag(R.id.item_checked, false)
                            return@check_clear
                        }
                    }
                }
            }
        }
    }

    fun getYear():String{
        return year.toString()
    }

    fun getMonth():String{
        var intMonth:Int = month
        intMonth = intMonth + 1

        var strMonth : String = String.format("%02d", intMonth)
        return strMonth
    }

    fun getDay():String{
        return day.toString()
    }

    fun setReservationDate(reservationDate: String) {

        var calendar: Calendar = Calendar.getInstance()
        calendar.time = FormatUtils.string2date(reservationDate, "yyyy-MM-dd")

        selectDate = reservationDate
        selectDay = calendar.get(Calendar.DATE)

//        if (daysTextView != null && daysTextView.size > 0) {
//            daysTextView.keys.forEachIndexed { index, s ->
//                var tvDays:TextView = daysTextView.get(s)!!
//                setDateTextView(tvDays, s)
//            }
//        }
//        else{
//            ViewUtils.toast(context, "날짜 뷰가 없나???")
//        }
    }

    fun isCheck(): Boolean {
        if (daysTextView != null && daysTextView.size > 0) {
            daysTextView.keys.forEachIndexed { index, s ->
                var tvDay: TextView? = daysTextView.get(s)

                if (tvDay != null) {
                    if (tvDay.getTag(R.id.item_checked) != null) {
                        var isChecked: Boolean = tvDay.getTag(R.id.item_checked) as Boolean
                        if (isChecked) {
                            return true
                        }
                    }
                }
            }

            return false
        }

        return false
    }

    fun isReadyTextView(): Boolean {
        if (daysTextView != null && daysTextView.size > 0)
            return true
        else
            return false
    }

    fun getDayTextView(keys: String): TextView? {
        if (daysTextView != null && daysTextView.size > 0) {
            return daysTextView.get(keys)
        }
        return null
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivCalendar_PreMonth -> {

                isCalendarCreate = false
                if (month - 1 == -1) {
                    month = 11
                    year = year - 1
                } else {
                    year = year
                    month = month - 1
                }

                var date: Calendar = Calendar.getInstance()
                day = date.get(Calendar.DATE)

                setSelectClear()
                getCalendarItems()

//                if (DateUtils.getNowMonth() == month) {
//                    ivCalendar_PreMonth.isEnabled = false
//                    ivCalendar_NextMonth.isEnabled = true
//                }
            }

            R.id.ivCalendar_NextMonth -> {
                isCalendarCreate = false
                if (month + 1 == 12) {
                    month = 0
                    year = year + 1
                } else {
                    month = month + 1
                    year = year
                }

                var date: Calendar = Calendar.getInstance()
                day = date.get(Calendar.DATE)

                setSelectClear()
                getCalendarItems()

                var limitMonth: Int = DateUtils.getLimitMonth(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), LIMITED_DATE)

//                if (limitMonth == month) {
//                    ivCalendar_PreMonth.isEnabled = true
//                    ivCalendar_NextMonth.isEnabled = false
//                }
            }
        }
    }
}