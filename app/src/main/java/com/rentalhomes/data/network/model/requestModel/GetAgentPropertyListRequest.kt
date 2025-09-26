package com.rentalhomes.data.network.model.requestModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAgentPropertyListRequest (
    @SerializedName("userId")
    @Expose
    var userId:Int?=0,

    /*@SerializedName("listType")
    @Expose
    var listType:Int?=0*/
) {

}