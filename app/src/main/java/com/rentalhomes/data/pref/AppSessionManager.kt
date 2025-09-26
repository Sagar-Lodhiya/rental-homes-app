package com.rentalhomes.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.rentalhomes.data.network.model.User
import timber.log.Timber

class AppSessionManager : SessionManager {

    /**
     * Set DeviceToken
     *
     * @param context - context of the screen
     * @param strDeviceToken - Device Token
     *
     */
    override fun setDeviceToken(context: Context?, strDeviceToken: String?) {
        try {
            Timber.e("strDeviceToken:-:$strDeviceToken")
            val prefDeviceToken = context?.applicationContext?.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor? = prefDeviceToken?.edit()
            editor?.putString(PREF_KEY_DEVICE_TOKEN, strDeviceToken)
            editor?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get DeviceToken
     *
     * @param context - context of the screen
     *
     */
    override fun getDeviceToken(context: Context?): String {
        return try {
            val pref = context?.applicationContext?.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            pref?.getString(PREF_KEY_DEVICE_TOKEN, "")!!
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Set Session
     *
     * @param context - context of the screen
     * @param session - Session Value
     *
     */
    override fun setSession(context: Context?, session: Boolean) {
        try {
            val prefDeviceToken =
                context?.applicationContext?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = prefDeviceToken?.edit()
            editor?.putBoolean(PREF_KEY_SESSION, session)
            editor?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get Session
     *
     * @param context - context of the screen
     *
     */
    override fun getSession(context: Context?): Boolean {
        return try {
            val pref =
                context?.applicationContext?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            pref?.getBoolean(PREF_KEY_SESSION, false)!!
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Set String
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     * @param value - value of the data
     *
     */
    override fun setString(context: Context?, prefName: String?, key: String?, value: String?) {
        try {
            val prefDeviceToken =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefDeviceToken!!.edit()
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get String
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     *
     * */
    override fun getString(context: Context?, prefName: String?, key: String?): String? {
        return try {
            val pref =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref?.getString(key, "")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * Set Integer
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     * @param value - value of the data
     *
     */
    override fun setInt(context: Context?, prefName: String?, key: String?, value: Int?) {
        try {
            val prefDeviceToken =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefDeviceToken!!.edit()
            value?.let { editor.putInt(key, it) }
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Get Integer
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     *
     */
    override fun getInt(context: Context?, prefName: String?, key: String?): Int? {
        return try {
            val pref =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref?.getInt(key, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


    /**
     * Set Boolean
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     * @param value - value of the data
     *
     */
    override fun setBoolean(context: Context?, prefName: String?, key: String?, value: Boolean?) {
        try {
            val prefDeviceToken =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefDeviceToken!!.edit()
            value?.let { editor.putBoolean(key, it) }
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get Boolean
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     *
     */
    override fun getBoolean(context: Context?, prefName: String?, key: String?): Boolean? {
        return try {
            val pref =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref?.getBoolean(key, false)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Set Double
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     * @param value - value of the data
     *
     */
    override fun setDouble(context: Context?, prefName: String?, key: String?, value: Long?) {
        try {
            val prefDeviceToken =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefDeviceToken!!.edit()
            value?.let { editor.putLong(key, it) }
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get Double
     *
     * @param context - context of the screen
     * @param prefName - Preference Name
     * @param key - key of the data
     *
     */
    override fun getDouble(context: Context?, prefName: String?, key: String?): Long? {
        return try {
            val pref =
                context?.applicationContext?.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref?.getLong(key, 0L)
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    /**
     * Clear Preference
     *
     * @param context - context of the screen
     *
     */
    override fun clearPreference(context: Context?) {
        val pref =
            context?.applicationContext?.getSharedPreferences(REMEMBER_PREF, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = pref?.edit()
        editor?.clear()
        editor?.apply()
    }

    override fun saveUser(context: Context?, user: User?) {
        try {
            val prefDeviceToken = context?.applicationContext?.getSharedPreferences(REMEMBER_PREF, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = prefDeviceToken?.edit()
            if (editor != null) {
                if (user != null) {
//                    editor.putInt(PREF_KEY_USER_TYPE, user.userType)
//                    editor.putInt(PREF_KEY_AGENT_ID, user.agentId)
//                    editor.putInt(PREF_KEY_BUYER_ID, user.buyerId)
//                    editor.putString(PREF_KEY_ACCESS_TOKEN, user.accessToken)
//                    editor.putString(PREF_KEY_FIRST_NAME, user.firstName)
//                    editor.putString(PREF_KEY_LAST_NAME, user.lastName)
                    editor.putString(PREF_KEY_DEVICE_TYPE, user.deviceType)
                    editor.putString(PREF_KEY_EMAIL, user.email)
                    editor.putString(PREF_KEY_ID, user.id)
                    editor.putString(PREF_KEY_NAME, user.name)

//                    editor.putString(PREF_KEY_MOBILE, user.mobile)
//                    editor.putString(PREF_KEY_AGENCY, user.agency)
                    //    editor.putString(PREF_KEY_DOB, user.dob)
                    editor.putString(PREF_KEY_PROFILE_IMAGE, user.profilePicture)
                    //   editor.putString(PREF_KEY_WEEK, user.week)
                    editor.putString(PREF_KEY_PASSWORD, user.currentPassword)
//                    editor.putInt(PREF_KEY_IS_PROFILE_SETUP, user.isProfileSetUp)
                    editor.apply()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getUser(context: Context?): User? {
        return try {
            val user = User()
            val pref = context?.applicationContext?.getSharedPreferences(REMEMBER_PREF, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor? = pref?.edit()
            if (pref != null) {
                user.userType = pref.getInt(PREF_KEY_USER_TYPE, 0)
//                user.buyerId = pref.getInt(PREF_KEY_BUYER_ID, 0)
//                user.agentId = pref.getInt(PREF_KEY_AGENT_ID, 0)
//                user.accessToken = pref.getString(PREF_KEY_ACCESS_TOKEN, "")
//                user.firstName = pref.getString(PREF_KEY_FIRST_NAME, "")
//                user.lastName = pref.getString(PREF_KEY_LAST_NAME, "")
                user.deviceType = pref.getString(PREF_KEY_DEVICE_TYPE,"2")
                user.email = pref.getString(PREF_KEY_EMAIL, "")
                user.id= pref.getString(PREF_KEY_ID, "")
                user.name = pref.getString(PREF_KEY_NAME, "")

//                user.mobile = pref.getString(PREF_KEY_MOBILE, "")
//                user.agency = pref.getString(PREF_KEY_AGENCY, "")
                // user.dob = pref.getString(PREF_KEY_DOB, "")
                user.profilePicture = pref.getString(PREF_KEY_PROFILE_IMAGE, "")
                user.currentPassword = pref.getString(PREF_KEY_PASSWORD, "")
                //            user.setWeek(pref.getString(PREF_KEY_WEEK, ""));
//                user.isProfileSetUp = pref.getInt(PREF_KEY_IS_PROFILE_SETUP, 0)
                //            user.setAge(pref.getInt(PREF_KEY_AGE, 0));
            }
            editor?.apply()
            user
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     *
     * Companion Object
     * Add all your preference keys here only
     *
     * */
    companion object {
        const val PREF_NAME = "APP_PREFERENCE"

        const val REMEMBER_PREF = "REMEMBER_PREF"
        const val PREF_KEY_DEVICE_TOKEN = "PREF_KEY_DEVICE_TOKEN"
        const val PREF_KEY_SESSION = "PREF_KEY_SESSION"
        const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
        const val PREF_KEY_USER_TYPE = "PREF_KEY_USER_TYPE"     //Agent = 0 & Buyer = 1
        const val PREF_KEY_MOBILE = "PREF_KEY_MOBILE"
        const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        const val PREF_KEY_FIRST_NAME = "PREF_KEY_FIRST_NAME"
        const val PREF_KEY_LAST_NAME = "PREF_KEY_LAST_NAME"
        const val PREF_KEY_EMAIL = "PREF_KEY_EMAIL"
        const val PREF_KEY_QR_CODE = "PREF_KEY_QR_CODE"
        const val PREF_KEY_SUBURB = "PREF_KEY_SUBURB"
        const val PREF_KEY_POST_CODE = "PREF_KEY_POST_CODE"
        const val PREF_KEY_PROFILE_IMAGE = "PREF_KEY_PROFILE_IMAGE"
        const val PREF_KEY_PROFILE_THUMB = "PREF_KEY_PROFILE_THUMB"
        const val PREF_KEY_DOB = "PREF_KEY_DOB"
        const val PREF_KEY_PROMO_CODE = "PREF_KEY_PROMO_CODE"
        const val PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD"

        const val PREF_KEY_AGENCY_NAME = "PREF_KEY_AGENCY_NAME"
        const val PREF_CAMERA_AND_READ_WRITE_PERMISSION_ASKED =
            "PREF_CAMERA_AND_READ_WRITE_PERMISSION_ASKED"
        const val PREF_PERMISSIONS = "PREF_PERMISSIONS"

        const val PREF_KEY_DEVICE_TYPE = "PREF_KEY_DEVICE_TYPE"
        const val PREF_KEY_ID = "PREF_KEY_ID"
        const val PREF_KEY_NAME = "PREF_KEY_NAME"
        const val PREF_KEY_IS_COMPARE_MODE = "PREF_KEY_IS_COMPARE_MODE"

        private var mSession: AppSessionManager? = null

        //        Create Class Instance
        val instance: AppSessionManager?
            get() {
                if (mSession == null) {
                    mSession = AppSessionManager()
                }
                return mSession
            }
    }
}