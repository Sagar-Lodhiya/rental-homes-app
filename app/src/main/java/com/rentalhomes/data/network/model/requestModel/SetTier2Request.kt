package com.rentalhomes.data.network.model.requestModel

data class SetTier2Request(
    val userId: Int = 0,
    val propertyId: Int = 0,
    val kitchen: Int = 0,
    val livingAreas: Int = 0,
    val bedroom1: Int = 0,
    val bedroom2: Int = 0,
    val bedroom3: Int = 0,
    val bedroom4: Int = 0,
    val bathrooms: Int = 0,
    val mediaRoom: Int = 0,
    val laundry: Int = 0,
    val study: Int = 0,
    val storage: Int = 0,
    val parking: Int = 0,
    val patio: Int = 0,
    val externalAreas: Int = 0,
    val averageScore: Int = 0,
) {}
