package com.rentalhomes.data.network.model.requestModel

data class OtpVerificationRequest(var email: String = "", var deviceType: Int = 0, var otp: Int = 0)
