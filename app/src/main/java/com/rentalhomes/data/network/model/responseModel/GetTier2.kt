package com.rentalhomes.data.network.model.responseModel

data class GetTier2(
    var message: String? = "",
    var status: Int = 0,
    val data: Data? = null
) {
    data class Data(
        val kitchen: Int = 100,
        val livingAreas: Int = 100,
        val bedroom1: Int = 100,
        val bedroom2: Int = 100,
        val bedroom3: Int = 100,
        val bedroom4: Int = 100,
        val bathrooms: Int = 100,
        val mediaRoom: Int = 100,
        val laundry: Int = 100,
        val study: Int = 100,
        val storage: Int = 100,
        val parking: Int = 100,
        val patio: Int = 100,
        val externalAreas: Int = 100,
        val averageScore: Int = 100,
    ) {

    }
}
