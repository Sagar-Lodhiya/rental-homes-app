package com.rentalhomes.data.network.model.requestModel

data class GetOTPRequest(var email: String = "", var deviceType: Int = 0)