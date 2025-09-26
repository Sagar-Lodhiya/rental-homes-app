package com.rentalhomes.data.network.model.responseModel


class GetProfileResponse(
    var data: Data,
    var status: Int = 0,
    var message: String? = null
) {
    data class Data(
        var userId: Int = 0,
        var userType: Int = 0,
        var profilePicThumb: String? = null,
        var agencyName: String? = null,
        var promoCode: String? = null,
        var profilePic: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var mobile: String? = null,
        var email: String? = null,
        var qrCode: String? = null,
        var suburb: String? = null,
        var postCode: String? = null,
    )
}