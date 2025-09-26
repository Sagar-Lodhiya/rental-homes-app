package com.rentalhomes.data.network.model.responseModel

data class StatisticsResponse(
    val status: Int = 0,
    val message: String? = "",
    val data: ArrayList<Data>? = null,
) {
    data class Data(
        val buyerId: Int = 0,
        val buyerName: String? = "",
        val buyerPhone: String? = "",
        val buyerProfileImage: String? = "",
        val buyerThumbProfileImage: String? = "",
        val filterType: Int = 0,
        val propertyCreatedDate: String = "",
        val Tier1: Tier1? = null,
        val Tier2: Tier2? = null,
        val Tier3: Tier3? = null,
    ) {

    }

    data class Tier1(val ratingScore: Int = 0) {

    }

    data class Tier2(val ratingScore: Int = 0) {

    }

    data class Tier3(
        val likeCount: Int = 0,
        val likePercent: Int = 0,
        val disLikeCount: Int = 0,
        val disLikePercent: Int = 0,
    ) {

    }
}
