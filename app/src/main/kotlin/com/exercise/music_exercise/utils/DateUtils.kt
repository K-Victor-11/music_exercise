package com.exercise.music_exercise.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        @JvmStatic
        fun getNowDate(format: String): String {
            val dataFormat: SimpleDateFormat = SimpleDateFormat(format)
            return dataFormat.format(Date())
        }

        @JvmStatic
        fun getNowYear(): Int {
            var calendar: Calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }

        @JvmStatic
        fun getNowMonth(): Int {
            var calendar: Calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH)
        }

        @JvmStatic
        fun getNowDay(): Int {
            var calendar: Calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

        @JvmStatic
        fun getFirstDateOfWeek(year: Int, month: Int): Int {
            if (month >= 0 && month < 12) {
                var calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, 1)

                return calendar.get(Calendar.DAY_OF_WEEK)
            } else return -1
        }

        @JvmStatic
        fun getLastDateOfMonth(year: Int, month: Int): Int {
            if (month >= 0 && month < 12) {
                var calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, 1)

                calendar.add(Calendar.MONTH, 1)
                calendar.add(Calendar.DATE, -1)

                return calendar.get(Calendar.DATE)
            } else return -1
        }

        @JvmStatic
        fun getLimitMonth(year: Int, month: Int, day: Int, limit: Int): Int {
            if (month >= 0 && month < 12) {
                var calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                calendar.add(Calendar.DATE, limit)

                return calendar.get(Calendar.MONTH)
            } else return -1
        }
    }


}