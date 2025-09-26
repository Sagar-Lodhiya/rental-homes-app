package com.rentalhomes.data.network.model.requestModel

data class AddItemsInTemplateRequest(
    var userId: Int,
    var categoryId: Int,
    var templateItems: ArrayList<String>,
)
