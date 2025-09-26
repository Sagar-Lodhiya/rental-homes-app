package com.rentalhomes.data.network.model.requestModel

class SetTier1Request(
    val userId: Int,
    val propertyId: Int,
    val question1: Int,
    val question2: Int,
    val question3: Int,
    val question3Feedback: String,
    val question4: String,
    val myNotes: String
) {
}