package com.rentalhomes.data.network.model.responseModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPropertyListBuyerResponse(
    var message: String = "",
    var status: Int = 0,
    var data: ArrayList<Data>
) {
    data class Data(

        @SerializedName("propertyId")
        @Expose
        var propertyId: Int? = 0,

        @SerializedName("propertyType")
        @Expose
        var propertyType: Int? = 0,

        @SerializedName("propertyImage")
        @Expose
        var propertyImage: String? = "",

        @SerializedName("propertyImageThumb")
        @Expose
        var propertyImageThumb: String? = "",

        @SerializedName("propertyAddress")
        @Expose
        var propertyAddress: String? = "",

        @SerializedName("propertyCity")
        @Expose
        var propertyCity: String? = "",

        @SerializedName("propertyCreatedDate")
        @Expose
        var propertyCreatedDate: String? = "",

        @SerializedName("propertyQRImage")
        @Expose
        var propertyQRImage: String? = "",

        @SerializedName("description")
        @Expose
        var description: String? = "",

        @SerializedName("landSize")
        @Expose
        var landSize: Int? = 0,

        @SerializedName("bed")
        @Expose
        var bed: Int? = 0,

        @SerializedName("bath")
        @Expose
        var bath: Int? = 0,

        @SerializedName("car")
        @Expose
        var car: Int? = 0,

        @SerializedName("propertyStatus")
        @Expose
        var propertyStatus: String? = "",

        @SerializedName("latitude")
        @Expose
        var latitude: String? = "",

        @SerializedName("longitude")
        @Expose
        var longitude: String? = "",

        @SerializedName("agentName")
        @Expose
        var agentName: String? = "",

        @SerializedName("agentProfilePic")
        @Expose
        var agentProfilePic: String? = "",

        @SerializedName("agentDetails")
        @Expose
        var agentDetails: AgentDetails? = null,

        @SerializedName("defaultTemplateType")
        @Expose
        var defaultTemplateType: Int? = 0,

        var isTick: Int = 0,

        @SerializedName("ratingScore")
        @Expose
        var ratingScore:Int?=0

    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
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
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(AgentDetails::class.java.classLoader) as? AgentDetails,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as Int,
            parcel.readValue(Int::class.java.classLoader) as Int
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(propertyId)
            parcel.writeValue(propertyType)
            parcel.writeString(propertyImage)
            parcel.writeString(propertyImageThumb)
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
            parcel.writeString(agentName)
            parcel.writeString(agentProfilePic)
            parcel.writeValue(agentDetails)
            parcel.writeValue(defaultTemplateType)
            parcel.writeValue(isTick)
            parcel.writeValue(ratingScore)
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

    data class AgentDetails(
        val agentId: Int = 0,
        val agentName: String? = "",
        val agentProfilePic: String? = "",
        val agentThumbPic: String? = "",
        val agentMobile: String? = "",
        val agentEmail: String? = "",
        val agentAgency: String? = "",
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(agentId)
            parcel.writeString(agentName)
            parcel.writeString(agentProfilePic)
            parcel.writeString(agentThumbPic)
            parcel.writeString(agentMobile)
            parcel.writeString(agentEmail)
            parcel.writeString(agentAgency)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<AgentDetails> {
            override fun createFromParcel(parcel: Parcel): AgentDetails {
                return AgentDetails(parcel)
            }

            override fun newArray(size: Int): Array<AgentDetails?> {
                return arrayOfNulls(size)
            }
        }
    }

}
