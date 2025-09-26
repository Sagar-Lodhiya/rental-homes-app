package com.rentalhomes.data.network.model.responseModel

data class RatingDataResponse(
    var message: String? = "",
    var status: Int = 0,
    val data: Data? = null
) {
    data class Data(
        val feedbackData: Feedback? = null,
        val tier1: Tier1? = null,
        val tier2: Tier2? = null,
        val tier3: Tier3? = null,
    )

    data class Feedback(
        val feedback: String = "",
        val buyerImage: String = "",
        val buyerName: String = "",
        val dateTime: String = "",
        val userId: Int = 0,
        val iscustom: Int = 0,
        val questions1: Int = 0,
        val questions2: Int = 0,
        val questions4: String = "",
    )

    data class Tier1(
        val location: Int = 100,
        val streetAppeal: Int = 100,
        val internalLayout: Int = 100,
        val externalLayout: Int = 100,
        val qualityOfBuilding: Int = 100,
        val averageScore: Int = 100,
        val marketValue: String = "",
    )

    data class Tier2(
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
    )

    data class Tier3(
        var defaultTemplateType: Int = 0,
        var isCustomTemplateCreated: Int = 0,
        var likeCount: Int = 0,
        var likePercent: Int = 0,
        var disLikeCount: Int = 0,
        var disLikePercent: Int = 0,
        var tier3Data: ArrayList<Tier3Data>? = null,
    )

    data class Tier3Data(
        var categoryId: Int = 0,
        var category: String? = "",
        var categoryList: ArrayList<Category>? = null,
        var expanded: Boolean = false,
    )

    data class Category(
        var featureId: Int = 0,
        var featureName: String? = "",
        var like: Int = 0,
        var visible: Int = 0,
    )
}

