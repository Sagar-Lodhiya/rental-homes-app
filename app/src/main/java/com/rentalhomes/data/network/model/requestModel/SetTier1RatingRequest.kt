package com.rentalhomes.data.network.model.requestModel

class SetTier1RatingRequest(
    val userId: Int,
    val propertyId: Int,
    val location: Int,
    val streetAppeal: Int,
    val internalLayout: Int,
    val externalLayout: Int,
    val qualityOfBuilding: Int,
    val averageScore: Int,
    val marketValue: Int,
    val myValuation: Int
) {
}