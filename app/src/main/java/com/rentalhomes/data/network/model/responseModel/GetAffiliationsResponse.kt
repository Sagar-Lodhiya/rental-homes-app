package com.rentalhomes.data.network.model.responseModel

class GetAffiliationsResponse(
    var message: String? = "",
    var status: Int = 0,
    var affiliations: ArrayList<Affiliations>
) {
    data class Affiliations(var image: String? = "", var link: String? = "")
}