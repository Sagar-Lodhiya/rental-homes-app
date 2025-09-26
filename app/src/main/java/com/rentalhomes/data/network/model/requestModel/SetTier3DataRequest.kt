package com.rentalhomes.data.network.model.requestModel

data class SetTier3DataRequest(
    var userId: Int,
    var propertyId: Int,
    var likeCount: Int,
    var likePercent: Int,
    var disLikeCount: Int,
    var disLikePercent: Int,
    var templateType :Int,
    var data: ArrayList<Data>? = null,
) {
    data class Data(
        var featureId: Int,
        var like: Int,
    ) {

    }
}
