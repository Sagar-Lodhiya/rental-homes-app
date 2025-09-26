package com.rentalhomes.data.network.model.requestModel

class SendFeedbackRequest(
    var userId: Int? = 0,
    var userType: Int? = 0,
    var feedback: String? = "",
    var versionCode: String? = "",
    var osVersion: String? = "",
    var mobileName: String? = "",
)