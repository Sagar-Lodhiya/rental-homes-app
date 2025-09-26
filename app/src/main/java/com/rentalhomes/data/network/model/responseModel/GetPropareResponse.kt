package com.rentalhomes.data.network.model.responseModel

data class GetPropareResponse(
    val status: Int = 0,
    val message: String? = "",
    val data: Data? = null,
) {

    data class Data(
        val tier1Numbers: Int = 0,
        val tier2Numbers: Int = 0,
        val tier3Numbers: Int = 0,
        val nonRegisteredPropares: Int = 0,
    ) {
    }
}
