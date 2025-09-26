package com.rentalhomes.data.network.model.requestModel

class LogoutRequest(
    var userId: Int? = 0,
    var userType: Int? = 0,
    /*var accessToken: String? = "",*/
    var deviceToken: String? = ""
)