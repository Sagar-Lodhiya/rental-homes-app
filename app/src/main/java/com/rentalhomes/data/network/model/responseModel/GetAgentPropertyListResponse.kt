package com.rentalhomes.data.network.model.responseModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAgentPropertyListResponse(
    var message: String = "",
    var status: Int = 0,
    var data : ArrayList<Data>
) {
    data class Data(
        @SerializedName("propertyId")
        @Expose
        var propertyId :Int=0,

        @SerializedName("propertyImage")
        @Expose
        var propertyImage : String?,

        @SerializedName("propertyAddress")
        @Expose
        var propertyAddress : String?,

        @SerializedName("propertyCity")
        @Expose
        var propertyCity:String?,

        @SerializedName("propertyCreatedDate")
        @Expose
        var propertyCreatedDate: String?,

        @SerializedName("propertyQRImage")
        @Expose
        var propertyQRImage :String?,

        @SerializedName("description")
        @Expose
        var description:String?,

        @SerializedName("landSize")
        @Expose
        var landSize :Int?,

        @SerializedName("bed")
        @Expose
        var bed :Int?,

        @SerializedName("bath")
        @Expose
        var bath :Int?,

        @SerializedName("car")
        @Expose
        var car: Int?,

        @SerializedName("propertyStatus")
        @Expose
        var propertyStatus:String?,

        @SerializedName("latitude")
        @Expose
        var latitude:String?,

        @SerializedName("longitude")
        @Expose
        var longitude:String?,

        @SerializedName("listType")
        @Expose
        var listType:Int?
    ):Parcelable{
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(propertyId)
            parcel.writeString(propertyImage)
            parcel.writeString(propertyAddress)
            parcel.writeString(propertyCity)
            parcel.writeString(propertyCreatedDate)
            parcel.writeString(propertyQRImage)
            parcel.writeString(description)
            parcel.writeValue(landSize)
            parcel.writeValue(bed)
            parcel.writeValue(bath)
            parcel.writeValue(car)
            parcel.writeString(propertyStatus)
            parcel.writeString(latitude)
            parcel.writeString(longitude)
            parcel.writeValue(listType)
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
}