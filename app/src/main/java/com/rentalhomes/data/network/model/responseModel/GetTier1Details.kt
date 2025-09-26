package com.rentalhomes.data.network.model.responseModel

data class GetTier1Details(
    val message: String? = "",
    val status: Int = 0,
    val data: Data? = null
) {
    data class Data(
        val details: Details? = null,
        val scoring: Scoring? = null,
    ) {

    }

    data class Details(
        var question1: Int?,
        var question2: Int?,
        var question3: Int?,
        var question3Feedback: String? = "",
        var question4: String? = "",
        var myNotes: String? = "",
    ) {

    }

    data class Scoring(
        var marketValue: String? = "",
        var myValuation: String? = "",
        val location: Int = 100,
        val streetAppeal: Int = 100,
        val internalLayout: Int = 100,
        val externalLayout: Int = 100,
        val qualityOfBuilding: Int = 100,
        val averageScore: Int = 100,
    ) {

    }
}
