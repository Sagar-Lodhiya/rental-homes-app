package com.rentalhomes.data.pref

import android.content.Context
import com.rentalhomes.data.network.model.User

interface SessionManager {

    fun setSession(context: Context?, session: Boolean)

    fun getSession(context: Context?): Boolean

    fun setString(context: Context?, prefName: String?, key: String?, value: String?)

    fun getString(context: Context?, prefName: String?, key: String?): String?

    fun setInt(context: Context?, prefName: String?, key: String?, value: Int?)

    fun getInt(context: Context?, prefName: String?, key: String?): Int?

    fun setBoolean(context: Context?, prefName: String?, key: String?, value: Boolean?)

    fun getBoolean(context: Context?, prefName: String?, key: String?): Boolean?

    fun setDouble(context: Context?, prefName: String?, key: String?, value: Long?)

    fun getDouble(context: Context?, prefName: String?, key: String?): Long?

    fun setDeviceToken(context: Context?, strDeviceToken: String?)

    fun getDeviceToken(context: Context?): String?

    fun clearPreference(context: Context?)

    fun saveUser(context: Context?, user: User?)

    fun getUser(context: Context?): User?
}