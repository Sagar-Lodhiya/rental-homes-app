package com.rentalhomes.data.network.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignUpRequest(
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("agencyName")
    @Expose
    var agencyName: String? = null,

    @SerializedName("promoCode")
    @Expose
    var promoCode: String? = null,

    @SerializedName("suburb")
    @Expose
    var suburb: String? = null,

    @SerializedName("userType")
    @Expose
    var userType: Int? = null,

    @SerializedName("deviceType")
    @Expose
    var deviceType: Int? = null,

    @SerializedName("deviceToken")
    @Expose
    var deviceToken: String? = null
) {
}