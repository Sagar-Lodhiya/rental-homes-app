package com.rentalhomes.utils

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import com.google.firebase.Timestamp
import com.rentalhomes.R
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
    fun getDate(timeStamp: Long): String {
        var timeStamp = timeStamp
        return try {
            if (timeStamp < 1000000000000L) {
                timeStamp *= 1000
            }
            val sdf: DateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val netDate = Date(timeStamp)
            sdf.format(netDate)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    companion object {
        //2019-07-20 12:42:20

        private val TAG = DateTimeUtil::class.simpleName

        val currentDateTime: String
            get() {
                var strDateTime = ""
                try {
                    val dateFormatGmt = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ) //2019-07-20 12:42:20
                    dateFormatGmt.timeZone = TimeZone.getDefault()
                    strDateTime = dateFormatGmt.format(Date())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return strDateTime
            }

        fun getTimeIn12hours(date: String?): String {
            var newDate = ""
            try {
                val dateFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
                val newDateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                newDate = newDateFormat.format(dateFormat.parse(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return newDate
        }

        fun getTimeIn24hours(date: String?): String {
            var newDate = ""
            try {
                val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                val newDateFormat = SimpleDateFormat("H:mm", Locale.getDefault())
                newDate = newDateFormat.format(dateFormat.parse(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return newDate
        }

        fun getUTCtoCurrentDate(dateString: String?): String {
            val formatter =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            var value: Date? = null
            try {
                value = formatter.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val dateFormatter = SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
            ) // dd/MM/yy
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(value)
        }

        fun convertUTCtoLocal(dateString: String?): String {
            val formatter =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            var value: Date? = null
            try {
                value = formatter.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val dateFormatter =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(value)
        }

        fun convertUTCtoTime(dateString: String?): Date? {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            var value: Date? = null
            try {
                value = formatter.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormatter.timeZone = TimeZone.getDefault()
            val dt = dateFormatter.format(value)
            var date: Date? = null
            try {
                date = dateFormatter.parse(dt)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return date
        }

        fun convertDate(date: String?): String {
            var newDate = ""
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val newDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                newDate = newDateFormat.format(dateFormat.parse(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return newDate
        }

        fun convertUTCDateWithCurrentTime(UTCDate: String?): String {
            var formattedDate = ""
            try {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                df.timeZone = TimeZone.getTimeZone("UTC")
                val date = df.parse(UTCDate)
                df.timeZone = TimeZone.getDefault()
                formattedDate = df.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return formattedDate
        }

        fun convertUTCTimeToDeviceTime(date: String?): String {
            var newDate = ""
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val newDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                newDate = newDateFormat.format(dateFormat.parse(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return newDate
        }

        fun getDate(date: String?): String {
            var newDate = ""
            try {
                val dateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                val newDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                newDate = newDateFormat.format(dateFormat.parse(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return newDate
        }

        fun getTime(date: String?): String {
            var newDate = ""
            try {
                val dateFormat = SimpleDateFormat("H:m", Locale.getDefault())
                val newDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                newDate = newDateFormat.format(dateFormat.parse(date))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return newDate
        }

        fun isYesterday(d: Date): Boolean {
            return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
        }

        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS //60000
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS //3600000
        private const val DAY_MILLIS = 24 * HOUR_MILLIS //86400000
        private const val WEEK_MILLIS = 7 * DAY_MILLIS
        fun getTimeAgo(time: Long, originalTimeFromAPI: String, ctx: Context?): CharSequence {
            var time = time
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now = System.currentTimeMillis()
            if (time > now || time <= 0) {
//            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//            cal.setTimeInMillis(time);
//            return DateFormat.format("MMM dd yyyy", cal).toString();
                return originalTimeFromAPI.split(" ".toRegex()).toTypedArray()[0]
            }
            val diff:Long = now - time
            return if (diff < MINUTE_MILLIS) {
                "Few sec ago"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "1 min ago"
            } else if (diff < 60 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " mins ago"
            } else if (diff < 2 * HOUR_MILLIS) {
                "1 hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else if (diff < 2 * DAY_MILLIS) {
                "Yesterday"
            } else {
//            return diff / DAY_MILLIS + " days ago";
                originalTimeFromAPI.split(" ".toRegex()).toTypedArray()[0]
            }
            //        else if (diff < 365 * DAY_MILLIS) {
//            try {
//                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//                cal.setTimeInMillis(time);
//                return DateFormat.format("MMM dd yyyy", cal).toString();
//            } catch (Exception ex) {
//                return "xx";
//            }
//        } else {
//            try {
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(time);
//                return DateFormat.format("yyyy-MM-dd", cal).toString();
//            } catch (Exception ex) {
//                return "xx";
//            }
//        }
        }

        fun getTimeDifference(mDate: String?): CharSequence {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            var date: Date? = null
            try {
                date = formatter.parse(mDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date == null) {
                return ""
            }
            var time = date.time
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now = System.currentTimeMillis()
            if (time > now || time <= 0) {
                val cal = Calendar.getInstance(Locale.ENGLISH)
                cal.timeInMillis = time
                return android.text.format.DateFormat.format("MMM dd yyyy", cal).toString()
            }
            val diff = now - time
            return if (diff < MINUTE_MILLIS) {
                "Just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "A Minute ago"
            } else if (diff < 60 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " Minutes ago"
            } else if (diff < 2 * HOUR_MILLIS) {
                "1 Hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " Hours ago"
            } else if (diff < 2 * DAY_MILLIS) {
                "Yesterday"
            } else if (diff < 7 * DAY_MILLIS) {
                (diff / DAY_MILLIS).toString() + " Days ago"
                //        } else if (diff < 365 * DAY_MILLIS) {
//            try {
//                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//                cal.setTimeInMillis(time);
//                return DateFormat.format("MMM dd yyyy", cal).toString();
//            } catch (Exception ex) {
//                return "xx";
//            }
            } else {
                try {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = time
                    android.text.format.DateFormat.format("yyyy-MM-dd", cal).toString()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    ""
                }
            }
        }

        fun getTimedisp(time: Long): CharSequence {
            var time = time
            return try {
                if (time < 1000000000000L) {
                    time *= 1000
                }
                val cal = Calendar.getInstance(Locale.ENGLISH)
                cal.timeInMillis = time
                android.text.format.DateFormat.format("dd MMM,yyyy", cal).toString()
            } catch (ex: Exception) {
                "xx"
            }
        }

        fun getMessageTime(context: Context,timestamp: Timestamp): String{
            var date: Date = timestamp.toDate()

            var timeStampString = date.toString()
            var messageTime = GlobalMethods.convertTime(timeStampString)
            try {
                val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy")
                val currentDate =
                    GlobalMethods.getMyDate(0, "dd MMM, yyyy")
                val timeDate = GlobalMethods.changeDateFormat(
                    date.toString(),
                    "EEE MMM dd HH:mm:ss zzz yyyy",
                    "dd MMM, yyyy"
                )
                val cDate = simpleDateFormat.parse(currentDate)
                val eDate = simpleDateFormat.parse(timeDate)
                val pastDays = GlobalMethods.pastDate(cDate, eDate)

                if (pastDays < -1) {
                    messageTime = GlobalMethods.changeDateFormat(
                        date.toString(),
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "dd MMM, yyyy"
                    )
                } else if (pastDays == -1L) {
                    messageTime = context.getString(R.string.yesterday)
                }

                Log.d(TAG, "Past days: ${pastDays}")

            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "Time conversion exception: ${e.localizedMessage}"
                )
                e.printStackTrace()
            }

            return messageTime
        }

        fun getNotiDate(context: Context,timestamp: Timestamp): String{
            var date: Date = timestamp.toDate()
            Log.e("TAG", "getNotiDate: ${date} " )
            var timeStampString = date.toString()
            var messageTime = GlobalMethods.convertTime(timeStampString)
            try {
                val simpleDateFormat = SimpleDateFormat("dd MMM yyyy")
                val currentDate =
                    GlobalMethods.getMyDate(0, "dd MMM yyyy")
                val timeDate = GlobalMethods.changeDateFormat(
                    date.toString(),
                    "EEE MMM dd HH:mm:ss zzz yyyy",
                    "dd MMM yyyy"
                )
                val cDate = simpleDateFormat.parse(currentDate)
                val eDate = simpleDateFormat.parse(timeDate)
                val pastDays = GlobalMethods.pastDate(cDate, eDate)

                if (pastDays < -1) {
                    messageTime = GlobalMethods.changeDateFormat(
                        date.toString(),
                        "EEE MMM dd HH:mm:ss zzz yyyy",
                        "dd MMM yyyy"
                    )
                } else if (pastDays == -1L) {
                    messageTime = context.getString(R.string.yesterday)
                }else if(pastDays == 0L) {
                    messageTime = "Today"
                }

                Log.d(TAG, "Past days: ${pastDays}")

            } catch (e: Exception) {
                Log.d(
                    TAG,
                    "Time conversion exception: ${e.localizedMessage}"
                )
                e.printStackTrace()
            }

            return messageTime
        }
    }
}