package com.rentalhomes.data.network.model.responseModel

data class GetChartDataResponse(
    val status: Int = 0,
    val message: String? = "",
    val data: Data? = null,
) {

    data class Data(
        val tier1Data: List<Int>? = null,
        val tier2Data: List<Int>? = null,
        val tier3Data: List<Int>? = null
    )
}
