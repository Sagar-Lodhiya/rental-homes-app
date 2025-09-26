package com.rentalhomes.ui.common.qrcode

import android.os.Parcel
import android.os.Parcelable

class ScannedQRModel(var propertyId: Int? = null) : Parcelable{

    constructor(parcel: Parcel) : this() {
        parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(propertyId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScannedQRModel> {
        override fun createFromParcel(parcel: Parcel): ScannedQRModel {
            return ScannedQRModel(parcel)
        }

        override fun newArray(size: Int): Array<ScannedQRModel?> {
            return arrayOfNulls(size)
        }
    }
}
