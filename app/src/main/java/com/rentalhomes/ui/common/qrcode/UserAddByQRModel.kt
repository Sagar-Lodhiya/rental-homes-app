package com.rentalhomes.ui.common.qrcode

import android.os.Parcel
import android.os.Parcelable

class UserAddByQRModel(var userId: Int? = null) : Parcelable {

    constructor(parcel: Parcel) : this() {
        parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAddByQRModel> {
        override fun createFromParcel(parcel: Parcel): UserAddByQRModel {
            return UserAddByQRModel(parcel)
        }

        override fun newArray(size: Int): Array<UserAddByQRModel?> {
            return arrayOfNulls(size)
        }
    }
}
