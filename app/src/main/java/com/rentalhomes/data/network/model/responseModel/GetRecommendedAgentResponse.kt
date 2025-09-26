package com.rentalhomes.data.network.model.responseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetRecommendedAgentResponse(
    var message: String = "",
    var status: Int = 0,
    var data: ArrayList<Data>
) {
    data class Data(
        @SerializedName("userId")
        @Expose
        var userId: Int? = 0,

        @SerializedName("buyerName")
        @Expose
        var buyerName: String? = "",

        @SerializedName("buyerImage")
        @Expose
        var buyerImage: String? = "",

        @SerializedName("mobile")
        @Expose
        var mobile: String? = "",

        @SerializedName("propertyCreatedDate")
        @Expose
        var date: String? = ""

    ) {
    }
}