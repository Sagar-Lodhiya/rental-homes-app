package com.rentalhomes.data.network.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LikeProspectsRequest(
    @SerializedName("userId")
    @Expose
    var userId: Int? = 0,

    @SerializedName("buyerId")
    @Expose
    var buyerId: Int? = 0,

    @SerializedName("propertyId")
    @Expose
    var propertyId: Int? = 0
) {
}