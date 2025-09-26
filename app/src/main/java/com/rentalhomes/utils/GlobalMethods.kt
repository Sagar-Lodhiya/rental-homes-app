package com.rentalhomes.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.rentalhomes.BuildConfig
import com.rentalhomes.R
import com.rentalhomes.utils.GlobalFields.CAMERA
import com.rentalhomes.utils.GlobalFields.EXT_JPG
import com.rentalhomes.utils.GlobalFields.GALLERY
import com.rentalhomes.utils.GlobalFields.PROVIDER
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object GlobalMethods {

    fun setStatusBarCustomColor(context: Activity, color: Int) {
        val window = context.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = context.getColor(color)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(context, color)
        }
    }

    fun enableButton(context: Context, button: AppCompatButton) {
        button.isEnabled = true
        button.setTextColor(context.getColor(R.color.white))
        button.background = ContextCompat.getDrawable(context, R.drawable.bg_app_theme_button)
    }

    fun disableButton(context: Context, button: AppCompatButton) {
        button.isEnabled = false
        button.setTextColor(context.getColor(R.color.grey_200))
        button.background = ContextCompat.getDrawable(context, R.drawable.box_disable_curved_8)
    }

    fun dp2px(mContext: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            mContext.resources.displayMetrics
        ).toInt()
    }

    fun isValidEmail(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidWord(word: String): Boolean {
        val PATTERN_WORD = "[A-Za-z][^.]*"
        val pattern: Pattern = Pattern.compile(PATTERN_WORD)
        val matcher: Matcher = pattern.matcher(word)
        return matcher.matches()
    }

    fun isValidName(word: String): Boolean {
        val name = word.trim()
        return !name.contains(" ")
    }

    fun isContainEmoji(str: String?): Boolean {
        val emo_regex =
            "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])"
        val matcher = Pattern.compile(emo_regex).matcher(str)
        return matcher.find()
    }

    fun isValidPassword(password: String?): Boolean {
        val PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}"
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun getAddress(
        context: Context?,
        latitude: Double,
        longitude: Double,
        isShortAddress: Boolean
    ): String {
        var strAdd = ""
        val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
        try {
            val addresses = geocoder?.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder()
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = if (isShortAddress) {
                    returnedAddress.subAdminArea + ", " + returnedAddress.adminArea
                } else {
                    returnedAddress.featureName + ", " + returnedAddress.subLocality + ", " + returnedAddress.locality + ", " + returnedAddress.subAdminArea + ", " + returnedAddress.adminArea
                }
                strAdd = strAdd.replace("null, ", "")
                Timber.e("My Current location address : $strAdd")
            } else {
                Timber.e("My Current location address : No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e("My Current location address : Can not get Address!")
        }
        return strAdd
    }

    fun getFilePath(context: Context, uri: Uri?): String? {
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(projection[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()
                return picturePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun makeCameraDir(context: Context): File? {
        try {
            val cacheDir = File(
                Environment.getExternalStorageDirectory(),
                context.resources.getString(R.string.app_name)
            )
            if (!cacheDir.exists()) cacheDir.mkdirs() else cacheDir.delete()
            return cacheDir
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap? {
        val MAX_HEIGHT = 1024
        val MAX_WIDTH = 1024
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var imageStream = context.contentResolver.openInputStream(selectedImage)
        BitmapFactory.decodeStream(imageStream, null, options)
        imageStream!!.close()
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)
        options.inJustDecodeBounds = false
        imageStream = context.contentResolver.openInputStream(selectedImage)
        var img = BitmapFactory.decodeStream(imageStream, null, options)
        img = rotateImageIfRequired(context, img, selectedImage)
        return img
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }
        }
        return inSampleSize
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(context: Context, img: Bitmap?, selectedImage: Uri): Bitmap? {
        val input = context.contentResolver.openInputStream(selectedImage)
        val ei: ExifInterface =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) ExifInterface(input!!) else ExifInterface(
                selectedImage.path!!
            )
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap?, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img!!, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun getdistanceinKM(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun isFileAvailable(context: Context, profilePicFile: File): Boolean {
        var profilePicFile = profilePicFile
        return try {
            profilePicFile =
                File(makeCameraDir(context), System.currentTimeMillis().toString() + EXT_JPG)
            if (profilePicFile.exists()) {
                profilePicFile.delete()
            }
            profilePicFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun openImageCapture(activity: Activity, profilePicFile: File) {
        if (isFileAvailable(activity, profilePicFile)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(
                    activity,
                    BuildConfig.APPLICATION_ID + PROVIDER,
                    profilePicFile
                )
            )
            activity.startActivityForResult(intent, CAMERA)
        }
    }

    fun openImageGallery(activity: Activity, profilePicFile: File) {
        if (isFileAvailable(activity, profilePicFile)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            //        intent.setAction();
            activity.startActivityForResult(
                Intent.createChooser(
                    intent,
                    activity.getString(R.string.use_gallery)
                ), GALLERY
            )
        }
    }

    fun convertDate(date: String?): String {
        var newDate = ""
        try {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
            //            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            val newDateFormat = SimpleDateFormat("hh:mm a")
            newDate = newDateFormat.format(dateFormat.parse(date))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newDate
    }

    private fun hasPermission(context: Context, permission: String): Boolean {
        val res = context.checkCallingOrSelfPermission(permission)
        Timber.e(
            "permission: " + permission + " = \t\t" +
                    if (res == PackageManager.PERMISSION_GRANTED) "GRANTED" else "DENIED"
        )
        return res == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Determines if the context calling has the required permissions
     *
     * @param context     - the IPC context
     * @param permissions - The permissions to check
     * @return true if the IPC has the granted permission
     */
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        var hasAllPermissions = true
        for (permission in permissions) {
            //you can return false instead of assigning, but by assigning you can log all permission values
            if (!hasPermission(context, permission)) {
                hasAllPermissions = false
            }
        }
        return hasAllPermissions
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun compareTwoDates(startDate: Date, endDate: Date): Long {
//milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
        )
        return elapsedMinutes
    }

    fun getAge(dtStart: String): Int {
        val cal = Calendar.getInstance()
        val year: Int
        val month: Int
        val day: Int
        var age: Int
        year = cal[Calendar.YEAR]
        month = cal[Calendar.MONTH]
        day = cal[Calendar.DAY_OF_MONTH]
        cal[getDateMonthYear(dtStart, 1), getDateMonthYear(dtStart, 2)] =
            getDateMonthYear(dtStart, 3)
        age = year - cal[Calendar.YEAR]
        if (month < cal[Calendar.MONTH] || month == cal[Calendar.MONTH] && day < cal[Calendar.DAY_OF_MONTH]) {
            --age
        }
        require(age >= 0) { "Age < 0" }
        return age
    }

    fun calculateAge( /*int year,*/ /*int day,*/
        date: Long
    ): Int {
        val dob = Calendar.getInstance()
        dob.timeInMillis = date
        val today = Calendar.getInstance()
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        //        int age = today.get(Calendar.YEAR) - year;
        if (today[Calendar.DAY_OF_MONTH] < dob[Calendar.DAY_OF_MONTH]) {
//        if(today.get(Calendar.DAY_OF_MONTH) < day){
            age--
        }
        return age
    }

    fun getDateMonthYear(dtStart: String, type: Int): Int {
        /*type = 1 => return day
         * type = 2 => return month
         * type = 3 => return year*/
        var day = 0
        var month = 0
        var year = 0
        val format: SimpleDateFormat
        val date: Date
        val calendar = Calendar.getInstance()
        try {
            if (type == 1) {
                format = SimpleDateFormat("dd")
                date = format.parse(dtStart)
                calendar.time = date
                day = calendar[Calendar.DAY_OF_MONTH]
            } else if (type == 2) {
                format = SimpleDateFormat("MM")
                date = format.parse(dtStart)
                calendar.time = date
                month = calendar[Calendar.MONTH]
            } else {
                format = SimpleDateFormat("yyyy")
                date = format.parse(dtStart)
                calendar.time = date
                year = calendar[Calendar.YEAR]
            }
            Log.e("Date ->", "-- $dtStart - $date - $day - $month - $year")
        } catch (e: ParseException) {
            Log.e("DateException", "==> " + e.localizedMessage)
            e.printStackTrace()
        }
        return if (type == 1) day else if (type == 2) month else year
    }

    fun getAgoTime(string: String?): String {
        var returnString: String
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date
        val date1: Date
        try {
            date = df.parse(string)
            df.timeZone = TimeZone.getDefault()
            date1 = Date()
            returnString = TimeUtils.millisToLongDHMS(date1.time - date.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("getAgoTime", "getAgoTime: " + e.localizedMessage)
            returnString = ""
        }
        return returnString
    }

    fun changeDateFormat(
        dateString: String?,
        sourceDateFormat: String?,
        targetDateFormat: String?
    ): String {
        return try {
            if (dateString == null || dateString.isEmpty()) {
                return ""
            }
            val inputDateFormat = SimpleDateFormat(sourceDateFormat, Locale.getDefault())
            var date: Date? = Date()
            try {
                date = inputDateFormat.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
            outputDateFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun getCurrentDateTime(dateFormat: String?): String {
        @SuppressLint("SimpleDateFormat") val format: DateFormat = SimpleDateFormat(dateFormat)
        val cal = Calendar.getInstance()
        return format.format(cal.time)
    }

    fun getMyDate(weekFlag: Int, dateFormat: String?): String {
        @SuppressLint("SimpleDateFormat") val format: DateFormat = SimpleDateFormat(dateFormat)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, weekFlag)
        return format.format(cal.time)
    }

    fun getMyTime(timeFlag: Int, dateFormat: String?): String {
        @SuppressLint("SimpleDateFormat") val format: DateFormat = SimpleDateFormat(dateFormat)
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, timeFlag)
        return format.format(cal.time)
    }

    fun deleteTempFolder(dir: String, context: Context?) {
        val myDir = File(Environment.getExternalStorageDirectory().toString() + "/" + dir)
        if (myDir.isDirectory) {
            if (myDir.listFiles() != null) {
                val children = myDir.list()
                if (children != null) {
                    for (child in children) {
                        File(myDir, child).delete()
                    }
                }
            }
        }
    }

    fun checkOneHourDifference(startTime: String, endTime: String): Boolean {
        val pattern = "HH : mm"
        val sdf = SimpleDateFormat(pattern)
        val startDate = sdf.parse(startTime)
        val endDate = sdf.parse(endTime)
        val millis: Long = endDate.getTime() - startDate.getTime()
        val hours = (millis / (1000 * 60 * 60)).toInt()
        val mins = (millis / (1000 * 60) % 60).toInt()
        val diff = "$hours:$mins"
        Log.d("checkOneHourDifference", "time difference: ${diff}")
        if (hours < 1) {
            return false
        }
        return true
    }

    fun openLinkInBrowser(context: Context, urlToOpen: String, snackBar: View) {
        var url = urlToOpen
        if (Patterns.WEB_URL.matcher(url).matches()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://${url}"
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)

        } else {
            KeyboardUtils.hideKeyboard(context, snackBar)
            AlertDialogUtils.showSnakeBar(
                context.resources.getString(R.string.invalid_url),
                snackBar,
                context
            )
        }
    }

    fun getTooltipTop(context: Activity, view: View?, text: String?): View? {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        return ViewTooltip
            .on(context, view)
            .autoHide(true, 3000)
            .corner(20)
            .textSize(1, 11f)
            .textColor(ContextCompat.getColor(context, R.color.white))
            .color(ContextCompat.getColor(context, R.color.color_green))
            .textTypeFace(ResourcesCompat.getFont(context, R.font.product_sans_regular))
//            .color(paint)
            .position(ViewTooltip.Position.BOTTOM)
            .text(text + "\n")
            .arrowWidth(20)
            .arrowHeight(40)
            .shadowColor(ContextCompat.getColor(context, R.color.color_green))
            //.padding(50, 50, 50, 50)
            .margin(10, 20, 10, 10)
            .setTextGravity(Gravity.CENTER)
            .distanceWithView(0)
            .clickToHide(true)
            .show()
    }

    fun stringToWords(s: String): ArrayList<String> = s.trim().split(",")
        .filter { it.isNotEmpty() }
        .toList() as ArrayList<String>

    fun pastDate(startDate: Date, endDate: Date): Long {
        //milliseconds
        var different = endDate.time - startDate.time
//        Timber.e("startDate : $startDate")
//        Timber.e("endDate : $endDate")
//        Timber.e("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
//        Timber.e("PAST DAYS: D-$elapsedDays, H-$elapsedHours, M-$elapsedMinutes, S-$elapsedSeconds")
        return elapsedDays
    }

    fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
        return try {
            if (source.height >= source.width) {
                if (source.height <= maxLength) { // if image already smaller than the required height
                    return source
                }
                val aspectRatio = source.width.toDouble() / source.height.toDouble()
                val targetWidth = (maxLength * aspectRatio).toInt()
                Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
            } else {
                if (source.width <= maxLength) { // if image already smaller than the required height
                    return source
                }
                val aspectRatio = source.height.toDouble() / source.width.toDouble()
                val targetHeight = (maxLength * aspectRatio).toInt()
                Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
            }
        } catch (e: java.lang.Exception) {
            source
        }
    }

    fun convertTime(date: String): String {
        var newDate = ""
        try {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
            //            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            val newDateFormat = SimpleDateFormat("hh:mm a")
            newDate = newDateFormat.format(dateFormat.parse(date))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return newDate
    }

    fun callSendNotifications(
        context: Context?,
        deviceType: String,
        deviceToken: String?,
        body: String?,
        title: String?,
        chatId: String?,
        senderId: String?,
        senderName: String?,
        emailId: String?,
        timeStamp: String?,
        userId: String?,
        messageId: String?,
        propertyId: String?,
        propertyAddress: String?
    ) {
//        /** Create handle for the RetrofitInstance interface */
//        val apiInterface: APIInterface =
//            RetrofitInstance.retrofitInstance!!.create(APIInterface::class.java)
//        val request = FirebaseSendNotificationRequest()
//        request.to = deviceToken
//        request.priority = "high"
//        val data: FirebaseSendNotificationRequest.DataBean =
//            FirebaseSendNotificationRequest.DataBean()
//        data.body = body
//        data.title = title
//        data.roomId = chatId
//        data.senderId = senderId
//        data.message = body
//        data.senderName = senderName
//        data.emailId = emailId
//        data.timeStamp = timeStamp
//        data.userId = userId
//        data.propertyAddress = propertyAddress
//        data.propertyId = propertyId
//        data.notificationType = "${Keys.CHAT}"
//
//        data.messageId = messageId
//        request.data = data
//        val dataNotificationOtherPlatform: NotificationRequestOtherPlatform.DataBean =
//            NotificationRequestOtherPlatform.DataBean()
//        dataNotificationOtherPlatform.body = body
//        dataNotificationOtherPlatform.title = title
//        dataNotificationOtherPlatform.roomId = chatId
//        dataNotificationOtherPlatform.senderId = senderId
//        dataNotificationOtherPlatform.message = body
//        dataNotificationOtherPlatform.senderName = senderName
//        dataNotificationOtherPlatform.emailId = emailId
//        dataNotificationOtherPlatform.timeStamp = timeStamp
//        dataNotificationOtherPlatform.userId = userId
//        dataNotificationOtherPlatform.propertyAddress = propertyAddress
//        dataNotificationOtherPlatform.propertyAddress = propertyId
//        dataNotificationOtherPlatform.notificationType = "${Keys.CHAT}"
//
//        data.messageId = messageId
//        if (deviceType == "2") {
//            val call: Call<FirebaseNotifications> =
//                apiInterface.sendNotificationsToAndroid(request)
//            Log.e(
//                "URL Called Android ",
//                call.request().url().toString() + " <=> " + deviceType + " = " + Gson().toJson(
//                    request
//                )
//            )
//            call.enqueue(object : Callback<FirebaseNotifications?> {
//                override fun onResponse(
//                    call: Call<FirebaseNotifications?>,
//                    response: Response<FirebaseNotifications?>
//                ) {
//                    if (response != null) {
////                        FirebaseNotifications mResponse = response.body();
//                        Timber.e("URL Called for Android ====== $response" /*+ "\n" + mResponse.toString()*/)
//                    }
//                }
//
//                override fun onFailure(call: Call<FirebaseNotifications?>, t: Throwable) {
//                    Log.e("onFailure", "")
//                }
//            })
//        } else {
//            val req = NotificationRequestOtherPlatform()
//            req.data = data
//            req.notification = dataNotificationOtherPlatform
//            req.to = deviceToken
//            req.priority = "high"
//            val call: Call<FirebaseNotifications> = apiInterface.sendNotificationsToOtherDevice(req)
//            Log.e(
//                "URL other device",
//                call.request().url().toString() + " <=> " + deviceType + " = " + Gson().toJson(req)
//            )
//            call.enqueue(object : Callback<FirebaseNotifications?> {
//                override fun onResponse(
//                    call: Call<FirebaseNotifications?>,
//                    response: Response<FirebaseNotifications?>
//                ) {
//                    if (response != null) {
////                        FirebaseNotifications mResponse = response.body();
//                        Timber.e("URL Called for other device ====== $response" /*+ "\n" + mResponse.toString()*/)
//                    }
//                }
//
//                override fun onFailure(call: Call<FirebaseNotifications?>, t: Throwable) {
//                    Timber.e("onFailure URL Called for other device")
//                }
//            })
//        }
    }

    suspend fun saveCroppedImage(context: Context, bitmap: Bitmap): Uri? {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        val imagesDir = File(context.filesDir.path + "/Images")
        if (!imagesDir.exists()) {
            imagesDir.mkdir()
        }
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)

        fos.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        fos?.flush()
        fos?.close()
//        MediaScannerConnection.scanFile(context, arrayOf(image.absolutePath),
//            null, null)
//        return FileProvider.getUriForFile(context, "${context.packageName}.provider",
//            image)
        Log.d("Image cropping", "image path: ${image.path}")
        return Uri.fromFile(image)

    }

    fun stringToPrice(price: String): String {
        val formatter = DecimalFormat("#,###,###")
//        val formattedPrice = formatter.format(price)
        val formattedPrice = String.format("%,d", price)
        return "$$formattedPrice"
    }
}