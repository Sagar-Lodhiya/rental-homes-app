package com.rentalhomes.data.network.model.responseModel

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    var message: String = "",
    var status: Int = 0,
    var data: Data

) {

    //=================================================================================================//
    data class Data(
        @SerializedName("accessToken")
        @Expose
        var accessToken: String = "",

        @SerializedName("userId")
        @Expose
        var userId: Int = 0,

        @SerializedName("userType")
        @Expose
        var userType: Int = 0,

        @SerializedName("profilePic")
        @Expose
        var profilePic: String = "",

        @SerializedName("profilePicThumb")
        @Expose
        var profilePicThumb: String = "",

        @SerializedName("firstName")
        @Expose
        var firstName: String = "",

        @SerializedName("lastName")
        @Expose
        var lastName: String = "",

        @SerializedName("mobile")
        @Expose
        var mobile: String = "",

        @SerializedName("email")
        @Expose
        var email: String = "",

        @SerializedName("qrCode")
        @Expose
        var qrCode: String = "",

        @SerializedName("suburb")
        @Expose
        var suburb: String = "",

        @SerializedName("postCode")
        @Expose
        var postCode: String = "",

        @SerializedName("agencyName")
        @Expose
        var agencyName: String = "",

        @SerializedName("promoCode")
        @Expose
        var promoCode: String = ""

        ) {

    }
}