package com.rentalhomes.data.network.model.responseModel

import android.os.Build
import android.os.Parcel
import android.os.Parcelable

data class GetTier3(
    var message: String? = "",
    var status: Int = 0,
    var defaultTemplateType: Int = 0,
    var isCustomTemplateCreated: Int = 0,
    var likeCount: Int = 0,
    var likePercent: Int = 0,
    var disLikeCount: Int = 0,
    var disLikePercent: Int = 0,
    var data: ArrayList<Data>? = null,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        arrayListOf<Data>().apply {
            if (Build.VERSION.SDK_INT >= 29)
                parcel.readParcelableList(this, Data::class.java.classLoader)
            else
                parcel.readList(this, Data::class.java.classLoader)
        }) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(message)
        parcel.writeValue(status)
        parcel.writeValue(defaultTemplateType)
        parcel.writeValue(isCustomTemplateCreated)
        parcel.writeValue(status)
        parcel.writeValue(likeCount)
        parcel.writeValue(likePercent)
        parcel.writeValue(disLikeCount)
        parcel.writeValue(disLikePercent)
        if (Build.VERSION.SDK_INT >= 29) {
            parcel.writeParcelableList(data, flags)
        } else {
            parcel.writeList(data)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetTier3> {
        override fun createFromParcel(parcel: Parcel): GetTier3 {
            return GetTier3(parcel)
        }

        override fun newArray(size: Int): Array<GetTier3?> {
            return arrayOfNulls(size)
        }
    }

    data class Data(
        var categoryId: Int = 0,
        var category: String? = "",
        var categoryList: ArrayList<Category>? = null,
        var expanded: Boolean = false,
    ) : Parcelable {

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            arrayListOf<Category>().apply {
                if (Build.VERSION.SDK_INT >= 29)
                    parcel.readParcelableList(this, Category::class.java.classLoader)
                else
                    parcel.readList(this, Category::class.java.classLoader)
            },
            parcel.readByte() != 0.toByte(),
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(categoryId)
            parcel.writeValue(category)
            if (Build.VERSION.SDK_INT >= 29) {
                parcel.writeParcelableList(categoryList, flags)
            } else {
                parcel.writeList(categoryList)
            }
            parcel.writeByte((if (expanded) 1 else 0).toByte())
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Category(
        var featureId: Int = 0,
        var featureName: String? = "",
        var like: Int = 0,
        var visible: Int = 0,
    ) : Parcelable {

        constructor(`in`: Parcel) : this() {
            featureId = `in`.readValue(Int::class.java.classLoader) as Int
            featureName = `in`.readValue(String::class.java.classLoader) as String?
            like = `in`.readValue(Int::class.java.classLoader) as Int
            visible = `in`.readValue(Int::class.java.classLoader) as Int
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeValue(featureId)
            dest.writeValue(featureName)
            dest.writeValue(like)
            dest.writeValue(visible)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Category> {
            override fun createFromParcel(parcel: Parcel): Category {
                return Category(parcel)
            }

            override fun newArray(size: Int): Array<Category?> {
                return arrayOfNulls(size)
            }
        }
    }
}