package com.rentalhomes.data.network.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetPropertyListBuyerRequest(
    @SerializedName("userId")
    @Expose
    var userId:Int?=0,

    @SerializedName("filterType")
    @Expose
    var filterType:Int?=0
) {

}