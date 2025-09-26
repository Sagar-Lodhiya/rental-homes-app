package com.rentalhomes.data.network.model.requestModel

class AddIgnorePropertyRequest(
    var userId: Int = 0,
    var agentId: Int = 0,
    var propertyId: Int = 0,
    var status: Int = 0
) {
}