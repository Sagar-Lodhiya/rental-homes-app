package com.rentalhomes.data.network.model

import android.os.Parcel
import android.os.Parcelable

data class User(

    var deviceType: String? = null,
    var email: String? = null,
    var id: String? = null,
    var name: String? = null,
    var profilePicture: String? = null,
    var profileColor: String? = null,
    var token: String? = null,
    var currentPassword: String? = null,
    var userType: Int? = 0
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(deviceType)
        parcel.writeString(email)
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(profilePicture)
        parcel.writeString(profileColor)
        parcel.writeString(token)
        parcel.writeString(currentPassword)
        parcel.writeInt(userType!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}
