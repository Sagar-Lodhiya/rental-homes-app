package com.rentalhomes.data.network.model.responseModel

class CompareNowResponse (
    var status: Int = 0,
    var message: String? = "",
    var data: Data
) {
    data class Data(
        val PropertyDetails: ArrayList<PropertyDetail>,
        val PropertyScore: Score
    ) {
        data class PropertyDetail(
            val propertyName: String,
            val propertyId: Int,
            val PropertyDate: String,
            val PropertyImage: String,
            val PropertyThumbImage: String,
            val PropertyAddress: String,
            val PropertyCity: String,
            val userName: String,
            val userImage: String
        ) {
//Inside of propertyDetail ArrayList
        }

        data class Score(
            val Tier1: ArrayList<TierOne>,
            val Tier2: ArrayList<TierTwo>,
            val Tier3: TierThree
        ) {
            data class TierOne(
                val categoryName: String,
                val score1: Int,
                val score2: Int,
                val score3:Int
            ) {
                // Inside of Tier 1
            }

            data class TierTwo(
                val categoryName: String,
                val score1: Int,
                val score2: Int,
                val score3:Int
            ) {
                // Inside of Tier 2
            }

            data class TierThree(
                val likeCount1: Int,
                val likePercent1: Int,
                val disLikeCount1: Int,
                val disLikePercent1: Int,
                val likeCount2: Int,
                val likePercent2: Int,
                val disLikeCount2: Int,
                val disLikePercent2: Int,
                val likeCount3: Int,
                val likePercent3: Int,
                val disLikeCount3: Int,
                val disLikePercent3: Int,
            ) {
                // Inside of Tier 3
            }
        }
    }
}