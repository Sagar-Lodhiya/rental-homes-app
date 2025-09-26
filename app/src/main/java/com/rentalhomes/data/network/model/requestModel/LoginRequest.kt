package com.rentalhomes.data.network.model.requestModel

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("email")
    var email:String?,
    @SerializedName("password")
    var password:String?,
    @SerializedName("deviceType")
    var deviceType:Int?,
    @SerializedName("deviceToken")
    var deviceToken:String?
) {
}