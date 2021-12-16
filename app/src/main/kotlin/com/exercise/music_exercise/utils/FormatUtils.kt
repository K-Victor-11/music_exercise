package com.exercise.music_exercise.utils

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class FormatUtils {
    companion object{
        @JvmStatic
        fun string2date(str: String?, fmt: String?, defaultValue: Date?): Date? {
            if (str == null) return defaultValue
            val formatter = SimpleDateFormat(fmt, Locale.KOREAN)
            return formatter.parse(str, ParsePosition(0))
        }

        @JvmStatic
        fun string2date(str: String?, fmt: String?): Date? {
            return string2date(str, fmt, null)
        }


        @JvmStatic
        fun date2string(date: Date?, fmt: String?, defaultValue: String?): String? {
            if (date == null) return defaultValue
            val formatter = SimpleDateFormat(fmt)
            return formatter.format(date)
        }

        @JvmStatic
        fun date2string(date: Date?, fmt: String?): String? {
            return date2string(date, fmt, null)
        }

        @JvmStatic
        fun date2string(date: Date?): String? {
            //DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.format(date)
        }

        @JvmStatic
        fun time2string(date: Date?): String? {
            val format = DateFormat.getTimeInstance(DateFormat.DEFAULT)
            return format.format(date)
        }
    }
}