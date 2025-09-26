package com.rentalhomes.data.network.model.requestModel

class ChangePasswordRequest(
    var userId:Int?,
    var userType:Int?,
    var oldPassword:String?,
    var newPassword:String?,
    /*var accessToken:String?*/
) {
}