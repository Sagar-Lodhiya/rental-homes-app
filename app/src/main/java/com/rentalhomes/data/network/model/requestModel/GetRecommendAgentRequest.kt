package com.rentalhomes.data.network.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetRecommendAgentRequest(
    @SerializedName("agentId")
    @Expose
    var agentId:Int?=0,

    @SerializedName("propertyId")
    @Expose
    var propertyId:Int?=0,

    @SerializedName("listType")
    @Expose
    var listType:Int?=0
)  {
}