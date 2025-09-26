package com.rentalhomes.data.network.model.requestModel

class ChangeEmailRequest(
    var userId:Int?,
    var userType:Int?,
    var oldEmail:String?,
    var newEmail:String?,
    var password:String?
    /*var accessToken:String?*/
) {
}